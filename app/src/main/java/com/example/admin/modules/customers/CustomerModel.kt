package com.example.admin.modules.customers

data class Customer(
    val customerId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val savedAddresses: List<String> = emptyList(),
    val totalOrdersCount: Int = 0,
    val totalSpent: Double = 0.0,
    val isAccountActive: Boolean = true,
    val isGuest: Boolean = false,
    val registeredAt: Long = System.currentTimeMillis()
)
