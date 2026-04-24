package com.chronoview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoview.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.content.Context

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: WatchCategory? = null,
    val watches: List<Watch> = emptyList()
)

class ChronoViewViewModel(private val repository: ChronoViewRepository) : ViewModel() {
    
    init {
        viewModelScope.launch {
            repository.ensureSeedData()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<WatchCategory?>(null)

    val watches: StateFlow<List<Watch>> = repository.observeAllWatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homeUiState: StateFlow<HomeUiState> = combine(_searchQuery, _selectedCategory, watches) { query, category, allWatches ->
        val filtered = allWatches.filter { watch ->
            val matchesText = query.isBlank() ||
                watch.name.contains(query, ignoreCase = true) ||
                watch.brand.contains(query, ignoreCase = true)
            val matchesCategory = category == null || watch.category == category
            matchesText && matchesCategory
        }
        HomeUiState(query, category, filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    val cartItems: StateFlow<List<CartItemUi>> = repository.observeCartItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val orderHistory: StateFlow<List<OrderWithItems>> = repository.observeOrders().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearMessage() {
        _snackbarMessage.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: WatchCategory?) {
        _selectedCategory.value = category
    }

    fun getWatchById(id: Int): Watch? = watches.value.firstOrNull { it.id == id }

    fun addToCart(watch: Watch) {
        viewModelScope.launch { 
            repository.addToCart(watch)
            showMessage("${watch.name} added to cart")
        }
    }

    fun increment(item: CartItemUi) {
        viewModelScope.launch { repository.increment(item.watchId, item.quantity) }
    }

    fun decrement(item: CartItemUi) {
        viewModelScope.launch { repository.decrement(item.watchId, item.quantity) }
    }

    fun checkout() {
        viewModelScope.launch {
            val items = cartItems.value
            val total = totalPrice.value
            repository.checkout(items, total)
            showMessage("Order placed successfully!")
        }
    }

    // --- Admin Methods ---
    fun saveProduct(watch: Watch) {
        viewModelScope.launch {
            repository.upsertWatch(watch)
            showMessage(if (watch.id == 0) "Product added" else "Product updated")
        }
    }

    fun deleteProduct(watch: Watch) {
        viewModelScope.launch {
            repository.deleteWatch(watch)
            showMessage("Product deleted")
        }
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
    
    // Auth logic (Mock)
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin = _isAdmin.asStateFlow()

    fun login(email: String, pass: String): Boolean {
        if (email.contains("@") && pass.length >= 6) {
            _isLoggedIn.value = true
            _isAdmin.value = email.lowercase() == "admin@chronoview.com"
            return true
        }
        return false
    }

    fun signup(name: String, email: String, pass: String): Boolean {
        if (name.isNotBlank() && email.contains("@") && pass.length >= 6) {
            _isLoggedIn.value = true
            _isAdmin.value = email.lowercase() == "admin@chronoview.com"
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
        _isAdmin.value = false
    }
}

class ChronoViewViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = ChronoViewDatabase.getInstance(context)
        val repository = ChronoViewRepository(db.cartDao(), db.watchDao())
        @Suppress("UNCHECKED_CAST")
        return ChronoViewViewModel(repository) as T
    }
}
