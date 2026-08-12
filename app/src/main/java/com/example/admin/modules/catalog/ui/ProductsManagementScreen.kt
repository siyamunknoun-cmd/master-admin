package com.example.admin.modules.catalog.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.admin.modules.catalog.CatalogViewModel
import com.example.admin.modules.catalog.StockFilter
import com.example.admin.modules.products.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsManagementScreen(
    catalogViewModel: CatalogViewModel,
    onNavigateBack: () -> Unit,
    onEditProduct: (Product?) -> Unit
) {
    val products by catalogViewModel.products.collectAsState()
    val categories by catalogViewModel.categories.collectAsState()
    val searchQuery by catalogViewModel.searchQuery.collectAsState()
    val selectedCategoryFilter by catalogViewModel.selectedCategoryFilter.collectAsState()
    val stockFilter by catalogViewModel.stockFilter.collectAsState()

    var showBarcodeScannerDialog by remember { mutableStateOf(false) }
    var scannedBarcodeResult by remember { mutableStateOf("") }

    val primaryBlue = Color(0xFF0056D2)

    val filteredProducts = products.filter { p ->
        val matchesSearch = searchQuery.isBlank() ||
                p.name.contains(searchQuery, ignoreCase = true) ||
                p.sku.contains(searchQuery, ignoreCase = true) ||
                p.barcode.contains(searchQuery, ignoreCase = true) ||
                p.brand.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategoryFilter == null || p.categoryId == selectedCategoryFilter

        val matchesStock = when (stockFilter) {
            StockFilter.ALL -> true
            StockFilter.LOW_STOCK -> p.stock in 1..p.minimumStock
            StockFilter.OUT_OF_STOCK -> p.stock == 0
        }

        matchesSearch && matchesCategory && matchesStock
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Catalog (1,000+ Items)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showBarcodeScannerDialog = true }) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditProduct(null) },
                containerColor = primaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Search Bar & Barcode/SKU entry
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { catalogViewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, SKU, barcode, brand...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { catalogViewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stock Filter Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = stockFilter == StockFilter.ALL,
                    onClick = { catalogViewModel.setStockFilter(StockFilter.ALL) },
                    label = { Text("All (${products.size})") }
                )
                FilterChip(
                    selected = stockFilter == StockFilter.LOW_STOCK,
                    onClick = { catalogViewModel.setStockFilter(StockFilter.LOW_STOCK) },
                    label = { Text("Low Stock (${products.count { it.stock in 1..it.minimumStock }})") }
                )
                FilterChip(
                    selected = stockFilter == StockFilter.OUT_OF_STOCK,
                    onClick = { catalogViewModel.setStockFilter(StockFilter.OUT_OF_STOCK) },
                    label = { Text("Out of Stock (${products.count { it.stock == 0 }})") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Horizontal Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryFilter == null,
                        onClick = { catalogViewModel.setCategoryFilter(null) },
                        label = { Text("All Categories") }
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategoryFilter == cat.categoryId,
                        onClick = { catalogViewModel.setCategoryFilter(cat.categoryId) },
                        label = { Text(cat.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Showing ${filteredProducts.size} products (Customer Website Sync Active)",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Product List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductItemCard(
                        product = product,
                        onClick = { onEditProduct(product) },
                        onToggleArchive = { catalogViewModel.archiveProduct(product.id) }
                    )
                }
            }
        }
    }

    if (showBarcodeScannerDialog) {
        AlertDialog(
            onDismissRequest = { showBarcodeScannerDialog = false },
            title = { Text("Barcode Scanner & Lookup") },
            text = {
                Column {
                    Text("Scan product barcode or enter barcode digits to search instantly:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = scannedBarcodeResult,
                        onValueChange = { scannedBarcodeResult = it },
                        placeholder = { Text("e.g. 8964001234561") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Note: Camera scanning utilizes device camera stream or manual barcode lookup matching catalog SKUs.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    catalogViewModel.setSearchQuery(scannedBarcodeResult)
                    showBarcodeScannerDialog = false
                }) {
                    Text("Search Barcode")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBarcodeScannerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    onClick: () -> Unit,
    onToggleArchive: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = Color(0xFF0056D2)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B),
                        maxLines = 1
                    )
                    if (product.bestSeller) {
                        Badge(containerColor = Color(0xFFFEF08A)) { Text("Best Seller", fontSize = 9.sp, color = Color(0xFF854D0E)) }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "SKU: ${product.sku} | ${product.categoryName}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rs. ${product.sellingPrice.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0056D2)
                    )
                    if (product.salePrice != null) {
                        Text(
                            text = "Sale: Rs. ${product.salePrice.toInt()}",
                            fontSize = 12.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    val stockColor = when {
                        product.stock == 0 -> Color(0xFFDC2626)
                        product.stock <= product.minimumStock -> Color(0xFFD97706)
                        else -> Color(0xFF16A34A)
                    }
                    Text(
                        text = "Stock: ${product.stock} ${product.unit}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = stockColor
                    )
                }
            }
            IconButton(onClick = onToggleArchive) {
                Icon(
                    imageVector = if (product.activeStatus) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Active Status",
                    tint = if (product.activeStatus) Color(0xFF16A34A) else Color(0xFFDC2626)
                )
            }
        }
    }
}
