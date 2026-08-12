package com.example.admin.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.firebase.FirebaseProvider
import com.example.admin.model.AdminPermission
import com.example.admin.model.AdminRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminUserState(
    val isAuthenticated: Boolean = false,
    val email: String = "",
    val name: String = "Admin User",
    val role: AdminRole = AdminRole.SUPER_ADMIN,
    val assignedPermissions: Set<AdminPermission> = AdminPermission.values().toSet(),
    val firebaseUid: String = ""
)

class AdminAuthViewModel : ViewModel() {
    private val _userState = MutableStateFlow(AdminUserState())
    val userState: StateFlow<AdminUserState> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = FirebaseProvider.auth.currentUser
        if (currentUser != null) {
            _userState.value = AdminUserState(
                isAuthenticated = true,
                email = currentUser.email ?: "admin@mastershopping.pk",
                name = currentUser.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "Admin",
                role = AdminRole.SUPER_ADMIN,
                assignedPermissions = AdminPermission.values().toSet(),
                firebaseUid = currentUser.uid
            )
        }
    }

    fun updateRole(newRole: AdminRole) {
        val permissions = when (newRole) {
            AdminRole.SUPER_ADMIN -> AdminPermission.values().toSet()
            AdminRole.ADMIN -> setOf(
                AdminPermission.MANAGE_PRODUCTS, AdminPermission.MANAGE_CATEGORIES,
                AdminPermission.MANAGE_INVENTORY, AdminPermission.MANAGE_ORDERS,
                AdminPermission.MANAGE_CUSTOMERS, AdminPermission.MANAGE_DELIVERY,
                AdminPermission.MANAGE_PAYMENTS, AdminPermission.MANAGE_COUPONS,
                AdminPermission.MANAGE_PROMOTIONS, AdminPermission.MANAGE_REVIEWS,
                AdminPermission.MANAGE_RETURNS, AdminPermission.MANAGE_REPLACEMENTS,
                AdminPermission.SEND_NOTIFICATIONS, AdminPermission.VIEW_REPORTS
            )
            AdminRole.STAFF -> setOf(
                AdminPermission.MANAGE_PRODUCTS, AdminPermission.MANAGE_INVENTORY,
                AdminPermission.MANAGE_ORDERS, AdminPermission.MANAGE_RETURNS
            )
            AdminRole.DELIVERY_STAFF -> setOf(
                AdminPermission.MANAGE_DELIVERY
            )
        }
        _userState.value = _userState.value.copy(role = newRole, assignedPermissions = permissions)
    }

    fun hasPermission(permission: AdminPermission): Boolean {
        if (_userState.value.role == AdminRole.SUPER_ADMIN) return true
        return _userState.value.assignedPermissions.contains(permission)
    }

    fun signIn(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please enter both email and password."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                FirebaseProvider.auth.signInWithEmailAndPassword(email.trim(), pass)
                    .addOnSuccessListener { authResult ->
                        _isLoading.value = false
                        val user = authResult.user
                        _userState.value = _userState.value.copy(
                            isAuthenticated = true,
                            email = user?.email ?: email,
                            name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                            firebaseUid = user?.uid ?: ""
                        )
                    }
                    .addOnFailureListener { e ->
                        _isLoading.value = false
                        _errorMessage.value = e.localizedMessage ?: "Authentication failed. Please check credentials."
                    }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred."
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                FirebaseProvider.auth.signOut()
            } catch (e: Exception) {}
            _userState.value = AdminUserState(isAuthenticated = false)
            _errorMessage.value = null
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
