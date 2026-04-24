package com.chronoview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE watchId = :watchId LIMIT 1")
    suspend fun getByWatchId(watchId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE watchId = :watchId")
    suspend fun updateQuantity(watchId: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE watchId = :watchId")
    suspend fun deleteByWatchId(watchId: Int)
}
