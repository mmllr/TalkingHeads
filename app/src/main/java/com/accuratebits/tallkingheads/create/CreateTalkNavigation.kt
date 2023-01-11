package com.accuratebits.tallkingheads.create

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import com.accuratebits.tallkingheads.navigation.AppScreens
import com.accuratebits.tallkingheads.navigation.route

fun NavGraphBuilder.createGraph(navController: NavController) {
    navigation(
        startDestination = AppScreens.CREATE.route,
        route = AppScreens.CREATE.name
    ) {
        createScreen {
            navController.navigateToCreateNewTalk()
        }

        createDialog(navController)
    }
}

internal fun NavGraphBuilder.createScreen(onCreateClick: () -> Unit) {
    composable(AppScreens.CREATE.route) {
        CreateTalkScreen(
            modifier = Modifier.fillMaxSize(),
            onNewClick = onCreateClick
        )
    }
}

internal fun NavGraphBuilder.createDialog(navController: NavController) {
    dialog("${AppScreens.CREATE}/new") {
        CreateTalk(modifier = Modifier.height(IntrinsicSize.Max)) {
            navController.popBackStack()
        }
    }
}

internal fun NavController.navigateToCreateNewTalk() {
    this.navigate("${AppScreens.CREATE}/new")
}