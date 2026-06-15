package com.edu.core.database.converter

import androidx.room.TypeConverter

class IntListConverter {

    @TypeConverter
    fun fromIntList(value: List<Int>): String = value.joinToString(",")

    @TypeConverter
    fun toIntList(value: String): List<Int> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { it.trim().toInt() }
}
