package com.example.chatroomapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph(navController: NavHostController,authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = Screen.SignUpScreen.route) {
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUpScreen.route)
                }
            ){
                navController.navigate(Screen.ChatRoomScreen.route)
            }
        }
        composable(Screen.ChatRoomScreen.route) {
            ChatRoomListScreen{
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }
    }
}

@Preview
@Composable
fun NavigationGraphPreview() {
    val navController = rememberNavController()
    val authViewModel = AuthViewModel()
    NavigationGraph(navController = navController,authViewModel = authViewModel)
}
