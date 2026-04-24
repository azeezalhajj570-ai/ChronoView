package com.chronoview.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chronoview.ui.screens.CartScreen
import com.chronoview.ui.screens.ProductDetailScreen
import com.chronoview.ui.screens.ProductListScreen
import com.chronoview.viewmodel.ChronoViewViewModel

sealed class Destinations(val route: String) {
    data object Home : Destinations("home")
    data object Cart : Destinations("cart")
    data object Detail : Destinations("detail/{watchId}") {
        fun routeFor(watchId: Int) = "detail/$watchId"
    }
}

@Composable
fun ChronoViewApp(viewModel: ChronoViewViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Home.route,
        modifier = modifier
    ) {
        composable(Destinations.Home.route) {
            ProductListScreen(
                viewModel = viewModel,
                onWatchClick = { navController.navigate(Destinations.Detail.routeFor(it)) },
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate(Destinations.Cart.route) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
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
                onBack = { navController.popBackStack() }
            )
        }
    }
}
