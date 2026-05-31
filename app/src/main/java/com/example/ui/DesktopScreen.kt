package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopScreen(
    viewModel: LifeSphereViewModel,
    onNavigate: (String) -> Unit
) {
    val xpState by viewModel.xpState.collectAsState()
    val stats by viewModel.statsState.collectAsState()
    
    // Privacy state simulator
    var isPrivateVaultLocked by remember { mutableStateOf(false) }

    val appGridItems = remember {
        listOf(
            AppConfig("Daily Desk", "Tasks & Agenda", "dashboard", Icons.Outlined.Dashboard, CosmicCyan),
            AppConfig("Memory Engine", "Vault & Kin Profiles", "memory", Icons.Outlined.AutoAwesome, CosmicLavender),
            AppConfig("Mission Center", "Milestones & Goals", "mission", Icons.Outlined.Flag, CosmicGold),
            AppConfig("Knowledge Hub", "Wiki & Learnings", "notes", Icons.Outlined.MenuBook, CosmicTeal),
            AppConfig("Life Journal", "Moods & Reflections", "journal", Icons.Outlined.HistoryEdu, CosmicCyan),
            AppConfig("Project Hub", "Schedules & Tasks", "projects", Icons.Outlined.FolderCopy, CosmicLavender),
            AppConfig("Dream Board", "Visions of Progress", "dream", Icons.Outlined.StarBorder, CosmicGold),
            AppConfig("Operational Stats", "Productivity Analytics", "analytics", Icons.Outlined.TrendingUp, CosmicTeal),
            AppConfig("System Credits", "About Developer", "about", Icons.Outlined.Info, CosmicGrey)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LifeSphere OS",
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicCyan,
                            fontSize = 20.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = "Active connection status",
                                tint = CosmicCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Offline-First Secured",
                                fontSize = 11.sp,
                                color = CosmicTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceDeepAbyss)
            )
        },
        containerColor = SpaceDeepAbyss
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gamification HUD Header Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                border = BorderStroke(1.dp, SpaceGlassStroke)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // level title rows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LEVEL ${xpState.level}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTeal,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = xpState.levelTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = SmoothWhite
                            )
                        }
                        
                        // Streak info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CosmicGold.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${xpState.streakCount} D Streak",
                                fontWeight = FontWeight.Bold,
                                color = CosmicGold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Level XP Indicator Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Core System Experience (XP)",
                                fontSize = 11.sp,
                                color = CosmicGrey
                            )
                            Text(
                                "${xpState.totalXp} XP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicCyan
                            )
                        }
                        LinearProgressIndicator(
                            progress = { xpState.xpProgress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = CosmicCyan,
                            trackColor = SpaceCardBg
                        )
                    }

                    // Short stats strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DashboardStatBadge("Productivity", "Pt ${stats.productivityScore}", CosmicCyan)
                        DashboardStatBadge("Active Proj", "${stats.activeProjects}", CosmicLavender)
                        DashboardStatBadge("Missions", "${stats.completedMissions} Done", CosmicGold)
                    }
                }
            }

            // Security Lock / Privacy quick widget
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                border = BorderStroke(1.dp, if (isPrivateVaultLocked) CosmicRed.copy(alpha = 0.3f) else SpaceGlassStroke)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isPrivateVaultLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Lock indicator",
                            tint = if (isPrivateVaultLocked) CosmicRed else CosmicCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isPrivateVaultLocked) "Encrypted Vault Shielded" else "Live OS State Decrypted",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SmoothWhite
                            )
                            Text(
                                text = if (isPrivateVaultLocked) "Required local auth toggle" else "Secured locally inside app context",
                                fontSize = 10.sp,
                                color = CosmicGrey
                            )
                        }
                    }
                    Switch(
                        checked = isPrivateVaultLocked,
                        onCheckedChange = { isPrivateVaultLocked = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CosmicRed,
                            checkedTrackColor = CosmicRed.copy(alpha = 0.3f),
                            uncheckedThumbColor = CosmicCyan,
                            uncheckedTrackColor = SpaceCardBg
                        ),
                        modifier = Modifier.testTag("security_lock_switch")
                    )
                }
            }

            // Launcher Grid Header
            Text(
                text = "SYSTEM ENGINE APPLICATIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CosmicGrey,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Grid Layout of Apps
            if (isPrivateVaultLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Secured logo",
                            tint = CosmicRed,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "Access Denied",
                            fontWeight = FontWeight.Bold,
                            color = SmoothWhite,
                            fontSize = 16.sp
                        )
                        Text(
                            "LifeSphere OS data is sealed. Toggle Security Lock to access application structures.",
                            textAlign = TextAlign.Center,
                            color = CosmicGrey,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(appGridItems) { config ->
                        AppIconTile(
                            config = config,
                            onClick = { onNavigate(config.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppIconTile(
    config: AppConfig,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .clickable(onClick = onClick)
            .testTag("app_tile_${config.route}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
        border = BorderStroke(1.dp, SpaceGlassStroke.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(config.color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = config.name,
                    tint = config.color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = config.name,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = SmoothWhite,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = config.desc,
                fontSize = 8.sp,
                color = CosmicGrey,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DashboardStatBadge(
    label: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SpaceCardBg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label: $value",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = IceGreyText
            )
        }
    }
}

data class AppConfig(
    val name: String,
    val desc: String,
    val route: String,
    val icon: ImageVector,
    val color: Color
)
