package com.example.admin.modules.staff

data class StaffMember(
    val staffId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "Staff",
    val isActive: Boolean = true,
    val permissions: List<String> = emptyList(),
    val phone: String = "",
    val assignedZone: String = "Pattoki Central",
    val createdAt: Long = System.currentTimeMillis()
)
