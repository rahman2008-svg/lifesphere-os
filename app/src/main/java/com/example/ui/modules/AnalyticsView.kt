package com.example.ui.modules

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val stats by viewModel.statsState.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val journals by viewModel.journals.collectAsState()
    val missions by viewModel.missions.collectAsState()
    val projects by viewModel.projects.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Operational Analytics", color = SmoothWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return", tint = SmoothWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceDeepAbyss)
            )
        },
        containerColor = SpaceDeepAbyss
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Main Gauge Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Productivity Index Score",
                            fontWeight = FontWeight.Bold,
                            color = CosmicCyan,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { stats.productivityScore / 100f },
                                modifier = Modifier.size(100.dp),
                                color = CosmicCyan,
                                strokeWidth = 8.dp,
                                trackColor = SpaceCardBg
                            )
                            Text(
                                "Pt ${stats.productivityScore}",
                                fontWeight = FontWeight.Bold,
                                color = SmoothWhite,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Computed organically based on total active tasks resolved, milestone progress, active project weight, and reflective journal continuity logs.",
                            fontSize = 12.sp,
                            color = IceGreyText,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Stats Sub Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Resolve Weight", fontSize = 11.sp, color = CosmicGrey)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${stats.taskCompletionRate}%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CosmicCyan)
                            Text("Task Completion", fontSize = 10.sp, color = IceGreyText)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Dominant Vibe", fontSize = 11.sp, color = CosmicGrey)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(stats.dominantMood, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CosmicGold)
                            Text("System State", fontSize = 10.sp, color = IceGreyText)
                        }
                    }
                }
            }

            // Activity Heatmap Simulated Vector Drawing
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = "Trend icon", tint = CosmicCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Consistency Heatmap Grid", fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Simulates relative activity density in local database columns.", fontSize = 11.sp, color = CosmicGrey)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom canvas drawing of contribution matrix
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            val spacing = 3.dp.toPx()
                            val numRows = 4
                            val numCols = 15
                            val itemWidth = (size.width - spacing * (numCols - 1)) / numCols
                            val itemHeight = (size.height - spacing * (numRows - 1)) / numRows

                            for (r in 0 until numRows) {
                                for (c in 0 until numCols) {
                                    // Generate modular highlight colors simulating high concentration zones
                                    val density = (r * c) % 5
                                    val fillPaint = when (density) {
                                        0 -> SpaceCardBg
                                        1 -> CosmicTeal.copy(alpha = 0.3f)
                                        2 -> CosmicTeal.copy(alpha = 0.6f)
                                        3 -> CosmicCyan.copy(alpha = 0.8f)
                                        else -> CosmicCyan
                                    }
                                    drawRoundRect(
                                        color = fillPaint,
                                        topLeft = androidx.compose.ui.geometry.Offset(
                                            x = c * (itemWidth + spacing),
                                            y = r * (itemHeight + spacing)
                                        ),
                                        size = Size(itemWidth, itemHeight),
                                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Database Object Volumes
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Operational Database Records", fontWeight = FontWeight.SemiBold, color = SmoothWhite, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Tasks: ${tasks.size}", fontSize = 12.sp, color = IceGreyText)
                                Text("Reflections: ${journals.size}", fontSize = 12.sp, color = IceGreyText)
                            }
                            Column {
                                Text("Missions: ${missions.size}", fontSize = 12.sp, color = IceGreyText)
                                Text("Projects: ${projects.size}", fontSize = 12.sp, color = IceGreyText)
                            }
                        }
                    }
                }
            }
        }
    }
}
