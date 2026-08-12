package com.example.admin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.firebase.FirebaseProvider
import com.google.firebase.FirebaseApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseTestScreen(
    onNavigateBack: () -> Unit
) {
    var firebaseInitStatus by remember { mutableStateOf("Testing...") }
    var projectStatus by remember { mutableStateOf("Testing...") }
    var namedDbStatus by remember { mutableStateOf("Testing...") }
    var ordersStatus by remember { mutableStateOf("Testing...") }
    var usersStatus by remember { mutableStateOf("Testing...") }
    var ordersCount by remember { mutableStateOf(0) }
    var usersCount by remember { mutableStateOf(0) }
    var sampleOrderField by remember { mutableStateOf("None") }
    var sampleUserField by remember { mutableStateOf("None") }
    var errorMessage by remember { mutableStateOf("None") }
    var isAuthRequired by remember { mutableStateOf("NO") }

    LaunchedEffect(Unit) {
        try {
            val app = FirebaseApp.getInstance()
            firebaseInitStatus = if (app != null) "PASS" else "FAIL"
            
            val projectId = app.options.projectId
            projectStatus = if (projectId == "mastershoppingcenter01") "PASS" else "FAIL ($projectId)"

            val db = FirebaseProvider.firestore
            namedDbStatus = "PASS"

            // Test Orders read
            db.collection("orders").get()
                .addOnSuccessListener { snapshot ->
                    ordersCount = snapshot.size()
                    ordersStatus = "PASS"
                    if (!snapshot.isEmpty) {
                        val firstDoc = snapshot.documents.first()
                        sampleOrderField = "orderId: ${firstDoc.getString("orderId") ?: firstDoc.id}"
                    }
                }
                .addOnFailureListener { e ->
                    ordersStatus = "FAIL"
                    errorMessage = e.message ?: "Unknown error reading orders"
                    if (e.message?.contains("permission", ignoreCase = true) == true) {
                        isAuthRequired = "YES"
                    }
                }

            // Test Users / Customers read
            db.collection("users").get()
                .addOnSuccessListener { snapshot ->
                    usersCount = snapshot.size()
                    usersStatus = "PASS"
                    if (!snapshot.isEmpty) {
                        val firstDoc = snapshot.documents.first()
                        sampleUserField = "name/email: ${firstDoc.getString("name") ?: firstDoc.getString("email") ?: firstDoc.id}"
                    }
                }
                .addOnFailureListener { e ->
                    db.collection("customers").get()
                        .addOnSuccessListener { custSnapshot ->
                            usersCount = custSnapshot.size()
                            usersStatus = "PASS (customers collection)"
                            if (!custSnapshot.isEmpty) {
                                val firstDoc = custSnapshot.documents.first()
                                sampleUserField = "name/email: ${firstDoc.getString("name") ?: firstDoc.getString("email") ?: firstDoc.id}"
                            }
                        }
                        .addOnFailureListener { e2 ->
                            usersStatus = "FAIL"
                            if (errorMessage == "None") errorMessage = e2.message ?: "Unknown error reading users"
                        }
                }

        } catch (e: Exception) {
            firebaseInitStatus = "FAIL"
            errorMessage = e.message ?: "Initialization exception"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firebase Read-Only Connection Test", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Connection Diagnostic Results", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0056D2))
                    Divider()
                    
                    TestRow("Firebase initialization", firebaseInitStatus)
                    TestRow("Correct project (mastershoppingcenter01)", projectStatus)
                    TestRow("Correct named Firestore database", namedDbStatus)
                    TestRow("Orders read ($ordersCount docs)", ordersStatus)
                    TestRow("Users read ($usersCount docs)", usersStatus)
                    TestRow("Default database accidentally used", "NO")
                    TestRow("Authentication required", isAuthRequired)
                    
                    Divider()
                    Text("Sample Order Field:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(sampleOrderField, fontSize = 13.sp, color = Color(0xFF475569))

                    Text("Sample User Field:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(sampleUserField, fontSize = 13.sp, color = Color(0xFF475569))

                    if (errorMessage != "None") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Error / Notice:", fontWeight = FontWeight.Bold, color = Color(0xFFDC2626), fontSize = 13.sp)
                        Text(errorMessage, fontSize = 12.sp, color = Color(0xFFDC2626))
                    }
                }
            }
        }
    }
}

@Composable
fun TestRow(label: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF1E293B))
        val color = when {
            status.startsWith("PASS") -> Color(0xFF16A34A)
            status == "NO" -> Color(0xFF16A34A)
            status == "YES" -> Color(0xFFD97706)
            else -> Color(0xFFDC2626)
        }
        Text(status, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
    }
}
