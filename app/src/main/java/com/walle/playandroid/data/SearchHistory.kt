package com.walle.playandroid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey
    val keyWord: String
)
