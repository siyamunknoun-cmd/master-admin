package com.example.admin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.auth.AdminAuthViewModel
import com.example.admin.model.AdminModuleDef
import com.example.admin.model.AdminModules
import com.example.admin.model.AdminRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardSkeletonScreen(
    authViewModel: AdminAuthViewModel,
    onNavigateToProducts: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToCustomers: () -> Unit,
    onNavigateToDelivery: () -> Unit,
    onNavigateToPayments: () -> Unit,
    onNavigateToCoupons: () -> Unit,
    onNavigateToPromotions: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToWhatsApp: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val userState by authViewModel.userState.collectAsState()
    var selectedModule by remember { mutableStateOf<AdminModuleDef?>(null) }
    var showRoleDialog by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF0056D2)
    val lightBlueBg = Color(0xFFF0F4FF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Master Shopping Centre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Admin Portal • Pattoki, Punjab",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showRoleDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Role & Permissions",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Security & Role Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = lightBlueBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security Status",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Role: ${userState.role.displayName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Active Security Rules & RBAC Enforced",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    Button(
                        onClick = { showRoleDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Switch", fontSize = 12.sp)
                    }
                }
            }

            Text(
                text = "Management Modules (18)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Modules Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(AdminModules.list) { module ->
                    val hasAccess = authViewModel.hasPermission(module.requiredPermission)
                    ModuleCard(
                        module = module,
                        hasAccess = hasAccess,
                        onClick = {
                            when (module.key) {
                                "products" -> if (hasAccess) onNavigateToProducts() else { selectedModule = module }
                                "categories" -> if (hasAccess) onNavigateToCategories() else { selectedModule = module }
                                "inventory" -> if (hasAccess) onNavigateToInventory() else { selectedModule = module }
                                "orders" -> if (hasAccess) onNavigateToOrders() else { selectedModule = module }
                                "customers" -> if (hasAccess) onNavigateToCustomers() else { selectedModule = module }
                                "delivery" -> if (hasAccess) onNavigateToDelivery() else { selectedModule = module }
                                "payments" -> if (hasAccess) onNavigateToPayments() else { selectedModule = module }
                                "coupons" -> if (hasAccess) onNavigateToCoupons() else { selectedModule = module }
                                "promotions" -> if (hasAccess) onNavigateToPromotions() else { selectedModule = module }
                                "reviews" -> if (hasAccess) onNavigateToReviews() else { selectedModule = module }
                                "notifications" -> if (hasAccess) onNavigateToNotifications() else { selectedModule = module }
                                "staff" -> if (hasAccess) onNavigateToStaff() else { selectedModule = module }
                                "settings" -> if (hasAccess) onNavigateToWhatsApp() else { selectedModule = module }
                                "reports" -> if (hasAccess) onNavigateToReports() else { selectedModule = module }
                                else -> selectedModule = module
                            }
                        }
                    )
                }
            }
        }
    }

    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            title = { Text("Select Admin Role") },
            text = {
                Column {
                    Text("Choose role to test RBAC and security permissions:", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    AdminRole.values().forEach { role ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    authViewModel.updateRole(role)
                                    showRoleDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = userState.role == role,
                                onClick = {
                                    authViewModel.updateRole(role)
                                    showRoleDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(role.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(role.description, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    selectedModule?.let { module ->
        val hasAccess = authViewModel.hasPermission(module.requiredPermission)
        AlertDialog(
            onDismissRequest = { selectedModule = null },
            title = { Text(module.title) },
            text = {
                Column {
                    Text(module.description)
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (hasAccess) "Access Granted: You have permissions for this module."
                        else "Access Restricted: Your current role (${userState.role.displayName}) does not have permission for this module.",
                        color = if (hasAccess) Color(0xFF16A34A) else Color(0xFFDC2626),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val key = module.key
                    selectedModule = null
                    if (hasAccess) {
                        when (key) {
                            "products" -> onNavigateToProducts()
                            "categories" -> onNavigateToCategories()
                            "inventory" -> onNavigateToInventory()
                            "orders" -> onNavigateToOrders()
                            "customers" -> onNavigateToCustomers()
                            "delivery" -> onNavigateToDelivery()
                            "payments" -> onNavigateToPayments()
                            "coupons" -> onNavigateToCoupons()
                            "promotions" -> onNavigateToPromotions()
                            "reviews" -> onNavigateToReviews()
                            "notifications" -> onNavigateToNotifications()
                            "staff" -> onNavigateToStaff()
                            "settings" -> onNavigateToWhatsApp()
                            "reports" -> onNavigateToReports()
                        }
                    }
                }) {
                    Text(if (hasAccess) "Open Module" else "OK")
                }
            }
        )
    }
}

@Composable
fun ModuleCard(
    module: AdminModuleDef,
    hasAccess: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (hasAccess) Color(0xFFEFF6FF) else Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (module.key) {
                            "dashboard" -> Icons.Default.Dashboard
                            "products" -> Icons.Default.Inventory2
                            "categories" -> Icons.Default.Category
                            "inventory" -> Icons.Default.Warehouse
                            "orders" -> Icons.Default.ShoppingBag
                            "customers" -> Icons.Default.Group
                            "delivery" -> Icons.Default.LocalShipping
                            "payments" -> Icons.Default.Payments
                            "coupons" -> Icons.Default.LocalOffer
                            "promotions" -> Icons.Default.Campaign
                            "reviews" -> Icons.Default.Star
                            "returns" -> Icons.Default.AssignmentReturn
                            "replacements" -> Icons.Default.SwapHoriz
                            "notifications" -> Icons.Default.Notifications
                            "reports" -> Icons.Default.BarChart
                            "staff" -> Icons.Default.Badge
                            "permissions" -> Icons.Default.Security
                            else -> Icons.Default.Settings
                        },
                        contentDescription = module.title,
                        tint = if (hasAccess) Color(0xFF0056D2) else Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (hasAccess) Color(0xFF22C55E) else Color(0xFF94A3B8))
                )
            }
            Column {
                Text(
                    text = module.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = module.description,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }
        }
    }
}
