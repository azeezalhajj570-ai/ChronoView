package com.chronoview.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val cartDao: CartDao) {
    fun observeCartItems(): Flow<List<CartItemUi>> =
        cartDao.observeCartItems().map { entities ->
            entities.map {
                CartItemUi(
                    watchId = it.watchId,
                    name = it.name,
                    brand = it.brand,
                    imageUrl = it.imageUrl,
                    unitPrice = it.unitPrice,
                    quantity = it.quantity
                )
            }
        }

    suspend fun addToCart(watch: Watch) {
        val existing = cartDao.getByWatchId(watch.id)
        val updated = if (existing == null) {
            CartItemEntity(
                watchId = watch.id,
                name = watch.name,
                brand = watch.brand,
                imageUrl = watch.imageUrls.first(),
                unitPrice = watch.price,
                quantity = 1
            )
        } else {
            existing.copy(quantity = existing.quantity + 1)
        }
        cartDao.upsert(updated)
    }

    suspend fun increment(watchId: Int, currentQuantity: Int) {
        cartDao.updateQuantity(watchId, currentQuantity + 1)
    }

    suspend fun decrement(watchId: Int, currentQuantity: Int) {
        if (currentQuantity <= 1) cartDao.deleteByWatchId(watchId)
        else cartDao.updateQuantity(watchId, currentQuantity - 1)
    }
}
