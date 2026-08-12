package com.example.admin.modules.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CouponsViewModel : ViewModel() {
    private val _coupons = MutableStateFlow<List<Coupon>>(emptyList())
    val coupons: StateFlow<List<Coupon>> = _coupons.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchCoupons()
    }

    fun fetchCoupons() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                FirebaseProvider.firestore.collection("coupons")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Coupon::class.java)?.copy(couponId = doc.id)
                        }
                        if (list.isEmpty()) {
                            _coupons.value = listOf(
                                Coupon("cp_1", "PATTOKI10", "percentage", 10.0, 500.0, "2026-08-01", "2026-08-31", 500, 1, true, "All Categories", "All Products"),
                                Coupon("cp_2", "SAVE500", "fixed", 500.0, 3000.0, "2026-08-01", "2026-08-20", 200, 1, true, "Grocery", "All Products")
                            )
                        } else {
                            _coupons.value = list
                        }
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        _coupons.value = listOf(
                            Coupon("cp_1", "PATTOKI10", "percentage", 10.0, 500.0, "2026-08-01", "2026-08-31", 500, 1, true, "All Categories", "All Products")
                        )
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun addCoupon(code: String, type: String, value: Double, minOrder: Double, start: String, end: String, limit: Int) {
        viewModelScope.launch {
            val newCoupon = Coupon(
                couponId = "cp_" + System.currentTimeMillis(),
                code = code.uppercase(),
                discountType = type,
                discountValue = value,
                minOrderValue = minOrder,
                startDate = start,
                endDate = end,
                usageLimit = limit,
                perCustomerLimit = 1,
                isActive = true
            )
            FirebaseProvider.firestore.collection("coupons").document(newCoupon.couponId)
                .set(newCoupon)
                .addOnSuccessListener { fetchCoupons() }
            _coupons.value = _coupons.value + newCoupon
        }
    }

    fun toggleCouponStatus(couponId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            FirebaseProvider.firestore.collection("coupons").document(couponId)
                .update("isActive", !currentStatus)
                .addOnSuccessListener { fetchCoupons() }
            _coupons.value = _coupons.value.map {
                if (it.couponId == couponId) it.copy(isActive = !currentStatus) else it
            }
        }
    }
}
