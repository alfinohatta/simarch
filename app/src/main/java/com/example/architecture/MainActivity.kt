package com.example.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.architecture.theme.ArchitectureTheme
import com.example.architecture.ui.screens.*
import com.example.architecture.viewmodel.*

class MainActivity : ComponentActivity() {

    private val userPreferencesViewModel: UserPreferencesViewModel by viewModels()
    private val startupViewModel: StartupViewModel by viewModels()
    private val executionViewModel: ExecutionViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ArchitectureTheme {
                AppNavigation(
                    userViewModel = userPreferencesViewModel,
                    startupViewModel = startupViewModel,
                    executionViewModel = executionViewModel,
                    quizViewModel = quizViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    userViewModel: UserPreferencesViewModel,
    startupViewModel: StartupViewModel,
    executionViewModel: ExecutionViewModel,
    quizViewModel: QuizViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToStartup = { navController.navigate("startup") },
                onNavigateToExecution = { navController.navigate("execution") },
                onNavigateToReference = { navController.navigate("reference") },
                onNavigateToAbout = { navController.navigate("about") }
            )
        }
        composable("about") {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable("startup") {
            StartupScreen(
                viewModel = startupViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("execution") {
            ExecutionScreen(
                viewModel = executionViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("reference") {
            ComponentReferenceScreen(onBack = { navController.popBackStack() })
        }
        composable("quiz") {
            QuizScreen(
                viewModel = quizViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
