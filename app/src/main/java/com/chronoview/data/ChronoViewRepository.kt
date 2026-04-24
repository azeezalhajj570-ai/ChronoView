package com.chronoview.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChronoViewRepository(
    private val cartDao: CartDao,
    private val watchDao: WatchDao
) {
    // --- Cart Methods ---
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
                imageUrl = watch.imageUrls.firstOrNull() ?: "",
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

    suspend fun checkout(items: List<CartItemUi>, totalPrice: Double) {
        if (items.isEmpty()) return
        
        val order = OrderEntity(date = System.currentTimeMillis(), totalPrice = totalPrice)
        val orderId = cartDao.insertOrder(order).toInt()
        
        val orderItems = items.map {
            OrderItemEntity(
                orderId = orderId,
                watchId = it.watchId,
                name = it.name,
                price = it.unitPrice,
                quantity = it.quantity
            )
        }
        cartDao.insertOrderItems(orderItems)
        cartDao.clearCart()
    }

    fun observeOrders(): Flow<List<OrderWithItems>> {
        return cartDao.observeOrders().map { orders ->
            orders.map { order ->
                val items = cartDao.getOrderItems(order.orderId)
                OrderWithItems(order.orderId, order.date, order.totalPrice, items)
            }
        }
    }

    // --- Product Methods ---
    fun observeAllWatches(): Flow<List<Watch>> {
        return watchDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun ensureSeedData() {
        if (watchDao.getCount() == 0) {
            val entities = WatchSeedData.watches.map { it.toEntity(it.id) }
            watchDao.insertAll(entities)
        }
    }

    suspend fun upsertWatch(watch: Watch) {
        watchDao.upsert(watch.toEntity(if (watch.id == 0) 0 else watch.id))
    }

    suspend fun deleteWatch(watch: Watch) {
        watchDao.delete(watch.toEntity(watch.id))
    }

    private fun WatchEntity.toDomain() = Watch(id, name, brand, category, price, rating, imageUrls, baseDescription)
    private fun Watch.toEntity(idOverride: Int) = WatchEntity(idOverride, name, brand, category, price, rating, imageUrls, baseDescription)
}
