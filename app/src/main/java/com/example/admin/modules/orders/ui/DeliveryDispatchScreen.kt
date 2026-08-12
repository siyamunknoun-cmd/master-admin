package com.example.admin.modules.orders.ui

import androidx.compose.foundation.background
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
import com.example.admin.modules.orders.DeliveryMethod
import com.example.admin.modules.orders.DeliveryStatus
import com.example.admin.modules.orders.Order
import com.example.admin.modules.orders.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryDispatchScreen(
    ordersViewModel: OrdersViewModel,
    onNavigateBack: () -> Unit
) {
    val orders by ordersViewModel.orders.collectAsState()
    val deliveryOrders = orders.filter { it.deliveryMethod == DeliveryMethod.HOME_DELIVERY }

    var selectedOrderForStaff by remember { mutableStateOf<Order?>(null) }
    val primaryBlue = Color(0xFF0056D2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delivery Dispatch (Pattoki)", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Master Shopping Centre Fleet • Assign delivery riders and track status.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(deliveryOrders) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(order.orderId, fontWeight = FontWeight.Bold, color = primaryBlue)
                                val dColor = when (order.deliveryStatus) {
                                    DeliveryStatus.DELIVERED -> Color(0xFF16A34A)
                                    DeliveryStatus.OUT_FOR_DELIVERY -> Color(0xFF2563EB)
                                    DeliveryStatus.FAILED_DELIVERY -> Color(0xFFDC2626)
                                    else -> Color(0xFFD97706)
                                }
                                Badge(containerColor = dColor.copy(alpha = 0.15f)) {
                                    Text(order.deliveryStatus.label, color = dColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("Customer: ${order.customerName} (${order.customerPhone})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Address: ${order.deliveryAddress}", fontSize = 13.sp, color = Color(0xFF475569))
                            if (order.assignedStaffName.isNotBlank()) {
                                Text("Assigned Rider: ${order.assignedStaffName}", fontSize = 12.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                            } else {
                                Text("Assigned Rider: Not Assigned", fontSize = 12.sp, color = Color(0xFFDC2626))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { selectedOrderForStaff = order },
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Assign Staff", fontSize = 12.sp)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(onClick = { ordersViewModel.updateDeliveryStatus(order.orderId, DeliveryStatus.OUT_FOR_DELIVERY) }) {
                                        Icon(Icons.Default.LocalShipping, contentDescription = "Out for delivery", tint = Color(0xFF2563EB))
                                    }
                                    IconButton(onClick = { ordersViewModel.updateDeliveryStatus(order.orderId, DeliveryStatus.DELIVERED) }) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Delivered", tint = Color(0xFF16A34A))
                                    }
                                    IconButton(onClick = { ordersViewModel.updateDeliveryStatus(order.orderId, DeliveryStatus.FAILED_DELIVERY) }) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Failed Delivery", tint = Color(0xFFDC2626))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedOrderForStaff?.let { order ->
        AlertDialog(
            onDismissRequest = { selectedOrderForStaff = null },
            title = { Text("Assign Delivery Staff") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select rider for order ${order.orderId}:")
                    listOf("Rashid Delivery (+92 300 1112233)", "Imran Courier (+92 321 4445566)", "Zeeshan Rider (+92 333 7778899)").forEach { staff ->
                        Button(
                            onClick = {
                                val name = staff.substringBefore(" (")
                                ordersViewModel.assignDeliveryStaff(order.orderId, "staff_${staff.hashCode()}", name)
                                selectedOrderForStaff = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                        ) {
                            Text(staff, color = Color(0xFF1E293B))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedOrderForStaff = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
