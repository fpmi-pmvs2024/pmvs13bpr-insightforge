package com.insightforge.edafpmi.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.insightforge.edafpmi.database.DatabaseHelper
import com.insightforge.edafpmi.models.Dish
import com.insightforge.edafpmi.services.Recipe

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

fun saveRecipeToFavorites(context: Context, recipe: Recipe) {
    val dbHelper = DatabaseHelper(context)
    val db = dbHelper.writableDatabase

    val recipeValues = ContentValues().apply {
        put("label", recipe.label)
        put("image", recipe.image)
        put("calories", recipe.calories)
    }
    val recipeId = db.insert("Recipe", null, recipeValues)

    for (ingredient in recipe.ingredientLines) {
        var ingredientId: Long

        val cursor = db.query(
            "Ingredient",
            arrayOf("id"),
            "name = ?",
            arrayOf(ingredient),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            ingredientId = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
        } else {
            // Если ингредиента нет, вставляем его
            val ingredientValues = ContentValues().apply {
                put("name", ingredient)
            }
            ingredientId = db.insert("Ingredient", null, ingredientValues)
        }
        cursor.close()

        val recipeIngredientValues = ContentValues().apply {
            put("recipeId", recipeId)
            put("ingredientId", ingredientId)
        }
        db.insert("RecipeIngredient", null, recipeIngredientValues)
    }

    db.close()
}