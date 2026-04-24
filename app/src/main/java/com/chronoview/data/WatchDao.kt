package com.chronoview.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchDao {
    @Query("SELECT * FROM watches")
    fun observeAll(): Flow<List<WatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(watch: WatchEntity)

    @Delete
    suspend fun delete(watch: WatchEntity)

    @Query("SELECT * FROM watches WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): WatchEntity?

    @Query("SELECT COUNT(*) FROM watches")
    suspend fun getCount(): Int

    @Insert
    suspend fun insertAll(watches: List<WatchEntity>)
}
