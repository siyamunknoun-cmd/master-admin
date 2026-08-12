package com.example.admin.modules.orders.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.admin.modules.orders.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    ordersViewModel: OrdersViewModel,
    order: Order,
    onNavigateBack: () -> Unit
) {
    var currentOrder by remember { mutableStateOf(order) }
    val allOrders by ordersViewModel.orders.collectAsState()
    
    // Keep in sync with VM
    LaunchedEffect(allOrders) {
        allOrders.find { it.orderId == order.orderId }?.let {
            currentOrder = it
        }
    }

    var newNote by remember { mutableStateOf("") }
    var showRefundDialog by remember { mutableStateOf(false) }
    var refundAmount by remember { mutableStateOf("") }
    var refundReason by remember { mutableStateOf("") }

    val primaryBlue = Color(0xFF0056D2)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details: ${currentOrder.orderId}", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Customer & Delivery Info Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Customer & Delivery Details", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)
                    Text("Customer: ${currentOrder.customerName} ${if (currentOrder.isGuest) "(Guest)" else ""}", fontWeight = FontWeight.Bold)
                    Text("Phone: ${currentOrder.customerPhone}")
                    Text("Email: ${currentOrder.customerEmail}")
                    Divider()
                    Text("Delivery Method: ${currentOrder.deliveryMethod.label}", fontWeight = FontWeight.Bold)
                    Text("Address: ${currentOrder.deliveryAddress}")
                    if (currentOrder.deliveryNotes.isNotBlank()) {
                        Text("Delivery Notes: ${currentOrder.deliveryNotes}", color = Color(0xFFD97706), fontSize = 12.sp)
                    }
                    if (currentOrder.assignedStaffName.isNotBlank()) {
                        Text("Assigned Delivery Staff: ${currentOrder.assignedStaffName}", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Order Items Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Order Items (${currentOrder.items.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)
                    currentOrder.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.productName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Variant: ${item.variantName} | Qty: ${item.quantity} × Rs. ${item.unitPrice.toInt()}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Text("Rs. ${item.totalPrice.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Divider(color = Color(0xFFF1F5F9))
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 13.sp)
                        Text("Rs. ${currentOrder.subtotal.toInt()}", fontSize = 13.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount", fontSize = 13.sp, color = Color(0xFF16A34A))
                        Text("- Rs. ${currentOrder.discount.toInt()}", fontSize = 13.sp, color = Color(0xFF16A34A))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee", fontSize = 13.sp)
                        Text("Rs. ${currentOrder.deliveryFee.toInt()}", fontSize = 13.sp)
                    }
                    Divider()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryBlue)
                        Text("Rs. ${currentOrder.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryBlue)
                    }
                }
            }

            // Status Management Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Workflow Status Control", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)

                    Text("Order Status:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.PROCESSING, OrderStatus.COMPLETED, OrderStatus.CANCELLED).forEach { status ->
                            FilterChip(
                                selected = currentOrder.orderStatus == status,
                                onClick = { ordersViewModel.updateOrderStatus(currentOrder.orderId, status) },
                                label = { Text(status.label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Text("Delivery Status:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(DeliveryStatus.NOT_ASSIGNED, DeliveryStatus.ASSIGNED, DeliveryStatus.OUT_FOR_DELIVERY, DeliveryStatus.DELIVERED, DeliveryStatus.FAILED_DELIVERY).forEach { dStatus ->
                            FilterChip(
                                selected = currentOrder.deliveryStatus == dStatus,
                                onClick = { ordersViewModel.updateDeliveryStatus(currentOrder.orderId, dStatus) },
                                label = { Text(dStatus.label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Text("Payment Status (${currentOrder.paymentMethod.label}):", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(PaymentStatus.PENDING, PaymentStatus.PAID, PaymentStatus.REFUNDED).forEach { pStatus ->
                            FilterChip(
                                selected = currentOrder.paymentStatus == pStatus,
                                onClick = { ordersViewModel.confirmPayment(currentOrder.orderId, pStatus) },
                                label = { Text(pStatus.label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Button(
                        onClick = { showRefundDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Record Refund / Return")
                    }
                }
            }

            // Internal Notes Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Internal Admin Notes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)
                    Text(
                        text = currentOrder.internalNotes.ifBlank { "No internal notes recorded yet." },
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newNote,
                        onValueChange = { newNote = it },
                        label = { Text("Add internal note...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (newNote.isNotBlank()) {
                                ordersViewModel.addInternalNote(currentOrder.orderId, newNote)
                                newNote = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        Text("Save Note")
                    }
                }
            }
        }
    }

    if (showRefundDialog) {
        AlertDialog(
            onDismissRequest = { showRefundDialog = false },
            title = { Text("Record Refund") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = refundAmount,
                        onValueChange = { refundAmount = it },
                        label = { Text("Refund Amount (Rs)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = refundReason,
                        onValueChange = { refundReason = it },
                        label = { Text("Reason (e.g. Damaged item)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val amt = refundAmount.toDoubleOrNull() ?: 0.0
                    if (amt > 0) {
                        ordersViewModel.addRefund(currentOrder.orderId, amt, refundReason, currentOrder.paymentMethod.label)
                        showRefundDialog = false
                    }
                }) {
                    Text("Confirm Refund")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRefundDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
