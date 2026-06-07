package com.example.mistercalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mistercalculator.data.DatabaseProvider
import com.example.mistercalculator.data.HistoryRepository
import com.example.mistercalculator.data.preferences.ThemePreferences
import com.example.mistercalculator.navigation.NavGraph
import com.example.mistercalculator.navigation.Routes
import com.example.mistercalculator.speech.SpeechRecognizerManager
import com.example.mistercalculator.ui.components.CalculatorBottomBar
import com.example.mistercalculator.ui.components.CalculatorTopBar
import com.example.mistercalculator.ui.components.VoiceButton
import com.example.mistercalculator.ui.theme.VoiceCalcTheme
import com.example.mistercalculator.viewmodel.CalculatorViewModel
import com.example.mistercalculator.viewmodel.CalculatorViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoiceCalculator()
        }
    }
}

@Composable
fun VoiceCalculator() {
    val context = LocalContext.current
    val themePreferences = remember {
        ThemePreferences(context)
    }
    val dao = DatabaseProvider
        .getDatabase(context)
        .historyDao()
    val repository = HistoryRepository(dao)
    val factory = CalculatorViewModelFactory(
        repository,
        themePreferences
    )
    val calculatorViewModel: CalculatorViewModel = viewModel(factory = factory)

    val navController = rememberNavController()
    val speechRecognizerManager = remember {
        SpeechRecognizerManager(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechRecognizerManager.startListening()
        }
    }

    LaunchedEffect(Unit) {
        speechRecognizerManager.setOnResultListener { spokenText ->
            Log.d(
                "VOICE",
                spokenText
            )
            calculatorViewModel.processVoiceInput(spokenText)
        }
    }

    if (calculatorViewModel.currentTheme == null) {
        return
    }

    VoiceCalcTheme(
        theme = calculatorViewModel.currentTheme!!
    ) {
        Scaffold(
            topBar = {
                CalculatorTopBar(navController = navController)

            },
            bottomBar = {
                CalculatorBottomBar(
                    onCalculatorClick = {
                        navController.navigate(
                            Routes.HOME_SCREEN
                        )
                    },
                    onSettingClick = {
                        navController.navigate(
                            Routes.CONVERTER_SCREEN
                        )
                    }
                )
            },
            floatingActionButton = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(18.dp, 66.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    VoiceButton(
                        onClick = {
                            if (speechRecognizerManager.isListening) {
                                speechRecognizerManager.stopListening()
                            } else {
                                permissionLauncher.launch(
                                    android.Manifest.permission.RECORD_AUDIO
                                )
                            }
                        }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Bottom
            ) {
                NavGraph(
                    navController = navController,
                    calculatorViewModel = calculatorViewModel
                )
            }
        }
    }
}