package com.gdevs.mubi.presentation.navigation

sealed class AppScreens(val route: String) {
    object SplashScreen: AppScreens("splash")
    object LoginScreen: AppScreens("login")
    object PopularListScreen: AppScreens("popular")
    object DetailScreen: AppScreens("detail")
    object SeasonScreen: AppScreens("season")
    object ProfileScreen: AppScreens("profile")
}