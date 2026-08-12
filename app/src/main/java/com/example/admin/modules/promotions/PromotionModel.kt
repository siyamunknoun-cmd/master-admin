package com.example.admin.modules.promotions

data class Promotion(
    val promoId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val type: String = "Banner Slider", // Banner Slider, Flash Sale, Featured Category, Best Seller
    val imageUrl: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isActive: Boolean = true,
    val priority: Int = 1
)
