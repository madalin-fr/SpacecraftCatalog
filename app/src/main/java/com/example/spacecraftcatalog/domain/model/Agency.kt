// domain/model/Agency.kt
package com.example.spacecraftcatalog.domain.model

data class Agency(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val foundingYear: String?,
    val administrator: String?,
    val url: String?
)