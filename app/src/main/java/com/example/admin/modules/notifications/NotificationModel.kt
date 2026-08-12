package com.example.admin.modules.notifications

data class BroadcastNotification(
    val notifId: String = "",
    val title: String = "",
    val message: String = "",
    val targetAudience: String = "All Customers", // All Customers, Pattoki Only, VIP Members
    val sentAt: String = "2026-08-11 14:00",
    val status: String = "Sent via FCM"
)
