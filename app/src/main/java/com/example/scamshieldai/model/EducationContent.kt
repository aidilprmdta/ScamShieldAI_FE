package com.example.scamshieldai.model

data class EducationContent(
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val description: List<String>,
    val tips: List<String>,
    val imageResId: Int? = null
)
