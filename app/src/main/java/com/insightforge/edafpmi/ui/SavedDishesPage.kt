package com.insightforge.edafpmi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SavedDishesPage(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Saved Dishes", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        val savedDishes = listOf("Saved Dish 1", "Saved Dish 2", "Saved Dish 3")
        savedDishes.forEach { dish ->
            Text(
                text = dish,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("dishDetail/$dish")
                    }
            )
        }
    }
}

