package com.insightforge.edafpmi.services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

data class RecipeResponse(val recipes: List<Recipe>, val nextPage: String?)

suspend fun fetchRecipes(query: String): RecipeResponse {
    val client = OkHttpClient()
    val url =
        "https://api.edamam.com/api/recipes/v2?type=public&q=${query}&app_id=a76ab7db&app_key=7b6e9ccfa5ae5e81968494cbc5b43eca"
    val request = Request.Builder()
        .url(url)
        .addHeader("accept", "application/json")
        .addHeader("Accept-Language", "en")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseBody = response.body?.string() ?: return RecipeResponse(emptyList(), null)
        val apiResponse = Gson().fromJson(responseBody, ApiResponse::class.java)
        val recipes = apiResponse.hits.map { it.recipe }
        val nextPage = apiResponse.links?.next?.href
        return RecipeResponse(recipes, nextPage)
    }
}

suspend fun fetchRecipesFromUrl(url: String): RecipeResponse {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .addHeader("accept", "application/json")
        .addHeader("Accept-Language", "en")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseBody = response.body?.string() ?: return RecipeResponse(emptyList(), null)
        val apiResponse = Gson().fromJson(responseBody, ApiResponse::class.java)
        val recipes = apiResponse.hits.map { it.recipe }
        val nextPage = apiResponse.links?.next?.href
        return RecipeResponse(recipes, nextPage)
    }
}