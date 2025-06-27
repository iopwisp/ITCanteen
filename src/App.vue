<template>
  <div class="min-h-screen bg-gray-100">
    <nav class="bg-gray-800 text-white p-4 shadow-lg">
      <div class="container mx-auto flex justify-between items-center">
        <div class="text-2xl font-bold">IT Canteen</div>
        <div class="space-x-4">
          <router-link to="/" class="hover:text-green-400 transition">Меню</router-link>
          <router-link to="/cart" class="hover:text-green-400 transition">Корзина</router-link>
        </div>
      </div>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>