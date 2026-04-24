package com.chronoview.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class ChronoViewDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: ChronoViewDatabase? = null

        fun getInstance(context: Context): ChronoViewDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChronoViewDatabase::class.java,
                    "chronoview.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
