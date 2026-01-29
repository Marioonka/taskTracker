package com.example.zv.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zv.auth.AuthState
import com.example.zv.auth.AuthViewModel
import com.example.zv.ui.components.BottomNavigationBar
import com.example.zv.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object TaskList : Screen("task_list")
    object DeviceInfo : Screen("device_info")
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val appUser by authViewModel.appUser.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomNav = currentRoute != Screen.Login.route && 
                       currentRoute != Screen.Register.route &&
                       authState is AuthState.Authenticated
    
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            val currentRoute = navController.currentDestination?.route
            if (currentRoute == Screen.Login.route || currentRoute == Screen.Register.route) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    isGuest = appUser?.isAnonymous == true
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                var errorMessage by remember { mutableStateOf<String?>(null) }
                LoginScreen(
                    onLoginClick = { email, password ->
                        errorMessage = null
                        authViewModel.signIn(email, password) { error -> errorMessage = error }
                    },
                    onRegisterClick = { navController.navigate(Screen.Register.route) },
                    onGuestClick = {
                        errorMessage = null
                        authViewModel.signInAsGuest { error -> errorMessage = error }
                    },
                    isLoading = authState is AuthState.Loading,
                    errorMessage = errorMessage
                )
            }
            
            composable(Screen.Register.route) {
                var errorMessage by remember { mutableStateOf<String?>(null) }
                RegisterScreen(
                    onRegisterClick = { email, password ->
                        errorMessage = null
                        authViewModel.signUp(email, password) { error -> errorMessage = error }
                    },
                    onBackClick = { navController.popBackStack() },
                    isLoading = authState is AuthState.Loading,
                    errorMessage = errorMessage
                )
            }
            
            composable(Screen.Home.route) {
                HomeScreen(
                    user = currentUser,
                    onTaskListClick = { navController.navigate(Screen.TaskList.route) },
                    onDeviceInfoClick = { navController.navigate(Screen.DeviceInfo.route) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onSignOut = {
                        authViewModel.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.TaskList.route) {
                TaskListScreen()
            }
            
            composable(Screen.DeviceInfo.route) {
                DeviceInfoScreen()
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    user = currentUser,
                    appUser = appUser,
                    onSignOut = {
                        authViewModel.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
