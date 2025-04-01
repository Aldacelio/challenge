<script setup>
import { ref, computed, watch } from 'vue'
import { MagnifyingGlassIcon } from '@heroicons/vue/24/outline'
import debounce from 'lodash/debounce'

const searchQuery = ref('')
const operators = ref([])
const totalResults = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const itemsPerPage = 10

const totalPages = computed(() => {
    return Math.ceil(totalResults.value / itemsPerPage)
})

const searchOperators = debounce(async (query, page = 1) => {
    if (!query) {
        operators.value = []
        totalResults.value = 0
        return
    }

    loading.value = true
    try {
        const response = await fetch(
            `http://localhost:5000/api/search?q=${encodeURIComponent(query)}&page=${page}&per_page=${itemsPerPage}`
        )
        const data = await response.json()
        operators.value = data.items || []
        totalResults.value = data.total
    } catch (error) {
        console.error('Error fetching operators:', error)
        operators.value = []
        totalResults.value = 0
    } finally {
        loading.value = false
    }
}, 300)

function changePage(page) {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page
        searchOperators(searchQuery.value, page)
    }
}

function goToFirstPage() {
    changePage(1)
}

function goToLastPage() {
    changePage(totalPages.value)
}

function goToPreviousPage() {
    changePage(currentPage.value - 1)
}

function goToNextPage() {
    changePage(currentPage.value + 1)
}

watch(searchQuery, (newQuery) => {
    searchOperators(newQuery)
})
</script>

<template>
    <div class="w-full">
        <div class="relative max-w-xl mx-auto mb-8">
            <MagnifyingGlassIcon class="h-5 w-5 absolute left-3 top-3 text-gray-400" />
            <input v-model="searchQuery" type="search" placeholder="Buscar por nome da operadora ou registro ANS..."
                class="w-full pl-10 pr-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 focus:border-transparent" />
        </div>

        <div class="space-y-4 w-full">
            <div class="overflow-x-auto w-full">
                <table class="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
                    <thead class="bg-gray-50 dark:bg-gray-800">
                        <tr>
                            <th v-for="header in ['Registro ANS', 'CNPJ', 'Razão Social', 'Modalidade', 'Cidade', 'UF', 'Representante']"
                                :key="header"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                {{ header }}
                            </th>
                        </tr>
                    </thead>
                    <tbody class="bg-white dark:bg-gray-900 divide-y divide-gray-200 dark:divide-gray-800">
                        <tr v-if="loading" class="hover:bg-gray-50 dark:hover:bg-gray-800">
                            <td colspan="7" class="px-6 py-4 text-center">
                                <div class="flex justify-center">
                                    <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
                                </div>
                            </td>
                        </tr>
                        <tr v-else-if="operators.length === 0 && searchQuery"
                            class="hover:bg-gray-50 dark:hover:bg-gray-800">
                            <td colspan="7" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                                Nenhum resultado encontrado
                            </td>
                        </tr>
                        <tr v-else-if="!searchQuery" class="hover:bg-gray-50 dark:hover:bg-gray-800">
                            <td colspan="7" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                                Digite algo para buscar operadoras
                            </td>
                        </tr>
                        <template v-else>
                            <tr v-for="operator in operators" :key="operator.Registro_ANS"
                                class="hover:bg-gray-50 dark:hover:bg-gray-800">
                                <td
                                    class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                                    {{ operator.Registro_ANS }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.CNPJ }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.Razao_Social }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.Modalidade }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.Cidade }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.UF }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-500 dark:text-gray-400">
                                    {{ operator.Representante }}
                                </td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>

            <!-- Paginação -->
            <div v-if="operators.length > 0" class="space-y-4">
                <!-- Nova Paginação -->
                <div v-if="totalPages > 1" class="flex justify-center items-center space-x-2 mt-4">
                    <!-- Primeira página -->
                    <button @click="goToFirstPage" :disabled="currentPage === 1" :class="[
                        'px-2 py-1 rounded',
                        currentPage === 1
                            ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    ]">
                        &lt;&lt;
                    </button>

                    <!-- Página anterior -->
                    <button @click="goToPreviousPage" :disabled="currentPage === 1" :class="[
                        'px-2 py-1 rounded',
                        currentPage === 1
                            ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    ]">
                        &lt;
                    </button>

                    <!-- Indicador de página atual -->
                    <span class="text-sm text-gray-700">
                        Página {{ currentPage }} de {{ totalPages }}
                    </span>

                    <!-- Próxima página -->
                    <button @click="goToNextPage" :disabled="currentPage === totalPages" :class="[
                        'px-2 py-1 rounded',
                        currentPage === totalPages
                            ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    ]">
                        &gt;
                    </button>

                    <!-- Última página -->
                    <button @click="goToLastPage" :disabled="currentPage === totalPages" :class="[
                        'px-2 py-1 rounded',
                        currentPage === totalPages
                            ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    ]">
                        &gt;&gt;
                    </button>
                </div>

                <div class="text-sm text-gray-500 dark:text-gray-400 text-center">
                    Mostrando {{ (currentPage - 1) * itemsPerPage + 1 }} a
                    {{ Math.min(currentPage * itemsPerPage, totalResults) }}
                    de {{ totalResults }} resultados
                </div>
            </div>
        </div>
    </div>
</template>