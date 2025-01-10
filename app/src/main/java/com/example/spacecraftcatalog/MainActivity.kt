// ui/MainActivity.kt
package com.example.spacecraftcatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.spacecraftcatalog.ui.navigation.AppNavigation
import com.example.spacecraftcatalog.ui.theme.SpacecraftCatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpacecraftCatalogTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}