package com.example.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.admin.auth.AdminAuthViewModel
import com.example.admin.auth.ui.AdminLoginScreen
import com.example.admin.modules.orders.Order
import com.example.admin.modules.orders.OrdersViewModel
import com.example.admin.modules.orders.ui.*
import com.example.admin.modules.catalog.CatalogViewModel
import com.example.admin.modules.catalog.ui.CategoriesManagementScreen
import com.example.admin.modules.catalog.ui.InventoryManagementScreen
import com.example.admin.modules.catalog.ui.ProductEditScreen
import com.example.admin.modules.catalog.ui.ProductsManagementScreen
import com.example.admin.modules.products.Product
import com.example.admin.modules.staff.StaffViewModel
import com.example.admin.modules.staff.ui.StaffManagementScreen
import com.example.admin.modules.coupons.CouponsViewModel
import com.example.admin.modules.coupons.ui.CouponsManagementScreen
import com.example.admin.modules.promotions.PromotionsViewModel
import com.example.admin.modules.promotions.ui.PromotionsManagementScreen
import com.example.admin.modules.reviews.ReviewsViewModel
import com.example.admin.modules.reviews.ui.ReviewsManagementScreen
import com.example.admin.modules.notifications.NotificationsViewModel
import com.example.admin.modules.notifications.ui.NotificationsManagementScreen
import com.example.admin.modules.whatsapp.ui.WhatsAppIntegrationScreen
import com.example.admin.modules.reports.ui.ReportsManagementScreen
import com.example.admin.ui.AdminDashboardSkeletonScreen
import com.example.admin.ui.FirebaseTestScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val authViewModel: AdminAuthViewModel = viewModel()
                val catalogViewModel: CatalogViewModel = viewModel()
                val ordersViewModel: OrdersViewModel = viewModel()
                val staffViewModel: StaffViewModel = viewModel()
                val couponsViewModel: CouponsViewModel = viewModel()
                val promotionsViewModel: PromotionsViewModel = viewModel()
                val reviewsViewModel: ReviewsViewModel = viewModel()
                val notificationsViewModel: NotificationsViewModel = viewModel()

                val authState by authViewModel.userState.collectAsState()

                var selectedProductForEdit by remember { mutableStateOf<Product?>(null) }
                var selectedOrderForDetail by remember { mutableStateOf<Order?>(null) }

                // Route protection guard
                LaunchedEffect(authState.isAuthenticated) {
                    if (!authState.isAuthenticated) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = if (authState.isAuthenticated) "dashboard" else "login") {
                    composable("login") {
                        AdminLoginScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("firebase_test") {
                        FirebaseTestScreen(
                            onNavigateBack = { navController.navigate("dashboard") }
                        )
                    }
                    composable("dashboard") {
                        AdminDashboardSkeletonScreen(
                            authViewModel = authViewModel,
                            onNavigateToProducts = { navController.navigate("products") },
                            onNavigateToCategories = { navController.navigate("categories") },
                            onNavigateToInventory = { navController.navigate("inventory") },
                            onNavigateToOrders = { navController.navigate("orders") },
                            onNavigateToCustomers = { navController.navigate("customers") },
                            onNavigateToDelivery = { navController.navigate("delivery") },
                            onNavigateToPayments = { navController.navigate("payments") },
                            onNavigateToCoupons = { navController.navigate("coupons") },
                            onNavigateToPromotions = { navController.navigate("promotions") },
                            onNavigateToReviews = { navController.navigate("reviews") },
                            onNavigateToNotifications = { navController.navigate("notifications") },
                            onNavigateToStaff = { navController.navigate("staff") },
                            onNavigateToWhatsApp = { navController.navigate("whatsapp") },
                            onNavigateToReports = { navController.navigate("reports") }
                        )
                    }
                    composable("products") {
                        ProductsManagementScreen(
                            catalogViewModel = catalogViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onEditProduct = { prod ->
                                selectedProductForEdit = prod
                                navController.navigate("product_edit")
                            }
                        )
                    }
                    composable("categories") {
                        CategoriesManagementScreen(
                            catalogViewModel = catalogViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("inventory") {
                        InventoryManagementScreen(
                            catalogViewModel = catalogViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("product_edit") {
                        ProductEditScreen(
                            catalogViewModel = catalogViewModel,
                            product = selectedProductForEdit,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("orders") {
                        OrdersManagementScreen(
                            ordersViewModel = ordersViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onOpenOrderDetail = { order ->
                                selectedOrderForDetail = order
                                navController.navigate("order_detail")
                            }
                        )
                    }
                    composable("order_detail") {
                        selectedOrderForDetail?.let { order ->
                            OrderDetailScreen(
                                ordersViewModel = ordersViewModel,
                                order = order,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                    composable("customers") {
                        CustomersManagementScreen(
                            ordersViewModel = ordersViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("delivery") {
                        DeliveryDispatchScreen(
                            ordersViewModel = ordersViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("payments") {
                        PaymentsManagementScreen(
                            ordersViewModel = ordersViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("staff") {
                        StaffManagementScreen(
                            staffViewModel = staffViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("coupons") {
                        CouponsManagementScreen(
                            couponsViewModel = couponsViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("promotions") {
                        PromotionsManagementScreen(
                            promotionsViewModel = promotionsViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("reviews") {
                        ReviewsManagementScreen(
                            reviewsViewModel = reviewsViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("notifications") {
                        NotificationsManagementScreen(
                            notificationsViewModel = notificationsViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("whatsapp") {
                        WhatsAppIntegrationScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("reports") {
                        ReportsManagementScreen(
                            ordersViewModel = ordersViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
