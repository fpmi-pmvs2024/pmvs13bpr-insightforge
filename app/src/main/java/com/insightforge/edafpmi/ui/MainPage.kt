package com.insightforge.edafpmi.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.insightforge.edafpmi.services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainPage(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val recipes = remember { mutableStateListOf<Recipe>() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var foundRecipesCount by remember { mutableStateOf(0) }
    var nextPage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(text = "Search Dishes", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (searchQuery.text.isEmpty()) {
                        Text(
                            text = "search dish...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val recipeResponse = fetchRecipes(searchQuery.text)
                                recipes.clear()
                                recipes.addAll(recipeResponse.recipes)
                                foundRecipesCount = recipeResponse.recipes.size
                                nextPage = recipeResponse.nextPage
                            } catch (e: Exception) {
                                Log.e("RecipeSearch", "Error fetching recipes", e)
                            }
                        }
                    }
                ) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Found: $foundRecipesCount")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                recipes.forEach { recipe ->
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        RecipeDetailDialog(recipe = recipe, onDismiss = { showDialog = false }, context = context)
                    }

                    RecipeItem(recipe = recipe, onClick = { showDialog = true })
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (nextPage != null) {
                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                try {
                                    val recipeResponse = fetchRecipesFromUrl(nextPage!!)
                                    recipes.addAll(recipeResponse.recipes)
                                    foundRecipesCount += recipeResponse.recipes.size
                                    nextPage = recipeResponse.nextPage
                                } catch (e: Exception) {
                                    Log.e("RecipeSearch", "Error fetching next page recipes", e)
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Show More")
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("savedDishes") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Saved Dishes")
        }
    }
}