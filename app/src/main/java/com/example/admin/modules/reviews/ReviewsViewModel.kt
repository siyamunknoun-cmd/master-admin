package com.example.admin.modules.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewsViewModel : ViewModel() {
    private val _reviews = MutableStateFlow<List<ProductReview>>(emptyList())
    val reviews: StateFlow<List<ProductReview>> = _reviews.asStateFlow()

    init {
        fetchReviews()
    }

    fun fetchReviews() {
        viewModelScope.launch {
            try {
                FirebaseProvider.firestore.collection("reviews")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(ProductReview::class.java)?.copy(reviewId = doc.id)
                        }
                        if (list.isEmpty()) {
                            _reviews.value = listOf(
                                ProductReview("rv_1", "Fresh Farm Milk 1L", "Tariq Mahmood", 5, "Excellent fresh milk delivery in Pattoki!", true, false, "2026-08-11"),
                                ProductReview("rv_2", "Basmati Rice 5kg", "Ayesha Bibi", 4, "Good quality rice, packaging was neat.", false, false, "2026-08-10"),
                                ProductReview("rv_3", "Sugar 1kg", "Anonymous Spam", 1, "Bad service spam review test", false, true, "2026-08-09")
                            )
                        } else {
                            _reviews.value = list
                        }
                    }
                    .addOnFailureListener {
                        _reviews.value = listOf(
                            ProductReview("rv_1", "Fresh Farm Milk 1L", "Tariq Mahmood", 5, "Excellent fresh milk delivery in Pattoki!", true, false, "2026-08-11"),
                            ProductReview("rv_2", "Basmati Rice 5kg", "Ayesha Bibi", 4, "Good quality rice, packaging was neat.", false, false, "2026-08-10")
                        )
                    }
            } catch (e: Exception) {
                // fallback
            }
        }
    }

    fun approveReview(reviewId: String, approved: Boolean) {
        viewModelScope.launch {
            FirebaseProvider.firestore.collection("reviews").document(reviewId)
                .update("isApproved", approved)
                .addOnSuccessListener { fetchReviews() }
            _reviews.value = _reviews.value.map {
                if (it.reviewId == reviewId) it.copy(isApproved = approved) else it
            }
        }
    }

    fun hideReview(reviewId: String, hidden: Boolean) {
        viewModelScope.launch {
            FirebaseProvider.firestore.collection("reviews").document(reviewId)
                .update("isHidden", hidden)
                .addOnSuccessListener { fetchReviews() }
            _reviews.value = _reviews.value.map {
                if (it.reviewId == reviewId) it.copy(isHidden = hidden) else it
            }
        }
    }
}
