package com.example.chatroomapp

import androidx.compose.runtime.Composable

sealed class Screen(val route:String) {
    object LoginScreen:Screen("loginScreen")
    object SignUpScreen:Screen("signUpScreen")
    object ChatRoomScreen:Screen("chatRoomScreen")
    object ChatScreen:Screen("chatScreen")
}

