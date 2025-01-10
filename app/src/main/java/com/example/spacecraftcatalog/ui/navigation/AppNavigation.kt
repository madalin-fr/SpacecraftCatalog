// ui/navigation/AppNavigation.kt

package com.example.spacecraftcatalog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spacecraftcatalog.ui.screens.AgencyListScreen

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
                    navController.navigate(Screen.SpacecraftList.createRoute(agencyId))
                }
            )
        }

        composable(
            route = Screen.SpacecraftList.route,
            arguments = listOf(
                navArgument("agencyId") {
                    type = NavType.IntType
                }
            )
        ) {
            // Placeholder for SpacecraftListScreen
            // We'll implement this next
        }

        composable(
            route = Screen.SpacecraftDetails.route,
            arguments = listOf(
                navArgument("spacecraftId") {
                    type = NavType.IntType
                }
            )
        ) {
            // Placeholder for SpacecraftDetailsScreen
            // We'll implement this later
        }
    }
}