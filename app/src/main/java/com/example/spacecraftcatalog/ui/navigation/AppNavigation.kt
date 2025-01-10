// ui/navigation/AppNavigation.kt

package com.example.spacecraftcatalog.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spacecraftcatalog.ui.screens.AgencyDetailsScreen
import com.example.spacecraftcatalog.ui.screens.AgencyListScreen
import com.example.spacecraftcatalog.ui.screens.SpacecraftDetailsScreen
import com.example.spacecraftcatalog.ui.screens.SpacecraftListScreen

sealed class Screen(val route: String) {
    object AgencyList : Screen("agency_list")
    object SpacecraftList : Screen("spacecraft_list")
    object AgencyDetails : Screen("agency_details/{agencyId}") {
        fun createRoute(agencyId: Int) = "agency_details/$agencyId"
    }
    object SpacecraftDetails : Screen("spacecraft_details/{spacecraftId}") {
        fun createRoute(spacecraftId: Int) = "spacecraft_details/$spacecraftId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.AgencyList.route
    ) {
        composable(Screen.AgencyList.route) {
            AgencyListScreen(
                viewModel = hiltViewModel(),
                onAgencyClick = { agencyId ->
                    navController.navigate(Screen.AgencyDetails.createRoute(agencyId))
                }
            )
        }

        composable(Screen.SpacecraftList.route) {
            SpacecraftListScreen(
                viewModel = hiltViewModel(),
                onSpacecraftClick = { spacecraftId ->
                    navController.navigate(Screen.SpacecraftDetails.createRoute(spacecraftId))
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.AgencyDetails.route,
            arguments = listOf(navArgument("agencyId") { type = NavType.IntType })
        ) {
            AgencyDetailsScreen(
                viewModel = hiltViewModel(),
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.SpacecraftDetails.route,
            arguments = listOf(navArgument("spacecraftId") { type = NavType.IntType })
        ) {
            SpacecraftDetailsScreen(
                viewModel = hiltViewModel(),
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}