<script setup>
import { useTheme } from './composables/theme'
import OperatorSearch from './components/OperatorSearch.vue'
import { SunIcon, MoonIcon } from '@heroicons/vue/24/outline'

const { isDark, toggleTheme } = useTheme()
</script>

<template>
  <div :class="isDark ? 'dark' : ''" class="min-h-screen w-full">
    <div class="bg-white dark:bg-gray-900 min-h-screen w-full transition-colors duration-200">
      <nav class="bg-white dark:bg-gray-800 shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div class="flex justify-between items-center">
            <div class="flex items-center">
              <h1 class="text-xl font-semibold text-gray-900 dark:text-white">
                Busca de Operadoras ANS
              </h1>
            </div>
            <button @click="toggleTheme"
              class="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none">
              <SunIcon v-if="isDark" class="h-5 w-5 text-gray-400" />
              <MoonIcon v-else class="h-5 w-5 text-gray-600" />
            </button>
          </div>
        </div>
      </nav>

      <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <OperatorSearch />
      </main>
    </div>
  </div>
</template>

<style>
#app {
  width: 100%;
  max-width: 100%;
  margin: 0;
  padding: 0;
}
</style>