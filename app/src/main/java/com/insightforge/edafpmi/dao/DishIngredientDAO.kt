package com.insightforge.edafpmi.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.insightforge.edafpmi.models.DishIngredient

class DishIngredientDAO(private val db: SQLiteDatabase) {

    fun insert(dishIngredient: DishIngredient): Long {
        val values = ContentValues().apply {
            put("dishId", dishIngredient.dishId)
            put("ingredientId", dishIngredient.ingredientId)
            put("amount", dishIngredient.amount)
        }
        return db.insert("DishIngredient", null, values)
    }

    fun getAll(): List<DishIngredient> {
        val cursor: Cursor = db.query("DishIngredient", null, null, null, null, null, null)
        val dishIngredients = mutableListOf<DishIngredient>()
        while (cursor.moveToNext()) {
            val dishId = cursor.getLong(cursor.getColumnIndexOrThrow("dishId"))
            val ingredientId = cursor.getLong(cursor.getColumnIndexOrThrow("ingredientId"))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
            dishIngredients.add(DishIngredient(dishId, ingredientId, amount))
        }
        cursor.close()
        return dishIngredients
    }
}