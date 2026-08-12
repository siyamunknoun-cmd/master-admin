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
import com.example.admin.modules.orders.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersManagementScreen(
    ordersViewModel: OrdersViewModel,
    onNavigateBack: () -> Unit
) {
    val customers by ordersViewModel.customers.collectAsState()
    val primaryBlue = Color(0xFF0056D2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Accounts & Guests (${customers.size})", fontWeight = FontWeight.Bold) },
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
            Text(
                text = "Registered customers and guest checkouts from Pattoki and surroundings.",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(customers) { customer ->
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
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = customer.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E293B)
                                )
                                if (customer.isGuest) {
                                    Badge(containerColor = Color(0xFFFEF08A)) { Text("Guest Order", fontSize = 10.sp, color = Color(0xFF854D0E)) }
                                } else {
                                    Badge(containerColor = Color(0xFFDCFCE7)) { Text("Registered", fontSize = 10.sp, color = Color(0xFF166534)) }
                                }
                            }
                            Text("Phone: ${customer.phone}", fontSize = 13.sp, color = Color(0xFF475569))
                            Text("Email: ${customer.email}", fontSize = 13.sp, color = Color(0xFF475569))
                            if (customer.savedAddresses.isNotEmpty()) {
                                Text("Saved Address: ${customer.savedAddresses.first()}", fontSize = 12.sp, color = Color(0xFF64748B))
                            }
                            Divider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Orders: ${customer.totalOrdersCount}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Total Spent: Rs. ${customer.totalSpent.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                        }
                    }
                }
            }
        }
    }
}
