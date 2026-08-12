package com.example.admin.modules.categories

data class Category(
    val categoryId: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val parentCategoryId: String = "", // empty if root
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
