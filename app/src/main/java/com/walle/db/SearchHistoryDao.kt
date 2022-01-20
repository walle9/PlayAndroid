package com.walle.db

import androidx.room.*
import com.walle.playandroid.data.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao{
    @Query("SELECT * FROM searchhistory")
    fun getAll(): Flow<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history:SearchHistory)

    @Delete
    fun delete(history: SearchHistory)

    @Query("DELETE FROM searchhistory")
    fun clear()
}