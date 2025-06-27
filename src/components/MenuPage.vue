<template>
  <div class="container mx-auto p-4">
    <div v-if="loading" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50">
      <div class="animate-spin h-8 w-8 border-4 border-blue-500 border-t-transparent rounded-full"></div>
    </div>
    <div v-if="error" class="bg-red-100 text-red-700 p-4 rounded mb-4">{{ error }}</div>
    <div class="flex flex-col md:flex-row gap-4">
      <div class="w-full md:w-1/3 bg-white p-4 rounded-lg shadow">
        <h2 class="text-2xl font-bold text-gray-800 mb-4">Категории</h2>
        <div v-if="categories.length === 0" class="text-gray-500">Нет категорий</div>
        <div class="grid grid-cols-2 gap-2">
          <div v-for="category in categories" :key="category.id" class="bg-gray-100 p-2 rounded cursor-pointer hover:bg-blue-100 transition" @click="goToCategory(category.id)">
            <div class="flex items-center gap-2">
              <span class="text-lg font-semibold">{{ category.name }}</span>
              <span class="text-xs text-gray-500">{{ category.description }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="w-full md:w-2/3">
        <div class="bg-gray-800 text-white p-4 rounded-lg mb-4 flex justify-between items-center">
          <span class="text-xl font-bold">IT Canteen</span>
          <span>Терминал: Столовая - справа<br>{{ currentDate }}</span>
        </div>
        <div class="text-gray-500 text-center mt-10">
          <p>Выберите категорию для просмотра блюд.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      categories: [],
      loading: true,
      error: null,
      currentDate: new Date().toLocaleString('ru-RU', { timeZone: 'Asia/Almaty', hour12: false })
    };
  },
  methods: {
    goToCategory(categoryId) {
      this.$router.push(`/categories/${categoryId}`);
    }
  },
  async mounted() {
    try {
      this.categories = await this.$api.getCategories();
    } catch (error) {
      this.error = error.message;
    } finally {
      this.loading = false;
    }
  }
}
</script>