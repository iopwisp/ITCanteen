import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import axios from 'axios';
import App from './App.vue';
import MenuPage from './components/MenuPage.vue';
import DishDetailsPage from './components/DishDetailsPage.vue';
import CartPage from './components/CartPage.vue';

axios.defaults.baseURL = 'http://localhost:8080/api';

const apiService = {
    async fetchMenu() {
        try {
            const response = await axios.get('/foods');
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.error || 'Ошибка загрузки меню');
        }
    },
    async fetchDishes(categoryId) {
        try {
            const response = await axios.get(`/foods/category/${categoryId}`);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.error || 'Ошибка загрузки блюд');
        }
    },
    async addToCart(studentId, items) {
        try {
            const response = await axios.post('/orders/cart', {
                studentId,
                items: items.map(item => ({ foodId: item.id, quantity: item.quantity || 1 }))
            });
            return { success: true, data: response.data };
        } catch (error) {
            return { 
                success: false, 
                message: error.response?.data?.error || 'Ошибка добавления в корзину' 
            };
        }
    },
    async getCart(studentId) {
        try {
            const response = await axios.get(`/orders/cart/${studentId}`);
            if (response.data) {
                return response.data.foods.map(food => ({
                    id: food.id,
                    name: food.name,
                    price: food.price,
                    quantity: 1
                }));
            }
            return [];
        } catch (error) {
            if (error.response?.status === 404) {
                return [];
            }
            throw new Error(error.response?.data?.error || 'Ошибка загрузки корзины');
        }
    },
    async getStudentCard(studentId) {
        try {
            const response = await axios.get(`/payments/card/${studentId}`);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.error || 'Ошибка получения карты');
        }
    },
    async checkout(studentId, cardId) {
        try {
            const response = await axios.post('/orders/checkout', null, {
                params: { studentId, cardId }
            });
            return { success: true, data: response.data };
        } catch (error) {
            return { 
                success: false, 
                message: error.response?.data?.error || 'Ошибка оплаты' 
            };
        }
    },
    async getCategories() {
        try {
            const response = await axios.get('/categories');
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.error || 'Ошибка загрузки категорий');
        }
    }
};

const routes = [
    { path: '/', component: MenuPage },
    { path: '/categories/:categoryId', component: DishDetailsPage },
    { path: '/cart', component: CartPage }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

const app = createApp(App);
app.use(router);
app.config.globalProperties.$api = apiService;
app.mount('#app');