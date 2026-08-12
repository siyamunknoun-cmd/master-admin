package com.example.admin.modules.coupons

data class Coupon(
    val couponId: String = "",
    val code: String = "",
    val discountType: String = "percentage", // percentage or fixed
    val discountValue: Double = 0.0,
    val minOrderValue: Double = 0.0,
    val startDate: String = "",
    val endDate: String = "",
    val usageLimit: Int = 100,
    val perCustomerLimit: Int = 1,
    val isActive: Boolean = true,
    val categoryRestriction: String = "All Categories",
    val productRestriction: String = "All Products"
)
