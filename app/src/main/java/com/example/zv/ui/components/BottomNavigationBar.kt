package com.example.zv.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.zv.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    isGuest: Boolean
) {
    val items = listOf(
        BottomNavItem(Screen.Home.route, "Главная", Icons.Default.Home),
        BottomNavItem(Screen.TaskList.route, "Задачи", Icons.Default.List),
        BottomNavItem(Screen.DeviceInfo.route, "Инфо", Icons.Default.Info),
        BottomNavItem(Screen.Settings.route, "Профиль", Icons.Default.Person)
    )
    
    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
