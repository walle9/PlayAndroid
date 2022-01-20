package com.walle.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.walle.playandroid.data.SearchHistory
import com.walle.playandroid.ui.AppContext

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {

        @Volatile
        private var INSTANCE: SearchHistoryDatabase? = null

        fun getInstance(): SearchHistoryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(AppContext).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SearchHistoryDatabase::class.java, "searchHistory.db"
            )
                .build()
    }
}