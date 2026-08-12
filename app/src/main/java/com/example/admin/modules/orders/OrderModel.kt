package com.example.admin.modules.orders

enum class OrderStatus(val label: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    RETURN_REQUESTED("Return Requested"),
    RETURNED("Returned"),
    REPLACEMENT_REQUESTED("Replacement Requested"),
    REPLACED("Replaced")
}

enum class DeliveryStatus(val label: String) {
    NOT_ASSIGNED("Not Assigned"),
    ASSIGNED("Assigned"),
    PREPARING("Preparing"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    FAILED_DELIVERY("Failed Delivery")
}

enum class PaymentStatus(val label: String) {
    PENDING("Pending"),
    PAID("Paid"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    PARTIALLY_REFUNDED("Partially Refunded")
}

enum class DeliveryMethod(val label: String) {
    HOME_DELIVERY("Home Delivery (Pattoki)"),
    STORE_PICKUP("Store Pickup")
}

enum class PaymentMethod(val label: String) {
    COD("Cash on Delivery"),
    BANK_TRANSFER("Bank Transfer")
}

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val variantName: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0
)

data class RefundRecord(
    val refundId: String = "",
    val amount: Double = 0.0,
    val reason: String = "",
    val method: String = "Cash/Bank",
    val status: String = "Completed",
    val notes: String = "",
    val recordedBy: String = "admin@mastershopping.pk",
    val timestamp: Long = System.currentTimeMillis()
)

data class Order(
    val orderId: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val customerEmail: String = "",
    val isGuest: Boolean = false,
    val items: List<OrderItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val orderStatus: OrderStatus = OrderStatus.PENDING,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.NOT_ASSIGNED,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val deliveryMethod: DeliveryMethod = DeliveryMethod.HOME_DELIVERY,
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    val deliveryAddress: String = "",
    val deliveryNotes: String = "",
    val assignedStaffId: String = "",
    val assignedStaffName: String = "",
    val internalNotes: String = "",
    val returnReason: String = "",
    val replacementReason: String = "",
    val refunds: List<RefundRecord> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
