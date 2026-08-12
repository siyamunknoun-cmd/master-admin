package com.example.admin.model

enum class AdminRole(val displayName: String, val description: String) {
    SUPER_ADMIN("Super Admin", "Full unrestrictive access to all modules, cost prices, profit margins, and security settings."),
    ADMIN("Admin", "Access to operational and management modules based on assigned permissions."),
    STAFF("Staff", "Restricted access to assigned daily operations and customer/inventory management."),
    DELIVERY_STAFF("Delivery Staff", "Dedicated access only to assigned delivery orders and delivery status updates.")
}

enum class AdminPermission(val moduleKey: String, val label: String) {
    MANAGE_PRODUCTS("products", "Manage Products & Catalog"),
    MANAGE_CATEGORIES("categories", "Manage Categories"),
    MANAGE_INVENTORY("inventory", "Manage Inventory & Stock Levels"),
    MANAGE_ORDERS("orders", "Manage Customer Orders"),
    MANAGE_CUSTOMERS("customers", "Manage Customer Accounts"),
    MANAGE_DELIVERY("delivery", "Manage Deliveries & Dispatch"),
    MANAGE_PAYMENTS("payments", "Manage Transactions & Payments"),
    MANAGE_COUPONS("coupons", "Manage Coupons & Discounts"),
    MANAGE_PROMOTIONS("promotions", "Manage Store Promotions"),
    MANAGE_REVIEWS("reviews", "Moderate Customer Reviews"),
    MANAGE_RETURNS("returns", "Process Returns"),
    MANAGE_REPLACEMENTS("replacements", "Process Item Replacements"),
    SEND_NOTIFICATIONS("notifications", "Broadcast Push Notifications"),
    VIEW_REPORTS("reports", "View Financial & Sales Reports"),
    MANAGE_STAFF("staff", "Manage Staff Accounts"),
    MANAGE_PERMISSIONS("permissions", "Configure Role Permissions"),
    MANAGE_SETTINGS("settings", "Store & System Settings"),
    VIEW_SENSITIVE_FINANCIALS("financials", "View Cost Price & Profit Margins (Super Admin Only)")
}
