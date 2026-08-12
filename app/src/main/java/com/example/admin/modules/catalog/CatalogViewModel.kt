package com.example.admin.modules.catalog

import androidx.lifecycle.ViewModel
import com.example.admin.modules.categories.Category
import com.example.admin.modules.inventory.InventoryLog
import com.example.admin.modules.inventory.StockMovementReason
import com.example.admin.modules.products.Product
import com.example.admin.modules.products.ProductVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(listOf(
        Category("cat_1", "Rice & Grains", "Premium Basmati rice, super kernel, and daily grains", "", "", 1, true),
        Category("cat_2", "Cooking Oil & Ghee", "Pure cooking oils, banaspati ghee, and olive oil", "", "", 2, true),
        Category("cat_3", "Flour & Sugar", "Wheat flour (Atta), maida, sugar, and salts", "", "", 3, true),
        Category("cat_4", "Pulses & Spices", "Dal lentils, red chillies, turmeric, and garam masala", "", "", 4, true),
        Category("cat_5", "Beverages", "Tea, coffee, juices, and soft drinks", "", "", 5, true),
        Category("cat_6", "Household & Personal Care", "Soaps, shampoos, detergents, and cleaning liquids", "", "", 6, true)
    ))
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(listOf(
        Product(
            id = "prod_1",
            name = "Super Kernel Basmati Rice",
            description = "Extra long grain aged aromatic Basmati rice from Pattoki farms.",
            categoryId = "cat_1",
            categoryName = "Rice & Grains",
            brand = "Master Select",
            sku = "MSC-RICE-001",
            barcode = "8964001234561",
            sellingPrice = 3800.0,
            salePrice = 3500.0,
            discount = 8.0,
            unit = "kg",
            weight = 5.0,
            stock = 45,
            minimumStock = 10,
            activeStatus = true,
            featuredStatus = true,
            bestSeller = true,
            costPrice = 3100.0,
            profitMargin = 700.0,
            variants = listOf(
                ProductVariant("v_1", "1 kg", "MSC-RICE-001-1", "8964001234562", 800.0, 750.0, 15, "kg", 1.0, 0.0),
                ProductVariant("v_2", "5 kg Pack", "MSC-RICE-001-5", "8964001234561", 3800.0, 3500.0, 30, "kg", 5.0, 0.0),
                ProductVariant("v_3", "10 kg Bag", "MSC-RICE-001-10", "8964001234563", 7400.0, 6900.0, 8, "kg", 10.0, 0.0),
                ProductVariant("v_4", "25 kg Sack", "MSC-RICE-001-25", "8964001234564", 18000.0, 17200.0, 4, "kg", 25.0, 0.0)
            )
        ),
        Product(
            id = "prod_2",
            name = "Dalda Banaspati Ghee",
            description = "Rich aroma and superior quality cooking ghee enriched with vitamins.",
            categoryId = "cat_2",
            categoryName = "Cooking Oil & Ghee",
            brand = "Dalda",
            sku = "MSC-OIL-002",
            barcode = "8964001234578",
            sellingPrice = 2450.0,
            salePrice = null,
            discount = 0.0,
            unit = "litre",
            volume = 5.0,
            stock = 3, // Low stock example
            minimumStock = 10,
            activeStatus = true,
            featuredStatus = true,
            bestSeller = true,
            costPrice = 2100.0,
            profitMargin = 350.0,
            variants = listOf(
                ProductVariant("v_oil_1", "1 Litre Pouch", "MSC-OIL-002-1", "8964001234579", 520.0, null, 2, "litre", 0.0, 1.0),
                ProductVariant("v_oil_5", "5 Litre Tin", "MSC-OIL-002-5", "8964001234578", 2450.0, null, 3, "litre", 0.0, 5.0)
            )
        ),
        Product(
            id = "prod_3",
            name = "Fine Chakki Atta (Flour)",
            description = "100% whole wheat stone-ground chakki flour for soft rotis.",
            categoryId = "cat_3",
            categoryName = "Flour & Sugar",
            brand = "Punjab Gold",
            sku = "MSC-FLR-003",
            barcode = "8964001234585",
            sellingPrice = 2850.0,
            salePrice = 2700.0,
            discount = 5.0,
            unit = "kg",
            weight = 10.0,
            stock = 0, // Out of stock example
            minimumStock = 15,
            activeStatus = true,
            featuredStatus = false,
            bestSeller = true,
            costPrice = 2400.0,
            profitMargin = 450.0
        ),
        Product(
            id = "prod_4",
            name = "Habib Cooking Oil",
            description = "Pure canola and soybean cooking oil for healthy hearts.",
            categoryId = "cat_2",
            categoryName = "Cooking Oil & Ghee",
            brand = "Habib",
            sku = "MSC-OIL-004",
            barcode = "8964001234592",
            sellingPrice = 2300.0,
            salePrice = 2200.0,
            discount = 4.0,
            unit = "litre",
            volume = 5.0,
            stock = 55,
            minimumStock = 10,
            activeStatus = true,
            featuredStatus = true,
            costPrice = 1950.0,
            profitMargin = 350.0
        ),
        Product(
            id = "prod_5",
            name = "White Refined Sugar",
            description = "Clean, sparkling white sugar crystals for daily brewing and baking.",
            categoryId = "cat_3",
            categoryName = "Flour & Sugar",
            brand = "Master Select",
            sku = "MSC-SGR-005",
            barcode = "8964001234608",
            sellingPrice = 145.0,
            salePrice = null,
            discount = 0.0,
            unit = "kg",
            weight = 1.0,
            stock = 120,
            minimumStock = 20,
            activeStatus = true,
            costPrice = 130.0,
            profitMargin = 15.0
        )
    ))
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _inventoryLogs = MutableStateFlow<List<InventoryLog>>(listOf(
        InventoryLog(
            logId = "log_1",
            productId = "prod_1",
            productName = "Super Kernel Basmati Rice",
            quantityChange = 25,
            previousStock = 20,
            newStock = 45,
            reason = StockMovementReason.PURCHASE,
            adjustedByEmail = "admin@mastershopping.pk"
        ),
        InventoryLog(
            logId = "log_2",
            productId = "prod_2",
            productName = "Dalda Banaspati Ghee",
            quantityChange = -2,
            previousStock = 5,
            newStock = 3,
            reason = StockMovementReason.SALE,
            adjustedByEmail = "cashier@mastershopping.pk"
        )
    ))
    val inventoryLogs: StateFlow<List<InventoryLog>> = _inventoryLogs.asStateFlow()

    // Filter & Search states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _stockFilter = MutableStateFlow<StockFilter>(StockFilter.ALL)
    val stockFilter: StateFlow<StockFilter> = _stockFilter.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(categoryId: String?) {
        _selectedCategoryFilter.value = categoryId
    }

    fun setStockFilter(filter: StockFilter) {
        _stockFilter.value = filter
    }

    fun addProduct(product: Product) {
        val newProd = if (product.id.isEmpty()) product.copy(id = "prod_${System.currentTimeMillis()}") else product
        _products.value = listOf(newProd) + _products.value
        logStockChange(newProd.id, newProd.name, "", newProd.stock, 0, newProd.stock, StockMovementReason.PURCHASE)
    }

    fun updateProduct(product: Product) {
        val old = _products.value.find { it.id == product.id }
        val diff = if (old != null) product.stock - old.stock else 0
        _products.value = _products.value.map { if (it.id == product.id) product.copy(updatedAt = System.currentTimeMillis()) else it }
        if (old != null && diff != 0) {
            logStockChange(product.id, product.name, "", diff, old.stock, product.stock, StockMovementReason.MANUAL_ADJUSTMENT)
        }
    }

    fun archiveProduct(productId: String) {
        _products.value = _products.value.map { if (it.id == productId) it.copy(activeStatus = !it.activeStatus) else it }
    }

    fun adjustStock(productId: String, variantId: String = "", quantityChange: Int, reason: StockMovementReason, userEmail: String) {
        val currentList = _products.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == productId }
        if (index != -1) {
            val prod = currentList[index]
            val prevStock = prod.stock
            val newStock = (prevStock + quantityChange).coerceAtLeast(0) // Never allow negative stock
            
            val updatedProd = prod.copy(stock = newStock, updatedAt = System.currentTimeMillis())
            currentList[index] = updatedProd
            _products.value = currentList

            val log = InventoryLog(
                logId = "log_${System.currentTimeMillis()}",
                productId = productId,
                variantId = variantId,
                productName = prod.name,
                quantityChange = quantityChange,
                previousStock = prevStock,
                newStock = newStock,
                reason = reason,
                adjustedByEmail = userEmail
            )
            _inventoryLogs.value = listOf(log) + _inventoryLogs.value
        }
    }

    private fun logStockChange(productId: String, productName: String, variantId: String, change: Int, prev: Int, new: Int, reason: StockMovementReason) {
        val log = InventoryLog(
            logId = "log_${System.currentTimeMillis()}",
            productId = productId,
            variantId = variantId,
            productName = productName,
            quantityChange = change,
            previousStock = prev,
            newStock = new,
            reason = reason,
            adjustedByEmail = "admin@mastershopping.pk"
        )
        _inventoryLogs.value = listOf(log) + _inventoryLogs.value
    }

    fun addCategory(category: Category) {
        val newCat = if (category.categoryId.isEmpty()) category.copy(categoryId = "cat_${System.currentTimeMillis()}") else category
        _categories.value = _categories.value + newCat
    }

    fun updateCategory(category: Category) {
        _categories.value = _categories.value.map { if (it.categoryId == category.categoryId) category else it }
    }

    fun toggleCategoryActive(categoryId: String) {
        _categories.value = _categories.value.map { if (it.categoryId == categoryId) it.copy(isActive = !it.isActive) else it }
    }
}

enum class StockFilter {
    ALL, LOW_STOCK, OUT_OF_STOCK
}
