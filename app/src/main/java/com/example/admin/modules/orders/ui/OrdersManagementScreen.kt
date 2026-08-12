package com.example.admin.modules.orders.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.admin.modules.orders.Order
import com.example.admin.modules.orders.OrderStatus
import com.example.admin.modules.orders.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersManagementScreen(
    ordersViewModel: OrdersViewModel,
    onNavigateBack: () -> Unit,
    onOpenOrderDetail: (Order) -> Unit
) {
    val orders by ordersViewModel.orders.collectAsState()
    val searchQuery by ordersViewModel.searchQuery.collectAsState()
    val statusFilter by ordersViewModel.statusFilter.collectAsState()

    val primaryBlue = Color(0xFF0056D2)

    val filteredOrders = orders.filter { ord ->
        val matchesSearch = searchQuery.isBlank() ||
                ord.orderId.contains(searchQuery, ignoreCase = true) ||
                ord.customerName.contains(searchQuery, ignoreCase = true) ||
                ord.customerPhone.contains(searchQuery, ignoreCase = true)

        val matchesStatus = statusFilter == null || ord.orderStatus == statusFilter

        matchesSearch && matchesStatus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Orders (${orders.size})", fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { ordersViewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by Order ID, customer name, phone...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { ordersViewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = statusFilter == null,
                        onClick = { ordersViewModel.setStatusFilter(null) },
                        label = { Text("All Orders") }
                    )
                }
                items(OrderStatus.values()) { status ->
                    FilterChip(
                        selected = statusFilter == status,
                        onClick = { ordersViewModel.setStatusFilter(status) },
                        label = { Text(status.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOpenOrderDetail(order) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderId,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF0056D2)
                )
                val statusColor = when (order.orderStatus) {
                    OrderStatus.PENDING -> Color(0xFFD97706)
                    OrderStatus.CONFIRMED, OrderStatus.PROCESSING -> Color(0xFF2563EB)
                    OrderStatus.COMPLETED -> Color(0xFF16A34A)
                    OrderStatus.CANCELLED -> Color(0xFFDC2626)
                    else -> Color(0xFF64748B)
                }
                Badge(containerColor = statusColor.copy(alpha = 0.15f)) {
                    Text(
                        text = order.orderStatus.label,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = order.customerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "${order.customerPhone} • ${order.deliveryMethod.label}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Rs. ${order.totalAmount.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${order.paymentMethod.label} (${order.paymentStatus.label})",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}
