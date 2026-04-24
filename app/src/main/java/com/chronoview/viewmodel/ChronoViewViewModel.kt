package com.chronoview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoview.data.CartItemUi
import com.chronoview.data.CartRepository
import com.chronoview.data.ChronoViewDatabase
import com.chronoview.data.Watch
import com.chronoview.data.WatchCategory
import com.chronoview.data.WatchSeedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.content.Context

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: WatchCategory? = null,
    val watches: List<Watch> = emptyList()
)

class ChronoViewViewModel(private val repository: CartRepository) : ViewModel() {
    private val allWatches = WatchSeedData.watches

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<WatchCategory?>(null)

    val homeUiState: StateFlow<HomeUiState> = combine(searchQuery, selectedCategory) { query, category ->
        val filtered = allWatches.filter { watch ->
            val matchesText = query.isBlank() ||
                watch.name.contains(query, ignoreCase = true) ||
                watch.brand.contains(query, ignoreCase = true)
            val matchesCategory = category == null || watch.category == category
            matchesText && matchesCategory
        }
        HomeUiState(query, category, filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState(watches = allWatches))

    val cartItems: StateFlow<List<CartItemUi>> = repository.observeCartItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategory(category: WatchCategory?) {
        selectedCategory.value = category
    }

    fun getWatchById(id: Int): Watch? = allWatches.firstOrNull { it.id == id }

    fun addToCart(watch: Watch) {
        viewModelScope.launch { repository.addToCart(watch) }
    }

    fun increment(item: CartItemUi) {
        viewModelScope.launch { repository.increment(item.watchId, item.quantity) }
    }

    fun decrement(item: CartItemUi) {
        viewModelScope.launch { repository.decrement(item.watchId, item.quantity) }
    }

    fun generateAiDescription(watch: Watch): String {
        val openings = listOf(
            "Precision meets personality with the",
            "Engineered for collectors, the",
            "A sophisticated statement, the"
        )
        val finishes = listOf(
            "Its details feel tailored for modern connoisseurs.",
            "It balances performance and prestige effortlessly.",
            "Designed to elevate every moment from boardroom to weekend."
        )
        val opening = openings[watch.id % openings.size]
        val finish = finishes[watch.id % finishes.size]
        return "$opening ${watch.name} by ${watch.brand}. ${watch.baseDescription} $finish"
    }

    val totalPrice: StateFlow<Double> = cartItems
        .map { items -> items.sumOf { it.lineTotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}

class ChronoViewViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = ChronoViewDatabase.getInstance(context).cartDao()
        val repository = CartRepository(dao)
        @Suppress("UNCHECKED_CAST")
        return ChronoViewViewModel(repository) as T
    }
}
