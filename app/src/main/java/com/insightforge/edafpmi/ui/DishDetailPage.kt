package com.insightforge.edafpmi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DishDetailPage(dishName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = dishName, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Calories: 400")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Country: Italy")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ingredients:")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Tomatoes: 200g")
        Text(text = "Pasta: 100g")
    }
}

