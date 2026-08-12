package com.example.admin.modules.orders

import androidx.lifecycle.ViewModel
import com.example.admin.modules.customers.Customer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrdersViewModel : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(listOf(
        Order(
            orderId = "MSC-ORD-1001",
            customerId = "cust_1",
            customerName = "Muhammad Ali",
            customerPhone = "+92 300 1234567",
            customerEmail = "muhammad.ali@gmail.com",
            isGuest = false,
            items = listOf(
                OrderItem("prod_1", "Super Kernel Basmati Rice", "5 kg Pack", 2, 3500.0, 7000.0),
                OrderItem("prod_2", "Dalda Banaspati Ghee", "5 Litre Tin", 1, 2450.0, 2450.0)
            ),
            subtotal = 9450.0,
            discount = 200.0,
            deliveryFee = 150.0,
            totalAmount = 9400.0,
            orderStatus = OrderStatus.PENDING,
            deliveryStatus = DeliveryStatus.NOT_ASSIGNED,
            paymentStatus = PaymentStatus.PENDING,
            deliveryMethod = DeliveryMethod.HOME_DELIVERY,
            paymentMethod = PaymentMethod.COD,
            deliveryAddress = "House 45, Main Bazaar, Pattoki",
            deliveryNotes = "Please deliver before 5 PM",
            internalNotes = "First time customer from Pattoki city"
        ),
        Order(
            orderId = "MSC-ORD-1002",
            customerId = "cust_2",
            customerName = "Ayesha Bibi",
            customerPhone = "+92 321 9876543",
            customerEmail = "ayesha.bibi@yahoo.com",
            isGuest = false,
            items = listOf(
                OrderItem("prod_4", "Habib Cooking Oil", "5 Litre", 1, 2200.0, 2200.0),
                OrderItem("prod_5", "White Refined Sugar", "1 kg", 5, 145.0, 725.0)
            ),
            subtotal = 2925.0,
            discount = 50.0,
            deliveryFee = 100.0,
            totalAmount = 2975.0,
            orderStatus = OrderStatus.CONFIRMED,
            deliveryStatus = DeliveryStatus.ASSIGNED,
            paymentStatus = PaymentStatus.PAID,
            deliveryMethod = DeliveryMethod.HOME_DELIVERY,
            paymentMethod = PaymentMethod.BANK_TRANSFER,
            deliveryAddress = "Near Al-Madina Masjid, Allama Iqbal Road, Pattoki",
            assignedStaffId = "staff_1",
            assignedStaffName = "Rashid Delivery",
            internalNotes = "Bank transfer payment verified by Admin"
        ),
        Order(
            orderId = "MSC-ORD-1003",
            customerId = "guest_99",
            customerName = "Usman Ahmed (Guest)",
            customerPhone = "+92 333 5554433",
            customerEmail = "usman.guest@pk.com",
            isGuest = true,
            items = listOf(
                OrderItem("prod_3", "Fine Chakki Atta (Flour)", "10 kg", 1, 2700.0, 2700.0)
            ),
            subtotal = 2700.0,
            discount = 0.0,
            deliveryFee = 0.0,
            totalAmount = 2700.0,
            orderStatus = OrderStatus.PROCESSING,
            deliveryStatus = DeliveryStatus.OUT_FOR_DELIVERY,
            paymentStatus = PaymentStatus.PENDING,
            deliveryMethod = DeliveryMethod.STORE_PICKUP,
            paymentMethod = PaymentMethod.COD,
            deliveryAddress = "Store Pickup - Master Shopping Centre Main Branch Pattoki"
        )
    ))
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _customers = MutableStateFlow<List<Customer>>(listOf(
        Customer("cust_1", "Muhammad Ali", "+92 300 1234567", "muhammad.ali@gmail.com", listOf("House 45, Main Bazaar, Pattoki"), 3, 24500.0, true, false),
        Customer("cust_2", "Ayesha Bibi", "+92 321 9876543", "ayesha.bibi@yahoo.com", listOf("Near Al-Madina Masjid, Allama Iqbal Road, Pattoki"), 5, 41200.0, true, false),
        Customer("guest_99", "Usman Ahmed (Guest)", "+92 333 5554433", "usman.guest@pk.com", emptyList(), 1, 2700.0, true, true)
    ))
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow<OrderStatus?>(null)
    val statusFilter: StateFlow<OrderStatus?> = _statusFilter.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(status: OrderStatus?) {
        _statusFilter.value = status
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) order.copy(orderStatus = newStatus) else order
        }
    }

    fun updateDeliveryStatus(orderId: String, newDeliveryStatus: DeliveryStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) order.copy(deliveryStatus = newDeliveryStatus) else order
        }
    }

    fun assignDeliveryStaff(orderId: String, staffId: String, staffName: String) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) {
                order.copy(
                    assignedStaffId = staffId,
                    assignedStaffName = staffName,
                    deliveryStatus = DeliveryStatus.ASSIGNED
                )
            } else order
        }
    }

    fun confirmPayment(orderId: String, paymentStatus: PaymentStatus) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) order.copy(paymentStatus = paymentStatus) else order
        }
    }

    fun addInternalNote(orderId: String, note: String) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) {
                val updatedNotes = if (order.internalNotes.isEmpty()) note else "${order.internalNotes}\n• $note"
                order.copy(internalNotes = updatedNotes)
            } else order
        }
    }

    fun addRefund(orderId: String, amount: Double, reason: String, method: String) {
        _orders.value = _orders.value.map { order ->
            if (order.orderId == orderId) {
                val refund = RefundRecord(
                    refundId = "ref_${System.currentTimeMillis()}",
                    amount = amount,
                    reason = reason,
                    method = method,
                    status = "Completed"
                )
                order.copy(
                    refunds = order.refunds + refund,
                    paymentStatus = PaymentStatus.REFUNDED
                )
            } else order
        }
    }
}
