<template>
  <div class="container mx-auto p-4">
    <div v-if="loading" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="animate-spin h-8 w-8 border-4 border-blue-500 border-t-transparent rounded-full"></div>
    </div>
    <div v-if="error" class="bg-red-100 text-red-700 p-4 rounded mb-4">{{ error }}</div>
    <div class="bg-white p-4 rounded-lg shadow">
      <h2 class="text-2xl font-bold text-gray-800 mb-4">Корзина</h2>
      <div v-if="cart.length === 0" class="text-gray-500">Корзина пуста</div>
      <div v-for="item in cart" :key="item.id" class="flex justify-between items-center mb-4 p-2 bg-gray-100 rounded">
        <div>
          <h4 class="text-lg font-semibold">{{ item.name }}</h4>
          <p class="text-gray-600">{{ item.description || 'Нет описания' }}</p>
          <p class="text-green-600 font-bold">{{ item.price.toFixed(2) }} тг</p>
        </div>
        <div class="flex items-center gap-2">
          <button @click="decreaseQuantity(item)" :disabled="item.quantity === 1 || updatingItem === item.id" class="px-2 py-1 bg-gray-300 rounded disabled:bg-gray-200">-</button>
          <span>{{ item.quantity }}</span>
          <button @click="increaseQuantity(item)" :disabled="updatingItem === item.id" class="px-2 py-1 bg-gray-300 rounded disabled:bg-gray-200">+</button>
          <button @click="removeFromCart(item)" class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">Удалить</button>
        </div>
      </div>
      <div class="text-right mt-4">
        <p class="text-xl font-bold">Итого: {{ total.toFixed(2) }} тг</p>
        <button @click="checkout" :disabled="cart.length === 0 || checkingOut" class="mt-2 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:bg-gray-400">
          <span v-if="checkingOut" class="flex items-center">
            <svg class="animate-spin h-5 w-5 mr-2" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path></svg>
            Оплата...
          </span>
          <span v-else>Оплатить</span>
        </button>
        <button @click="goBack" class="mt-2 ml-2 px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">Назад к меню</button>
        <div v-if="orderStatus" class="text-green-600 mt-2">{{ orderStatus }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cart: [],
      orderStatus: '',
      loading: true,
      error: null,
      updatingItem: null,
      checkingOut: false,
      studentCard: null
    };
  },
  computed: {
    total() {
      return this.cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    }
  },
  methods: {
    async increaseQuantity(item) {
      this.updatingItem = item.id;
      item.quantity++;
      const response = await this.$api.addToCart(1, this.cart);
      this.updatingItem = null;
      if (!response.success) {
        item.quantity--;
        this.orderStatus = response.message;
      } else {
        this.cart = await this.$api.getCart(1);
      }
    },
    async decreaseQuantity(item) {
      this.updatingItem = item.id;
      if (item.quantity > 1) {
        item.quantity--;
        const response = await this.$api.addToCart(1, this.cart);
        if (!response.success) {
          item.quantity++;
          this.orderStatus = response.message;
        } else {
          this.cart = await this.$api.getCart(1);
        }
      } else {
        this.removeFromCart(item);
      }
      this.updatingItem = null;
    },
    removeFromCart(item) {
      this.cart = this.cart.filter(i => i.id !== item.id);
      this.$api.addToCart(1, this.cart);
    },
    goBack() {
      this.$router.push('/');
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