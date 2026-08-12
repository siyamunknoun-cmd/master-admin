package com.example.admin.modules.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffViewModel : ViewModel() {
    private val _staffList = MutableStateFlow<List<StaffMember>>(emptyList())
    val staffList: StateFlow<List<StaffMember>> = _staffList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchStaff()
    }

    fun fetchStaff() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                FirebaseProvider.firestore.collection("staff")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val list = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(StaffMember::class.java)?.copy(staffId = doc.id)
                        }
                        if (list.isEmpty()) {
                            // Provide default initial mock/live staff if empty
                            _staffList.value = listOf(
                                StaffMember("st_1", "Muhammad Ali", "ali@mastershopping.pk", "Super Admin", true, listOf("all"), "+92 300 1234567", "Pattoki Head Office"),
                                StaffMember("st_2", "Usman Khan", "usman@mastershopping.pk", "Admin", true, listOf("products", "orders", "inventory"), "+92 301 9876543", "Pattoki Warehouse"),
                                StaffMember("st_3", "Bilal Ahmed", "bilal@mastershopping.pk", "Delivery Staff", true, listOf("delivery"), "+92 302 5551122", "Pattoki North Zone")
                            )
                        } else {
                            _staffList.value = list
                        }
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        // Fallback sample data if collection doesn't exist yet
                        _staffList.value = listOf(
                            StaffMember("st_1", "Muhammad Ali", "ali@mastershopping.pk", "Super Admin", true, listOf("all"), "+92 300 1234567", "Pattoki Head Office"),
                            StaffMember("st_2", "Usman Khan", "usman@mastershopping.pk", "Admin", true, listOf("products", "orders", "inventory"), "+92 301 9876543", "Pattoki Warehouse")
                        )
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun toggleStaffStatus(staffId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            FirebaseProvider.firestore.collection("staff").document(staffId)
                .update("isActive", !currentStatus)
                .addOnSuccessListener {
                    fetchStaff()
                }
            // Also update locally
            _staffList.value = _staffList.value.map {
                if (it.staffId == staffId) it.copy(isActive = !currentStatus) else it
            }
        }
    }

    fun addStaff(name: String, email: String, role: String, phone: String, zone: String) {
        viewModelScope.launch {
            val newStaff = StaffMember(
                staffId = "st_" + System.currentTimeMillis(),
                name = name,
                email = email,
                role = role,
                isActive = true,
                permissions = if (role == "Super Admin") listOf("all") else listOf("orders", "inventory"),
                phone = phone,
                assignedZone = zone
            )
            FirebaseProvider.firestore.collection("staff").document(newStaff.staffId)
                .set(newStaff)
                .addOnSuccessListener {
                    fetchStaff()
                }
            _staffList.value = _staffList.value + newStaff
        }
    }
}
