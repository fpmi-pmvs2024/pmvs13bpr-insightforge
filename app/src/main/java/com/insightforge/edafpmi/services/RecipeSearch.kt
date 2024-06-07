package com.insightforge.edafpmi.services

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecipeSearch() {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val recipes = remember { mutableStateListOf<Recipe>() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    if (query.text.isEmpty()) {
                        Text("Search for recipes...")
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val fetchedRecipes = fetchRecipes(query.text)
                        recipes.clear()
                        recipes.addAll(fetchedRecipes.recipes)
                    } catch (e: Exception) {
                        Log.e("RecipeSearch", "Error fetching recipes", e)
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            recipes.forEach { recipe ->
                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    RecipeDetailDialog(recipe = recipe, onDismiss = { showDialog = false })
                }

                RecipeItem(recipe = recipe, onClick = { showDialog = true })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberImagePainter(recipe.image),
            contentDescription = recipe.label,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(recipe.label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun RecipeDetailDialog(recipe: Recipe, onDismiss: () -> Unit) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Calories: ${recipe.calories}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ingredients:", style = MaterialTheme.typography.bodyMedium)
                recipe.ingredientLines.forEach { ingredient ->
                    Text(ingredient, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Row {
                Button(onClick = onDismiss) {
                    Text("Close")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Здесь будет логика сохранения в избранное */ }) {
                    Text("Save to Favorites")
                }
            }
        }
    )
}