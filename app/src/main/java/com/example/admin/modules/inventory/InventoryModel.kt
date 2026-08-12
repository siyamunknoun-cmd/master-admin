package com.example.admin.modules.inventory

enum class StockMovementReason(val label: String) {
    PURCHASE("Purchase"),
    SALE("Sale"),
    DAMAGED("Damaged"),
    EXPIRED("Expired"),
    LOST("Lost"),
    MANUAL_ADJUSTMENT("Manual Adjustment"),
    RETURN("Return")
}

data class InventoryLog(
    val logId: String = "",
    val productId: String = "",
    val variantId: String = "",
    val productName: String = "",
    val quantityChange: Int = 0,
    val previousStock: Int = 0,
    val newStock: Int = 0,
    val reason: StockMovementReason = StockMovementReason.MANUAL_ADJUSTMENT,
    val adjustedByUserId: String = "",
    val adjustedByEmail: String = "admin@mastershopping.pk",
    val timestamp: Long = System.currentTimeMillis()
)
