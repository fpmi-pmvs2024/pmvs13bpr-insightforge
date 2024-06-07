package com.insightforge.edafpmi.ui

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.insightforge.edafpmi.database.DatabaseHelper
import com.insightforge.edafpmi.services.Recipe
import com.insightforge.edafpmi.services.RecipeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SavedDishesPage(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var savedRecipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.query("Recipe", null, null, null, null, null, null)

            val recipes = mutableListOf<Recipe>()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val label = cursor.getString(cursor.getColumnIndexOrThrow("label"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val calories = cursor.getDouble(cursor.getColumnIndexOrThrow("calories"))

                val ingredientCursor = db.rawQuery(
                    "SELECT name FROM Ingredient INNER JOIN RecipeIngredient ON Ingredient.id = RecipeIngredient.ingredientId WHERE RecipeIngredient.recipeId = ?",
                    arrayOf(id.toString())
                )

                val ingredients = mutableListOf<String>()
                while (ingredientCursor.moveToNext()) {
                    ingredients.add(ingredientCursor.getString(ingredientCursor.getColumnIndexOrThrow("name")))
                }
                ingredientCursor.close()

                recipes.add(Recipe(id, label, image, calories, ingredients))
            }
            cursor.close()
            db.close()
            savedRecipes = recipes
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(text = "Saved Dishes", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val scrollState = rememberScrollState()

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                savedRecipes.forEach { recipe ->
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        SavedRecipeDetailDialog(
                            recipe = recipe,
                            onDismiss = { showDialog = false },
                            context = context,
                            onDelete = { recipeToDelete ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    val dbHelper = DatabaseHelper(context)
                                    val db = dbHelper.writableDatabase
                                    db.delete("Recipe", "id = ?", arrayOf(recipeToDelete.id.toString()))
                                    savedRecipes = savedRecipes.filter { it.id != recipeToDelete.id }
                                }
                            }
                        )
                    }

                    RecipeItem(recipe = recipe, onClick = { showDialog = true })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Back to Search")
        }
    }
}

@Composable
fun SavedRecipeDetailDialog(
    recipe: Recipe,
    onDismiss: () -> Unit,
    context: Context,
    onDelete: (Recipe) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(recipe.label)
        },
        text = {
            Column {
                Image(
                    painter = rememberImagePainter(recipe.image),
                    contentDescription = recipe.label,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Calories: ${recipe.calories}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Ingredients:")
                recipe.ingredientLines.forEach { ingredient ->
                    Text(text = "- $ingredient")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDelete(recipe)
                    onDismiss()
                }
            ) {
                Text("Delete from Saved")
            }
        }
    )
}