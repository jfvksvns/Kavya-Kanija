package com.kavyakanaja.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kavyakanaja.app.ui.screens.FavoritesScreen
import com.kavyakanaja.app.ui.screens.GenerateScreen
import com.kavyakanaja.app.ui.screens.HistoryScreen
import com.kavyakanaja.app.ui.screens.HomeScreen
import com.kavyakanaja.app.ui.screens.PoemDetailScreen
import com.kavyakanaja.app.ui.screens.PoetScreen
import com.kavyakanaja.app.ui.screens.QuizScreen
import com.kavyakanaja.app.ui.theme.KavyaKanajaTheme
import com.kavyakanaja.app.utils.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KavyaKanajaTheme {
                KavyaKanajaNavHost()
            }
        }
    }
}

@Composable
fun KavyaKanajaNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Constants.ROUTE_HOME
    ) {

        // ── Home ──────────────────────────────────────────────────────────
        composable(Constants.ROUTE_HOME) {
            HomeScreen(
                onPoemClick = { id ->
                    navController.navigate("poem_detail/$id/en")
                },
                onFavoritesClick = {
                    navController.navigate("favorites/en")
                },
                onHistoryClick = {
                    navController.navigate("history/en")
                },
                onGenerateClick = {
                    navController.navigate("generate/en")
                }
            )
        }

        // ── Poem Detail ───────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_DETAIL,
            arguments = listOf(
                navArgument("poemId")   { type = NavType.IntType },
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val poemId   = backStackEntry.arguments?.getInt("poemId")
                ?: return@composable
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            PoemDetailScreen(
                poemId         = poemId,
                language       = language,
                onNavigateBack = { navController.popBackStack() },
                onPoetClick    = { poetName ->
                    // URL-encode poet name in case it has spaces
                    navController.navigate(
                        "poet/${poetName.replace(" ", "%20")}/$language"
                    )
                },
                onQuizClick    = { id, poemText ->
                    // Pass poemText via saved state handle to avoid URL length issues
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("poemText", poemText)
                    navController.navigate("quiz/$id/$language")
                }
            )
        }

        // ── Poet Profile ──────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_POET,
            arguments = listOf(
                navArgument("poetName") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val poetName = backStackEntry.arguments?.getString("poetName")
                ?.replace("%20", " ")           // decode spaces back
                ?: return@composable
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            PoetScreen(
                poetName       = poetName,
                language       = language,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Quiz ──────────────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_QUIZ,
            arguments = listOf(
                navArgument("poemId")   { type = NavType.IntType },
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val poemId   = backStackEntry.arguments?.getInt("poemId")
                ?: return@composable
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            // Retrieve poemText passed via savedStateHandle from PoemDetailScreen
            val poemText = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("poemText") ?: ""

            QuizScreen(
                poemId         = poemId,
                poemText       = poemText,
                language       = language,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Generate Poem ─────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_GENERATE,
            arguments = listOf(
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            GenerateScreen(
                language       = language,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Favorites ─────────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_FAVORITES,
            arguments = listOf(
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            FavoritesScreen(
                language       = language,
                onNavigateBack = { navController.popBackStack() },
                onPoemClick    = { id ->
                    navController.navigate("poem_detail/$id/$language")
                }
            )
        }

        // ── History ───────────────────────────────────────────────────────
        composable(
            route = Constants.ROUTE_HISTORY,
            arguments = listOf(
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val language = backStackEntry.arguments?.getString("language")
                ?: "en"

            HistoryScreen(
                language       = language,
                onNavigateBack = { navController.popBackStack() },
                onPoemClick    = { id ->
                    navController.navigate("poem_detail/$id/$language")
                }
            )
        }
    }
}