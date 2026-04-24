package com.chronoview.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CartItemEntity::class, OrderEntity::class, OrderItemEntity::class, WatchEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ChronoViewDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun watchDao(): WatchDao

    companion object {
        @Volatile
        private var INSTANCE: ChronoViewDatabase? = null

        fun getInstance(context: Context): ChronoViewDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChronoViewDatabase::class.java,
                    "chronoview.db"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
