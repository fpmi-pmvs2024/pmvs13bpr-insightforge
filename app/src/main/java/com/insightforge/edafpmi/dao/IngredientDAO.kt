package com.insightforge.edafpmi.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.insightforge.edafpmi.models.Ingredient

class IngredientDAO(private val db: SQLiteDatabase) {

    fun insert(ingredient: Ingredient): Long {
        val values = ContentValues().apply {
            put("name", ingredient.name)
        }
        return db.insert("Ingredient", null, values)
    }

    fun getAll(): List<Ingredient> {
        val cursor: Cursor = db.query("Ingredient", null, null, null, null, null, null)
        val ingredients = mutableListOf<Ingredient>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            ingredients.add(Ingredient(id, name))
        }
        cursor.close()
        return ingredients
    }
}