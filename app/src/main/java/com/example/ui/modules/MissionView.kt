package com.example.ui.modules

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MissionGoal
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val missions by viewModel.missions.collectAsState()

    var missionTitle by remember { mutableStateOf("") }
    var missionDesc by remember { mutableStateOf("") }
    var missionDue by remember { mutableStateOf("") }
    var isAddingMission by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mission Center", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "🎯 Long-Term Missions & Milestones",
                            fontWeight = FontWeight.Bold,
                            color = CosmicCyan,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Define broad arcs for personal excellence. Complete milestones and slide the progress indicators to earn substantial level XP bonuses (+100 XP upon completion!).",
                            fontSize = 13.sp,
                            color = IceGreyText
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { isAddingMission = !isAddingMission },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_add_mission_btn")
                ) {
                    Icon(if (isAddingMission) Icons.Default.Close else Icons.Default.AddLocation, contentDescription = "Goal flag", tint = SpaceDeepAbyss)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isAddingMission) "Close Form" else "Initiate New Mission", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                }
            }

            item {
                AnimatedVisibility(
                    visible = isAddingMission,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SpaceCardBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = missionTitle,
                                onValueChange = { missionTitle = it },
                                label = { Text("Mission Title", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("mission_title_input")
                            )

                            OutlinedTextField(
                                value = missionDesc,
                                onValueChange = { missionDesc = it },
                                label = { Text("Mission Objective & Criteria", color = CosmicGrey) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = missionDue,
                                onValueChange = { missionDue = it },
                                label = { Text("Target Deadline (e.g. Q4 2026)", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    if (missionTitle.isNotBlank()) {
                                        viewModel.addMission(missionTitle, missionDesc, missionDue)
                                        missionTitle = ""
                                        missionDesc = ""
                                        missionDue = ""
                                        isAddingMission = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_mission_btn")
                            ) {
                                Text("Load Mission Objective (+100 XP reward space)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (missions.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("No active missions logged.", color = CosmicGrey, fontSize = 13.sp)
                    }
                }
            } else {
                items(missions, key = { it.id }) { mission ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(mission.title, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 16.sp)
                                    Text("Deadline: ${mission.dueDate}", fontSize = 11.sp, color = CosmicTeal, fontWeight = FontWeight.SemiBold)
                                }
                                IconButton(onClick = { viewModel.deleteMission(mission.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Abort mission", tint = CosmicRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(mission.description, color = IceGreyText, fontSize = 13.sp)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Milestone progress tracking
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Overall Progress", fontSize = 12.sp, color = CosmicGrey)
                                Text("${mission.progress}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CosmicCyan)
                            }
                            
                            Slider(
                                value = mission.progress.toFloat(),
                                onValueChange = { floatVal ->
                                    viewModel.updateMissionProgress(mission, floatVal.toInt())
                                },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(
                                    thumbColor = CosmicCyan,
                                    activeTrackColor = CosmicCyan,
                                    inactiveTrackColor = SpaceCardBg
                                )
                            )
                            
                            if (mission.isCompleted) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MoodHappy.copy(alpha = 0.15f))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.EmojiEvents, contentDescription = "Crown icon", tint = CosmicGold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("MISSION ARCHIVED & XP APPLIED!", color = MoodHappy, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
