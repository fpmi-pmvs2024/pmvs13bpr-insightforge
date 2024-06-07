package com.insightforge.edafpmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.insightforge.edafpmi.ui.*
import com.insightforge.edafpmi.ui.theme.EdaFPMITheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdaFPMITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainPage(navController)
        }
        composable("savedDishes") {
            SavedDishesPage(navController)
        }
        composable(
            "dishDetail/{dishName}",
            arguments = listOf(navArgument("dishName") { type = NavType.StringType })
        ) { backStackEntry ->
            val dishName = backStackEntry.arguments?.getString("dishName") ?: ""
            DishDetailPage(dishName)
        }
    }
}

