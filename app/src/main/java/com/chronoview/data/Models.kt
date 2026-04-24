package com.chronoview.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

enum class WatchCategory {
    Luxury,
    Sport,
    Casual
}

@Entity(tableName = "watches")
data class WatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val category: WatchCategory,
    val price: Double,
    val rating: Double,
    val imageUrls: List<String>,
    val baseDescription: String
)

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

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

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

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val date: Long,
    val totalPrice: Double
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val watchId: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class OrderWithItems(
    val orderId: Int,
    val date: Long,
    val totalPrice: Double,
    val items: List<OrderItemEntity>
)
