package com.example.admin.modules.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<BroadcastNotification>>(emptyList())
    val notifications: StateFlow<List<BroadcastNotification>> = _notifications.asStateFlow()

    init {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                FirebaseProvider.firestore.collection("notifications")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(BroadcastNotification::class.java)?.copy(notifId = doc.id)
                        }
                        if (list.isEmpty()) {
                            _notifications.value = listOf(
                                BroadcastNotification("nf_1", "Eid Mega Savings in Pattoki!", "Get flat 15% OFF across all grocery items today.", "All Customers", "2026-08-01 10:00", "Sent via FCM"),
                                BroadcastNotification("nf_2", "Fresh Milk Stock Arrived", "Direct farm fresh milk now available for delivery.", "Pattoki Residents", "2026-08-05 08:30", "Sent via FCM")
                            )
                        } else {
                            _notifications.value = list
                        }
                    }
                    .addOnFailureListener {
                        _notifications.value = listOf(
                            BroadcastNotification("nf_1", "Eid Mega Savings in Pattoki!", "Get flat 15% OFF across all grocery items today.", "All Customers", "2026-08-01 10:00", "Sent via FCM")
                        )
                    }
            } catch (e: Exception) {}
        }
    }

    fun sendBroadcast(title: String, message: String, audience: String) {
        viewModelScope.launch {
            val notif = BroadcastNotification(
                notifId = "nf_" + System.currentTimeMillis(),
                title = title,
                message = message,
                targetAudience = audience,
                sentAt = "Just now",
                status = "Sent via FCM"
            )
            FirebaseProvider.firestore.collection("notifications").document(notif.notifId)
                .set(notif)
                .addOnSuccessListener { fetchNotifications() }
            _notifications.value = listOf(notif) + _notifications.value
        }
    }
}
