package com.accuratebits.tallkingheads.talks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.accuratebits.tallkingheads.navigation.AppScreens
import com.accuratebits.tallkingheads.navigation.route


fun NavGraphBuilder.talksGraph(navController: NavController) {
    navigation(
        startDestination = AppScreens.TALKS.route,
        route = AppScreens.TALKS.name
    ) {
        talksScreen()
    }
}


internal fun NavGraphBuilder.talksScreen(
) {
    composable(AppScreens.TALKS.route) {
        TalksScreen(modifier = Modifier.fillMaxSize())
    }
}
