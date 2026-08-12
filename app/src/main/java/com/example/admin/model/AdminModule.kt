package com.example.admin.model

data class AdminModuleDef(
    val key: String,
    val title: String,
    val description: String,
    val requiredPermission: AdminPermission,
    val iconName: String
)

object AdminModules {
    val list = listOf(
        AdminModuleDef("dashboard", "Dashboard", "Store overview, real-time metrics, and quick actions", AdminPermission.VIEW_REPORTS, "dashboard"),
        AdminModuleDef("products", "Products", "1,000+ grocery & general items catalog management", AdminPermission.MANAGE_PRODUCTS, "inventory_2"),
        AdminModuleDef("categories", "Categories", "Organize store categories and sub-departments", AdminPermission.MANAGE_CATEGORIES, "category"),
        AdminModuleDef("inventory", "Inventory", "Stock tracking, low stock alerts, and warehouse updates", AdminPermission.MANAGE_INVENTORY, "warehouse"),
        AdminModuleDef("orders", "Orders", "Customer order processing, fulfillment, and packing", AdminPermission.MANAGE_ORDERS, "shopping_bag"),
        AdminModuleDef("customers", "Customers", "Customer profiles, purchase history, and details", AdminPermission.MANAGE_CUSTOMERS, "group"),
        AdminModuleDef("delivery", "Delivery", "Dispatch, Pattoki delivery zones, and delivery staff", AdminPermission.MANAGE_DELIVERY, "local_shipping"),
        AdminModuleDef("payments", "Payments", "COD, online transactions, and payment reconciliation", AdminPermission.MANAGE_PAYMENTS, "payments"),
        AdminModuleDef("coupons", "Coupons", "Discount codes, vouchers, and validity rules", AdminPermission.MANAGE_COUPONS, "local_offer"),
        AdminModuleDef("promotions", "Promotions", "Store-wide deals, banners, and featured specials", AdminPermission.MANAGE_PROMOTIONS, "campaign"),
        AdminModuleDef("reviews", "Reviews", "Customer product ratings and feedback moderation", AdminPermission.MANAGE_REVIEWS, "star"),
        AdminModuleDef("returns", "Returns", "Return requests, inspection status, and approvals", AdminPermission.MANAGE_RETURNS, "assignment_return"),
        AdminModuleDef("replacements", "Replacements", "Damaged or incorrect item replacement workflow", AdminPermission.MANAGE_REPLACEMENTS, "swap_horiz"),
        AdminModuleDef("notifications", "Notifications", "FCM push notifications and customer alerts", AdminPermission.SEND_NOTIFICATIONS, "notifications"),
        AdminModuleDef("reports", "Reports", "Sales analytics, revenue, and profit reports", AdminPermission.VIEW_REPORTS, "bar_chart"),
        AdminModuleDef("staff", "Staff", "Admin & delivery staff accounts and activity tracking", AdminPermission.MANAGE_STAFF, "badge"),
        AdminModuleDef("permissions", "Permissions", "Role-based access control (RBAC) configuration", AdminPermission.MANAGE_PERMISSIONS, "security"),
        AdminModuleDef("settings", "Settings", "Store hours, tax rules, pickup/delivery settings", AdminPermission.MANAGE_SETTINGS, "settings")
    )
}
