package com.example.admin.modules.promotions.ui

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
import com.example.admin.modules.promotions.PromotionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsManagementScreen(
    promotionsViewModel: PromotionsViewModel,
    onNavigateBack: () -> Unit
) {
    val promotions by promotionsViewModel.promotions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var promoType by remember { mutableStateOf("Banner Slider") }
    var startDate by remember { mutableStateOf("2026-08-01") }
    var endDate by remember { mutableStateOf("2026-08-31") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Store Promotions & Banners", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Promotion")
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
                Icon(Icons.Default.Add, contentDescription = "Add Promotion")
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
                    text = "Shared Backend Promotions & Banners",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Customer e-commerce website automatically syncs active promotions.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(promotions) { promo ->
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
                                    Icon(Icons.Default.Campaign, contentDescription = null, tint = Color(0xFF0056D2))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(promo.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                                    Text(promo.type, fontSize = 11.sp, color = Color(0xFF0056D2), fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Switch(
                                checked = promo.isActive,
                                onCheckedChange = { promotionsViewModel.togglePromotionStatus(promo.promoId, promo.isActive) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0056D2))
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(promo.subtitle, fontSize = 13.sp, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Scheduled: ${promo.startDate} to ${promo.endDate}", fontSize = 11.sp, color = Color(0xFF64748B))
                            Badge(
                                containerColor = if (promo.isActive) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                contentColor = if (promo.isActive) Color(0xFF16A34A) else Color(0xFFDC2626)
                            ) {
                                Text(if (promo.isActive) "Live on Website" else "Inactive", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
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
            title = { Text("Create New Promotion / Banner") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Promotion Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle / Description") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = promoType, onValueChange = { promoType = it }, label = { Text("Type (Banner Slider / Flash Sale)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            promotionsViewModel.addPromotion(title, subtitle, promoType, startDate, endDate)
                            showDialog = false
                            title = ""
                            subtitle = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056D2))
                ) {
                    Text("Save Promotion")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}
