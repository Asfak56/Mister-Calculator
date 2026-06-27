package com.example.mistercalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mistercalculator.ui.screens.ConverterScreen
import com.example.mistercalculator.ui.screens.HistoryScreen
import com.example.mistercalculator.ui.screens.HomeScreen
import com.example.mistercalculator.ui.screens.SettingScreen
import com.example.mistercalculator.viewmodel.CalculatorViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    calculatorViewModel: CalculatorViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN,
    ) {
        composable(Routes.HOME_SCREEN) {
            HomeScreen(viewModel = calculatorViewModel)
        }

        composable(Routes.SETTING_SCREEN) {
            SettingScreen(viewModel = calculatorViewModel)
        }

        composable(Routes.HISTORY_SCREEN) {
            HistoryScreen(
                viewModel = calculatorViewModel,
                navController = navController
            )
        }

        composable(Routes.CONVERTER_SCREEN) {
            ConverterScreen()
        }
    }
}