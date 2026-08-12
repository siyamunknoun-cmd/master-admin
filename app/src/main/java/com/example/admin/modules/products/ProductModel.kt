package com.example.admin.modules.products

data class ProductVariant(
    val variantId: String = "",
    val name: String = "", // e.g. "1 kg", "5 kg", "1 Litre"
    val sku: String = "",
    val barcode: String = "",
    val price: Double = 0.0,
    val salePrice: Double? = null,
    val stock: Int = 0,
    val unit: String = "kg",
    val weight: Double = 0.0,
    val volume: Double = 0.0
)

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val categoryId: String = "",
    val categoryName: String = "",
    val subcategoryId: String = "",
    val brand: String = "",
    val sku: String = "",
    val barcode: String = "",
    val sellingPrice: Double = 0.0,
    val salePrice: Double? = null,
    val discount: Double = 0.0,
    val unit: String = "pcs",
    val weight: Double = 0.0,
    val volume: Double = 0.0,
    val packSize: String = "",
    val variants: List<ProductVariant> = emptyList(),
    val stock: Int = 0,
    val minimumStock: Int = 5,
    val activeStatus: Boolean = true,
    val featuredStatus: Boolean = false,
    val bestSeller: Boolean = false,
    val newArrival: Boolean = false,
    val flashSale: Boolean = false,
    // Admin-only fields (never exposed to customer website)
    val costPrice: Double = 0.0,
    val profitMargin: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
