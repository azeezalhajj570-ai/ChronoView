package com.chronoview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WatchCategory {
    Luxury,
    Sport,
    Casual
}

data class Watch(
    val id: Int,
    val name: String,
    val brand: String,
    val category: WatchCategory,
    val price: Double,
    val rating: Double,
    val imageUrls: List<String>,
    val baseDescription: String
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val watchId: Int,
    val name: String,
    val brand: String,
    val imageUrl: String,
    val unitPrice: Double,
    val quantity: Int
)

data class CartItemUi(
    val watchId: Int,
    val name: String,
    val brand: String,
    val imageUrl: String,
    val unitPrice: Double,
    val quantity: Int
) {
    val lineTotal: Double get() = unitPrice * quantity
}
