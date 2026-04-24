package com.chronoview.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chronoview.ui.screens.*
import com.chronoview.viewmodel.ChronoViewViewModel
import kotlinx.coroutines.launch

sealed class Destinations(val route: String) {
    data object Home : Destinations("home")
    data object Cart : Destinations("cart")
    data object Detail : Destinations("detail/{watchId}") {
        fun routeFor(watchId: Int) = "detail/$watchId"
    }
    data object Login : Destinations("login")
    data object SignUp : Destinations("signup")
    data object History : Destinations("history")
    data object Admin : Destinations("admin")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronoViewApp(viewModel: ChronoViewViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearMessage()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute != Destinations.Login.route && currentRoute != Destinations.SignUp.route) {
                CenterAlignedTopAppBar(
                    title = { Text("ChronoView", style = MaterialTheme.typography.titleLarge) },
                    actions = {
                        if (isLoggedIn) {
                            if (isAdmin) {
                                IconButton(onClick = { navController.navigate(Destinations.Admin.route) }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Admin")
                                }
                            }
                            IconButton(onClick = { navController.navigate(Destinations.History.route) }) {
                                Icon(Icons.Default.History, contentDescription = "History")
                            }
                            IconButton(onClick = { 
                                viewModel.logout()
                                navController.navigate(Destinations.Login.route) {
                                    popUpTo(0)
                                } 
                            }) {
                                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                            }
                        } else {
                            IconButton(onClick = { navController.navigate(Destinations.Login.route) }) {
                                Icon(Icons.Default.Person, contentDescription = "Login")
                            }
                        }
                        IconButton(onClick = { navController.navigate(Destinations.Cart.route) }) {
                            val cartItems by viewModel.cartItems.collectAsState()
                            BadgedBox(badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge { Text(cartItems.sumOf { it.quantity }.toString()) }
                                }
                            }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Destinations.Home.route else Destinations.Login.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destinations.Login.route) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { navController.navigate(Destinations.Home.route) { popUpTo(Destinations.Login.route) { inclusive = true } } },
                    onNavigateToSignUp = { navController.navigate(Destinations.SignUp.route) }
                )
            }
            composable(Destinations.SignUp.route) {
                SignUpScreen(
                    viewModel = viewModel,
                    onSignUpSuccess = { navController.navigate(Destinations.Home.route) { popUpTo(Destinations.SignUp.route) { inclusive = true } } },
                    onNavigateToLogin = { navController.navigate(Destinations.Login.route) }
                )
            }
            composable(Destinations.Home.route) {
                ProductListScreen(
                    viewModel = viewModel,
                    onWatchClick = { navController.navigate(Destinations.Detail.routeFor(it)) }
                )
            }
            composable(
                route = Destinations.Detail.route,
                arguments = listOf(navArgument("watchId") { type = NavType.IntType })
            ) { backStackEntry ->
                val watchId = backStackEntry.arguments?.getInt("watchId") ?: return@composable
                ProductDetailScreen(
                    viewModel = viewModel,
                    watchId = watchId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Destinations.Cart.route) {
                CartScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToHistory = { navController.navigate(Destinations.History.route) }
                )
            }
            composable(Destinations.History.route) {
                PurchaseHistoryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Destinations.Admin.route) {
                AdminDashboardScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
