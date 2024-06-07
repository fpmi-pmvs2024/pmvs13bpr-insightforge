package com.insightforge.edafpmi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "edafpmi.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCuisineTable = """
            CREATE TABLE Cuisine (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                country TEXT NOT NULL
            )
        """
        val createDishTable = """
            CREATE TABLE Dish (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                calories INTEGER NOT NULL,
                countryId INTEGER,
                FOREIGN KEY(countryId) REFERENCES Cuisine(id)
            )
        """
        val createIngredientTable = """
            CREATE TABLE Ingredient (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            )
        """
        val createDishIngredientTable = """
            CREATE TABLE DishIngredient (
                dishId INTEGER,
                ingredientId INTEGER,
                amount REAL,
                FOREIGN KEY(dishId) REFERENCES Dish(id),
                FOREIGN KEY(ingredientId) REFERENCES Ingredient(id),
                PRIMARY KEY (dishId, ingredientId)
            )
        """

        db.execSQL(createCuisineTable)
        db.execSQL(createDishTable)
        db.execSQL(createIngredientTable)
        db.execSQL(createDishIngredientTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS DishIngredient")
        db.execSQL("DROP TABLE IF EXISTS Ingredient")
        db.execSQL("DROP TABLE IF EXISTS Dish")
        db.execSQL("DROP TABLE IF EXISTS Cuisine")
        onCreate(db)
    }
}