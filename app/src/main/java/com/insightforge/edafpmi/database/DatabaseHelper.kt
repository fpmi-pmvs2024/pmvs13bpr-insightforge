package com.insightforge.edafpmi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "edafpmi.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createRecipeTable = """
            CREATE TABLE Recipe (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                label TEXT NOT NULL,
                image TEXT NOT NULL,
                calories REAL NOT NULL
            )
        """
        val createIngredientTable = """
            CREATE TABLE Ingredient (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            )
        """
        val createRecipeIngredientTable = """
            CREATE TABLE RecipeIngredient (
                recipeId INTEGER,
                ingredientId INTEGER,
                FOREIGN KEY(recipeId) REFERENCES Recipe(id),
                FOREIGN KEY(ingredientId) REFERENCES Ingredient(id),
                PRIMARY KEY (recipeId, ingredientId)
            )
        """

        db.execSQL(createRecipeTable)
        db.execSQL(createIngredientTable)
        db.execSQL(createRecipeIngredientTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS RecipeIngredient")
        db.execSQL("DROP TABLE IF EXISTS Ingredient")
        db.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }
}