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
import com.example.admin.modules.orders.PaymentStatus
import com.example.admin.modules.orders.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsManagementScreen(
    ordersViewModel: OrdersViewModel,
    onNavigateBack: () -> Unit
) {
    val orders by ordersViewModel.orders.collectAsState()
    val primaryBlue = Color(0xFF0056D2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payments & Bank Transfers", fontWeight = FontWeight.Bold) },
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
                text = "Cash on Delivery & Bank Transfer verification. No fake card payments.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(orders) { order ->
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
                                val pColor = when (order.paymentStatus) {
                                    PaymentStatus.PAID -> Color(0xFF16A34A)
                                    PaymentStatus.REFUNDED -> Color(0xFFD97706)
                                    else -> Color(0xFFDC2626)
                                }
                                Badge(containerColor = pColor.copy(alpha = 0.15f)) {
                                    Text(order.paymentStatus.label, color = pColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("Customer: ${order.customerName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Method: ${order.paymentMethod.label}", fontSize = 13.sp, color = Color(0xFF475569))
                            Text("Amount: Rs. ${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))

                            if (order.refunds.isNotEmpty()) {
                                Text("Refunds Recorded: ${order.refunds.size} (Rs. ${order.refunds.sumOf { it.amount }.toInt()})", fontSize = 12.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (order.paymentStatus != PaymentStatus.PAID) {
                                    Button(
                                        onClick = { ordersViewModel.confirmPayment(order.orderId, PaymentStatus.PAID) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Mark Paid / Verify", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
