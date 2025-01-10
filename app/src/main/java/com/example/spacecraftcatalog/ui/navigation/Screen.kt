// ui/navigation/Screen.kt
package com.example.spacecraftcatalog.ui.navigation

sealed class Screen(val route: String) {
    object AgencyList : Screen("agency_list")
    object SpacecraftList : Screen("spacecraft_list/{agencyId}") {
        fun createRoute(agencyId: Int) = "spacecraft_list/$agencyId"
    }
    object SpacecraftDetails : Screen("spacecraft_details/{spacecraftId}") {
        fun createRoute(spacecraftId: Int) = "spacecraft_details/$spacecraftId"
    }
}