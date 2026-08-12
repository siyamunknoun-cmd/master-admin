package com.example.admin.modules.coupons.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.admin.modules.coupons.CouponsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsManagementScreen(
    couponsViewModel: CouponsViewModel,
    onNavigateBack: () -> Unit
) {
    val coupons by couponsViewModel.coupons.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var code by remember { mutableStateOf("") }
    var discountType by remember { mutableStateOf("percentage") }
    var discountValue by remember { mutableStateOf("") }
    var minOrder by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("2026-08-01") }
    var endDate by remember { mutableStateOf("2026-08-31") }
    var usageLimit by remember { mutableStateOf("100") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coupons & Discount Vouchers", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Coupon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0056D2),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF0056D2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Coupon")
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Trusted Backend Coupon Management",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Validated securely on backend. Never trust unverified client amounts.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(coupons) { coupon ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color(0xFF0056D2))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(coupon.code, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                                    Text(
                                        text = if (coupon.discountType == "percentage") "${coupon.discountValue}% OFF" else "Rs. ${coupon.discountValue} OFF",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF16A34A)
                                    )
                                }
                            }
                            Switch(
                                checked = coupon.isActive,
                                onCheckedChange = { couponsViewModel.toggleCouponStatus(coupon.couponId, coupon.isActive) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0056D2))
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Min Order Value", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("Rs. ${coupon.minOrderValue}", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color(0xFF334155))
                            }
                            Column {
                                Text("Validity", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("${coupon.startDate} to ${coupon.endDate}", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color(0xFF334155))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Usage Limit", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                Text("${coupon.usageLimit} max", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color(0xFF334155))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Create New Coupon") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Coupon Code (e.g. EID2026)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = discountValue, onValueChange = { discountValue = it }, label = { Text("Discount Value (e.g. 15 or 250)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = minOrder, onValueChange = { minOrder = it }, label = { Text("Minimum Order Value (Rs.)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = usageLimit, onValueChange = { usageLimit = it }, label = { Text("Total Usage Limit") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (code.isNotBlank() && discountValue.isNotBlank()) {
                            couponsViewModel.addCoupon(
                                code = code,
                                type = discountType,
                                value = discountValue.toDoubleOrNull() ?: 10.0,
                                minOrder = minOrder.toDoubleOrNull() ?: 500.0,
                                start = startDate,
                                end = endDate,
                                limit = usageLimit.toIntOrNull() ?: 100
                            )
                            showDialog = false
                            code = ""
                            discountValue = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056D2))
                ) {
                    Text("Save Coupon")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
