package com.example.admin.modules.reviews.ui

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
import com.example.admin.modules.reviews.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsManagementScreen(
    reviewsViewModel: ReviewsViewModel,
    onNavigateBack: () -> Unit
) {
    val reviews by reviewsViewModel.reviews.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Reviews & Moderation", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Product Review Moderation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Approve verified purchase reviews or hide inappropriate content.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(reviews) { review ->
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
                                        .background(Color(0xFFFEF08A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFCA8A04))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(review.productName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                                    Text("By ${review.customerName} • ${review.date}", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${review.rating}/5", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFCA8A04))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("\"${review.comment}\"", fontSize = 13.sp, color = Color(0xFF334155))
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { reviewsViewModel.approveReview(review.reviewId, !review.isApproved) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (review.isApproved) Color(0xFF22C55E) else Color(0xFF0056D2)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(if (review.isApproved) "Approved" else "Approve", fontSize = 12.sp)
                                }
                                OutlinedButton(
                                    onClick = { reviewsViewModel.hideReview(review.reviewId, !review.isHidden) },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(if (review.isHidden) "Unhide" else "Hide", fontSize = 12.sp)
                                }
                            }
                            Badge(
                                containerColor = if (review.isApproved) Color(0xFFDCFCE7) else Color(0xFFFEF9C3),
                                contentColor = if (review.isApproved) Color(0xFF16A34A) else Color(0xFFCA8A04)
                            ) {
                                Text(if (review.isApproved) "Published" else "Pending", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
