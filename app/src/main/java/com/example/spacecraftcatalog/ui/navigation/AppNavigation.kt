// ui/navigation/AppNavigation.kt
package com.example.spacecraftcatalog.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.spacecraftcatalog.ui.screens.AgencyDetailsScreen
import com.example.spacecraftcatalog.ui.screens.AgencyListScreen
import com.example.spacecraftcatalog.ui.screens.SpacecraftDetailsScreen
import com.example.spacecraftcatalog.ui.screens.SpacecraftListScreen

sealed class Screen(val route: String) {
    object AgencyDetails : Screen("agency_details/{agencyId}") {
        fun createRoute(agencyId: Int) = "agency_details/$agencyId"
    }
    object SpacecraftDetails : Screen("spacecraft_details/{spacecraftId}") {
        fun createRoute(spacecraftId: Int) = "spacecraft_details/$spacecraftId"
    }
}

sealed class Tab(val title: String, val route: String) {
    object Agencies : Tab("Agencies", "agency_list")
    object Spacecrafts : Tab("Spacecrafts", "spacecraft_list")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val tabs = listOf(Tab.Agencies, Tab.Spacecrafts)
    var selectedTab by remember { mutableStateOf<Tab>(Tab.Agencies) }

    Column {
        // TabRow for switching between tabs
        TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
            tabs.forEach { tab ->
                Tab(
                    selected = selectedTab == tab, // Compare tabs directly
                    onClick = {
                        selectedTab = tab // Assign the tab directly
                        navController.navigate(tab.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    },
                    text = { Text(tab.title) }
                )
            }
        }

        // Navigation logic for the selected tab
        NavHost(navController = navController, startDestination = Tab.Agencies.route) {
            composable(Tab.Agencies.route) {
                AgencyListScreen(
                    viewModel = hiltViewModel(),
                    onAgencyClick = { agencyId ->
                        navController.navigate(Screen.AgencyDetails.createRoute(agencyId))
                    }
                )
            }
            composable(Tab.Spacecrafts.route) {
                { navController.navigateUp() }.SpacecraftListScreen(
                    viewModel = hiltViewModel(),
                    onSpacecraftClick = { spacecraftId ->
                        navController.navigate(Screen.SpacecraftDetails.createRoute(spacecraftId))
                    }
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
}
