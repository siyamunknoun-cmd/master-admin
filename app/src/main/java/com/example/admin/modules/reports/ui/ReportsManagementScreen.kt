package com.example.admin.modules.reports.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.admin.modules.orders.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsManagementScreen(
    ordersViewModel: OrdersViewModel,
    onNavigateBack: () -> Unit
) {
    val orders by ordersViewModel.orders.collectAsState()

    val totalRevenue = orders.sumOf { it.totalAmount }
    val totalOrdersCount = orders.size
    val dailySalesEst = totalRevenue * 0.35
    val weeklySalesEst = totalRevenue * 0.80
    val monthlySalesEst = totalRevenue

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial & Sales Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0056D2),
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.BarChart, contentDescription = null, tint = Color(0xFF0056D2))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Sales Analytics Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                                Text("Master Shopping Centre • Pattoki", fontSize = 12.sp, color = Color(0xFF64748B))
                            }
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    ReportMetricRow("Daily Sales (Estimated)", "Rs. ${String.format("%,.2f", dailySalesEst)}")
                    ReportMetricRow("Weekly Sales (Estimated)", "Rs. ${String.format("%,.2f", weeklySalesEst)}")
                    ReportMetricRow("Monthly Revenue", "Rs. ${String.format("%,.2f", monthlySalesEst)}")
                    ReportMetricRow("Total Completed Orders", "$totalOrdersCount orders")
                    ReportMetricRow("Top Selling Category", "Grocery & Staples")
                    ReportMetricRow("Top Selling Product", "Fresh Farm Milk 1L")
                }
            }
        }
    }
}

@Composable
fun ReportMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF475569))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0056D2))
    }
}
