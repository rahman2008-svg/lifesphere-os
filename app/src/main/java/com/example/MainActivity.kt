package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.DesktopScreen
import com.example.ui.modules.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LifeSphereViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: LifeSphereViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("desktop") }

                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = {
                            if (targetState == "desktop") {
                                // Transition when going back
                                (slideInHorizontally { -it } + fadeIn())
                                    .togetherWith(slideOutHorizontally { it } + fadeOut())
                            } else {
                                // Transition when going forward
                                (slideInHorizontally { it } + fadeIn())
                                    .togetherWith(slideOutHorizontally { -it } + fadeOut())
                            }
                        },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            "desktop" -> {
                                DesktopScreen(viewModel) { dest ->
                                    currentScreen = dest
                                }
                            }
                            "dashboard" -> {
                                DashboardView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "memory" -> {
                                MemoryView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "mission" -> {
                                MissionView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "notes" -> {
                                NotesView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "journal" -> {
                                JournalView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "projects" -> {
                                ProjectsView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "dream" -> {
                                DreamView {
                                    currentScreen = "desktop"
                                }
                            }
                            "analytics" -> {
                                AnalyticsView(viewModel) {
                                    currentScreen = "desktop"
                                }
                            }
                            "about" -> {
                                AboutView {
                                    currentScreen = "desktop"
                                }
                            }
                            else -> {
                                DesktopScreen(viewModel) { dest ->
                                    currentScreen = dest
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
