package com.insightforge.edafpmi.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.insightforge.edafpmi.models.Dish

class DishDAO(private val db: SQLiteDatabase) {

    fun insert(dish: Dish): Long {
        val values = ContentValues().apply {
            put("name", dish.name)
            put("calories", dish.calories)
            put("countryId", dish.countryId)
        }
        return db.insert("Dish", null, values)
    }

    fun getAll(): List<Dish> {
        val cursor: Cursor = db.query("Dish", null, null, null, null, null, null)
        val dishes = mutableListOf<Dish>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories"))
            val countryId = cursor.getLong(cursor.getColumnIndexOrThrow("countryId"))
            dishes.add(Dish(id, name, calories, countryId))
        }
        cursor.close()
        return dishes
    }

    fun findByName(name: String): List<Dish> {
        val cursor: Cursor = db.query(
            "Dish",
            null,
            "name LIKE ?",
            arrayOf("%$name%"),
            null,
            null,
            null
        )
        val dishes = mutableListOf<Dish>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val dishName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories"))
            val countryId = cursor.getLong(cursor.getColumnIndexOrThrow("countryId"))
            dishes.add(Dish(id, dishName, calories, countryId))
        }
        cursor.close()
        return dishes
    }

    fun findByIngredient(ingredientId: Long): List<Dish> {
        val query = """
            SELECT Dish.id, Dish.name, Dish.calories, Dish.countryId
            FROM Dish
            INNER JOIN DishIngredient ON Dish.id = DishIngredient.dishId
            WHERE DishIngredient.ingredientId = ?
        """
        val cursor: Cursor = db.rawQuery(query, arrayOf(ingredientId.toString()))
        val dishes = mutableListOf<Dish>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories"))
            val countryId = cursor.getLong(cursor.getColumnIndexOrThrow("countryId"))
            dishes.add(Dish(id, name, calories, countryId))
        }
        cursor.close()
        return dishes
    }
}