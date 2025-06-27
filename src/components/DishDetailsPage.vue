<template>
  <div class="container mx-auto p-4">
    <div v-if="loading" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="animate-spin h-8 w-8 border-4 border-blue-500 border-t-transparent rounded-full"></div>
    </div>
    <div v-if="error" class="bg-red-100 text-red-700 p-4 rounded mb-4">{{ error }}</div>
    <div class="flex flex-col md:flex-row gap-4">
      <div class="w-full md:w-1/3 bg-white p-4 rounded-lg shadow">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Корзина</h2>
        <div v-if="cart.length === 0" class="text-gray-500">Корзина пуста</div>
        <div v-for="item in cart" :key="item.id" class="flex justify-between items-center mb-2 p-2 bg-gray-100 rounded">
          <span>{{ item.name }} (x{{ item.quantity }})</span>
          <span>{{ (item.price * item.quantity).toFixed(2) }} тг</span>
          <button @click="removeFromCart(item)" class="text-red-500 hover:text-red-700">Удалить</button>
        </div>
      </div>
      <div class="w-full md:w-2/3">
        <div class="bg-gray-800 text-white p-4 rounded-lg mb-4 flex justify-between items-center">
          <span class="text-xl font-bold">{{ categoryName }}</span>
          <span>Терминал: Столовая - справа<br>{{ currentDate }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow">
          <h3 class="text-2xl font-bold text-gray-800 mb-4">Блюда категории: {{ categoryName }}</h3>
          <div v-if="dishes.length === 0" class="text-gray-500">Нет блюд в этой категории</div>
          <div v-for="food in dishes" :key="food.id" class="flex justify-between items-center mb-4 p-2 bg-gray-100 rounded">
            <div>
              <h4 class="text-lg font-semibold">{{ food.name }}</h4>
              <p class="text-gray-600">{{ food.description || 'Нет описания' }}</p>
              <p class="text-green-600 font-bold">{{ food.price.toFixed(2) }} тг</p>
            </div>
            <div class="flex items-center gap-2">
              <button @click="decreaseQuantity(food)" :disabled="getQuantity(food) === 0" class="px-2 py-1 bg-gray-300 rounded">-</button>
              <span>{{ getQuantity(food) }}</span>
              <button @click="increaseQuantity(food)" class="px-2 py-1 bg-gray-300 rounded">+</button>
              <button @click="addToCart(food)" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-gray-400" :disabled="addingItem === food.id">
                <span v-if="addingItem === food.id" class="flex items-center">
                  <svg class="animate-spin h-5 w-5 mr-2" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path></svg>
                  Добавление...
                </span>
                <span v-else>Добавить</span>
              </button>
            </div>
          </div>
          <button @click="goBack" class="mt-4 px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">Назад к категориям</button>
        </div>
        <div class="mt-4 text-right">
          <p class="text-xl font-bold">Итого: {{ total.toFixed(2) }} тг</p>
          <button @click="checkout" :disabled="cart.length === 0 || checkingOut" class="mt-2 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:bg-gray-400">
            <span v-if="checkingOut" class="flex items-center">
              <svg class="animate-spin h-5 w-5 mr-2" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path></svg>
              Оплата...
            </span>
            <span v-else>Оплатить картой</span>
          </button>
          <div v-if="orderStatus" class="text-green-600 mt-2">{{ orderStatus }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dishes: [],
      cart: [],
      categoryName: '',
      loading: true,
      error: null,
      addingItem: null,
      checkingOut: false,
      currentDate: new Date().toLocaleString('ru-RU', { timeZone: 'Asia/Almaty', hour12: false }),
      orderStatus: '',
      studentCard: null
    };
  },
  computed: {
    total() {
      return this.cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    }
  },
  methods: {
    goBack() {
      this.$router.push('/');
    },
    getQuantity(food) {
      const item = this.cart.find(i => i.id === food.id);
      return item ? item.quantity : 0;
    },
    async increaseQuantity(food) {
      this.addingItem = food.id;
      const item = this.cart.find(i => i.id === food.id) || { ...food, quantity: 0 };
      if (item.quantity === 0) this.cart.push(item);
      item.quantity++;
      const response = await this.$api.addToCart(1, this.cart);
      this.addingItem = null;
      if (!response.success) {
        item.quantity--;
        this.orderStatus = response.message;
      } else {
        this.cart = await this.$api.getCart(1);
      }
    },
    async decreaseQuantity(food) {
      const item = this.cart.find(i => i.id === food.id);
      if (item && item.quantity > 1) {
        item.quantity--;
        const response = await this.$api.addToCart(1, this.cart);
        if (!response.success) {
          item.quantity++;
          this.orderStatus = response.message;
        } else {
          this.cart = await this.$api.getCart(1);
        }
      } else if (item) {
        this.removeFromCart(item);
      }
    },
    async addToCart(food) {
      this.addingItem = food.id;
      const existing = this.cart.find(i => i.id === food.id);
      if (existing) existing.quantity++;
      else this.cart.push({ ...food, quantity: 1 });
      const response = await this.$api.addToCart(1, this.cart);
      this.addingItem = null;
      if (!response.success) {
        this.orderStatus = response.message;
        if (existing) existing.quantity--;
        else this.cart.pop();
      } else {
        this.cart = await this.$api.getCart(1);
      }
    },
    removeFromCart(item) {
      this.cart = this.cart.filter(i => i.id !== item.id);
      this.$api.addToCart(1, this.cart);
    },
    async checkout() {
      if (!this.studentCard) {
        this.orderStatus = 'Карта не найдена. Обратитесь к администратору.';
        return;
      }
      this.checkingOut = true;
      const response = await this.$api.checkout(1, this.studentCard.id);
      this.checkingOut = false;
      if (response.success) {
        this.orderStatus = 'Оплата успешна!';
        this.cart = [];
      } else {
        this.orderStatus = response.message;
      }
    }
  },
  async mounted() {
    try {
      const categoryId = this.$route.params.categoryId;
      this.dishes = await this.$api.fetchDishes(categoryId);
      // Получаем название категории
      const categories = await this.$api.getCategories();
      const category = categories.find(c => c.id == categoryId);
      this.categoryName = category ? category.name : 'Неизвестная категория';
      this.cart = await this.$api.getCart(1);
      this.studentCard = await this.$api.getStudentCard(1);
    } catch (error) {
      this.error = error.message;
    } finally {
      this.loading = false;
    }
  }
}
</script>