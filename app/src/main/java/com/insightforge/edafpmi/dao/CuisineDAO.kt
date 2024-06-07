package com.insightforge.edafpmi.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.insightforge.edafpmi.models.Cuisine

class CuisineDAO(private val db: SQLiteDatabase) {

    fun insert(cuisine: Cuisine): Long {
        val values = ContentValues().apply {
            put("country", cuisine.country)
        }
        return db.insert("Cuisine", null, values)
    }

    fun getAll(): List<Cuisine> {
        val cursor: Cursor = db.query("Cuisine", null, null, null, null, null, null)
        val cuisines = mutableListOf<Cuisine>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val country = cursor.getString(cursor.getColumnIndexOrThrow("country"))
            cuisines.add(Cuisine(id, country))
        }
        cursor.close()
        return cuisines
    }
}