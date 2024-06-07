package com.insightforge.edafpmi.services

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("hits") val hits: List<Hit>,
    @SerializedName("_links") val links: Links?
)

data class Hit(
    @SerializedName("recipe") val recipe: Recipe
)

data class Links(
    @SerializedName("next") val next: Link?
)

data class Link(
    @SerializedName("href") val href: String
)

data class Recipe(
    @SerializedName("label") val label: String,
    @SerializedName("image") val image: String,
    @SerializedName("calories") val calories: Double,
    @SerializedName("ingredientLines") val ingredientLines: List<String>
)