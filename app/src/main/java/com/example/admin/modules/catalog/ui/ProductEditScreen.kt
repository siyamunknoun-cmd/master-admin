package com.example.admin.modules.catalog.ui

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
import com.example.admin.modules.catalog.CatalogViewModel
import com.example.admin.modules.products.Product
import com.example.admin.modules.products.ProductVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    catalogViewModel: CatalogViewModel,
    product: Product?,
    onNavigateBack: () -> Unit
) {
    val categories by catalogViewModel.categories.collectAsState()

    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var brand by remember { mutableStateOf(product?.brand ?: "") }
    var sku by remember { mutableStateOf(product?.sku ?: "MSC-${System.currentTimeMillis().toString().takeLast(6)}") }
    var barcode by remember { mutableStateOf(product?.barcode ?: "8964${System.currentTimeMillis().toString().takeLast(9)}") }
    var sellingPrice by remember { mutableStateOf(product?.sellingPrice?.toString() ?: "") }
    var salePrice by remember { mutableStateOf(product?.salePrice?.toString() ?: "") }
    var costPrice by remember { mutableStateOf(product?.costPrice?.toString() ?: "") } // Admin only!
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "50") }
    var minStock by remember { mutableStateOf(product?.minimumStock?.toString() ?: "10") }
    var unit by remember { mutableStateOf(product?.unit ?: "kg") }
    var selectedCategoryId by remember { mutableStateOf(product?.categoryId ?: categories.firstOrNull()?.categoryId ?: "") }
    
    var isBestSeller by remember { mutableStateOf(product?.bestSeller ?: false) }
    var isFeatured by remember { mutableStateOf(product?.featuredStatus ?: false) }

    val primaryBlue = Color(0xFF0056D2)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (product == null) "Create New Product" else "Edit Product", fontWeight = FontWeight.Bold) },
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
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Basic Product Information", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Brand / Manufacturer") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Category Dropdown selection
                    Text("Category", fontSize = 12.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategoryId == cat.categoryId,
                                onClick = { selectedCategoryId = cat.categoryId },
                                label = { Text(cat.name, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("SKU, Barcode & Pricing", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = sku,
                            onValueChange = { sku = it },
                            label = { Text("SKU *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = barcode,
                            onValueChange = { barcode = it },
                            label = { Text("Barcode") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = sellingPrice,
                            onValueChange = { sellingPrice = it },
                            label = { Text("Selling Price (Rs) *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = salePrice,
                            onValueChange = { salePrice = it },
                            label = { Text("Sale Price (Rs)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Admin Only Cost Price
                    OutlinedTextField(
                        value = costPrice,
                        onValueChange = { costPrice = it },
                        label = { Text("Cost Price (Admin Only - Hidden from Website)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("Initial Stock *") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = minStock,
                            onValueChange = { minStock = it },
                            label = { Text("Minimum Alert Stock") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }

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
                    Text("Badges & Visibility", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryBlue)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Best Seller Badge")
                        Switch(checked = isBestSeller, onCheckedChange = { isBestSeller = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Featured Product")
                        Switch(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    }
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && sellingPrice.isNotBlank()) {
                        val sPrice = sellingPrice.toDoubleOrNull() ?: 0.0
                        val cPrice = costPrice.toDoubleOrNull() ?: 0.0
                        val sPriceOpt = salePrice.toDoubleOrNull()
                        val stk = stock.toIntOrNull() ?: 0
                        val minStk = minStock.toIntOrNull() ?: 5
                        val catName = categories.find { it.categoryId == selectedCategoryId }?.name ?: "General"

                        val updated = Product(
                            id = product?.id ?: "",
                            name = name,
                            description = description,
                            brand = brand,
                            sku = sku,
                            barcode = barcode,
                            sellingPrice = sPrice,
                            salePrice = sPriceOpt,
                            costPrice = cPrice,
                            profitMargin = sPrice - cPrice,
                            stock = stk,
                            minimumStock = minStk,
                            categoryId = selectedCategoryId,
                            categoryName = catName,
                            bestSeller = isBestSeller,
                            featuredStatus = isFeatured,
                            variants = product?.variants ?: emptyList()
                        )

                        if (product == null) {
                            catalogViewModel.addProduct(updated)
                        } else {
                            catalogViewModel.updateProduct(updated)
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save & Sync with Customer Website", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
