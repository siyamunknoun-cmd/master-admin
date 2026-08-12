package com.example.admin.modules.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PromotionsViewModel : ViewModel() {
    private val _promotions = MutableStateFlow<List<Promotion>>(emptyList())
    val promotions: StateFlow<List<Promotion>> = _promotions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchPromotions()
    }

    fun fetchPromotions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                FirebaseProvider.firestore.collection("promotions")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Promotion::class.java)?.copy(promoId = doc.id)
                        }
                        if (list.isEmpty()) {
                            _promotions.value = listOf(
                                Promotion("pr_1", "Mega Monsoon Grocery Sale", "Up to 40% OFF on fresh produce & essentials", "Banner Slider", "", "2026-08-01", "2026-08-20", true, 1),
                                Promotion("pr_2", "Flash Hour Deals", "Limited time hourly specials in Pattoki", "Flash Sale", "", "2026-08-12", "2026-08-15", true, 2),
                                Promotion("pr_3", "Organic Dairy & Milk", "Directly sourced from local Pattoki farms", "Featured Category", "", "2026-08-01", "2026-09-01", true, 3)
                            )
                        } else {
                            _promotions.value = list
                        }
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        _promotions.value = listOf(
                            Promotion("pr_1", "Mega Monsoon Grocery Sale", "Up to 40% OFF on fresh produce & essentials", "Banner Slider", "", "2026-08-01", "2026-08-20", true, 1)
                        )
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun addPromotion(title: String, subtitle: String, type: String, start: String, end: String) {
        viewModelScope.launch {
            val promo = Promotion(
                promoId = "pr_" + System.currentTimeMillis(),
                title = title,
                subtitle = subtitle,
                type = type,
                startDate = start,
                endDate = end,
                isActive = true
            )
            FirebaseProvider.firestore.collection("promotions").document(promo.promoId)
                .set(promo)
                .addOnSuccessListener { fetchPromotions() }
            _promotions.value = _promotions.value + promo
        }
    }

    fun togglePromotionStatus(promoId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            FirebaseProvider.firestore.collection("promotions").document(promoId)
                .update("isActive", !currentStatus)
                .addOnSuccessListener { fetchPromotions() }
            _promotions.value = _promotions.value.map {
                if (it.promoId == promoId) it.copy(isActive = !currentStatus) else it
            }
        }
    }
}
