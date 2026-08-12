package com.example.admin.modules.reviews

data class ProductReview(
    val reviewId: String = "",
    val productName: String = "",
    val customerName: String = "",
    val rating: Int = 5,
    val comment: String = "",
    val isApproved: Boolean = false,
    val isHidden: Boolean = false,
    val date: String = "2026-08-11"
)
