package com.accuratebits.tallkingheads

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.accuratebits.tallkingheads.navigation.*
import com.accuratebits.tallkingheads.ui.theme.TallkingHeadsTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TalkingHeadsApp : Application()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        AppScreens.values().find { currentDestination?.route?.startsWith(it.name) ?: false }
            ?: AppScreens.CREATE
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentScreen.title,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        bottomBar = {
            TalkingHeadsBottomNavigation(
                allScreens = AppScreens.values().toList(),
                onTabSelected = {
                    navController.navigateSingleTopTo(it.route)
                },
                currentScreen = currentScreen,
                Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        TalkingHeadsNavHost(
            navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TallkingHeadsTheme {
        App()
    }
}