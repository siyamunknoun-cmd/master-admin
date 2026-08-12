package com.example.admin.modules.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.modules.catalog.CatalogViewModel
import com.example.admin.modules.inventory.StockMovementReason
import com.example.admin.modules.products.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryManagementScreen(
    catalogViewModel: CatalogViewModel,
    onNavigateBack: () -> Unit
) {
    val products by catalogViewModel.products.collectAsState()
    val inventoryLogs by catalogViewModel.inventoryLogs.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Stock Alerts, 1 = Stock Movements Audit Log
    var showAdjustDialog by remember { mutableStateOf(false) }
    var selectedProductForAdjustment by remember { mutableStateOf<Product?>(null) }
    var adjustmentQty by remember { mutableStateOf("") }
    var selectedReason by remember { mutableStateOf(StockMovementReason.MANUAL_ADJUSTMENT) }

    val primaryBlue = Color(0xFF0056D2)

    val lowStockProducts = products.filter { it.stock in 1..it.minimumStock }
    val outOfStockProducts = products.filter { it.stock == 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory & Stock Management", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Stock Alerts (${lowStockProducts.size + outOfStockProducts.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Audit Movements (${inventoryLogs.size})") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                // Stock Alerts tab
                Text(
                    text = "Products requiring inventory attention:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val attentionProducts = (outOfStockProducts + lowStockProducts).distinctBy { it.id }
                    if (attentionProducts.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("All stock levels are healthy!", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                }
                            }
                        }
                    } else {
                        items(attentionProducts) { product ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("SKU: ${product.sku}", fontSize = 12.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val isOut = product.stock == 0
                                        Text(
                                            text = if (isOut) "OUT OF STOCK" else "Low Stock: ${product.stock} (Min: ${product.minimumStock})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isOut) Color(0xFFDC2626) else Color(0xFFD97706)
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            selectedProductForAdjustment = product
                                            adjustmentQty = ""
                                            showAdjustDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                                    ) {
                                        Text("Adjust Stock", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Stock Movements Audit Log
                Text(
                    text = "Transactional stock adjustments & movement history:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(inventoryLogs) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(log.productName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    val isPositive = log.quantityChange >= 0
                                    Text(
                                        text = "${if (isPositive) "+" else ""}${log.quantityChange}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isPositive) Color(0xFF16A34A) else Color(0xFFDC2626)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Reason: ${log.reason.label} | By: ${log.adjustedByEmail}", fontSize = 11.sp, color = Color.Gray)
                                    Text("Stock: ${log.previousStock} → ${log.newStock}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0056D2))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAdjustDialog && selectedProductForAdjustment != null) {
        val prod = selectedProductForAdjustment!!
        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            title = { Text("Adjust Stock: ${prod.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Current Stock: ${prod.stock} ${prod.unit}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = adjustmentQty,
                        onValueChange = { adjustmentQty = it },
                        label = { Text("Quantity Change (e.g. +10 or -5)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Select Reason:", fontSize = 12.sp, color = Color.Gray)
                    StockMovementReason.values().forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedReason == reason,
                                onClick = { selectedReason = reason }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(reason.label, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val change = adjustmentQty.toIntOrNull() ?: 0
                    if (change != 0) {
                        catalogViewModel.adjustStock(
                            productId = prod.id,
                            quantityChange = change,
                            reason = selectedReason,
                            userEmail = "admin@mastershopping.pk"
                        )
                        showAdjustDialog = false
                    }
                }) {
                    Text("Apply Adjustment")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdjustDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
