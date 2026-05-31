package com.example.ui.modules

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectItem
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()

    var projectName by remember { mutableStateOf("") }
    var projectDesc by remember { mutableStateOf("") }
    var projectDeadline by remember { mutableStateOf("") }
    var isAddingProject by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Hub", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("📁 Project Management Dashboard", fontWeight = FontWeight.Bold, color = CosmicCyan)
                        Text("Keep multiple initiatives structured. Active projects yield +50 XP, and Completing them awards a high +150 XP level injection directly to your user score.", fontSize = 12.sp, color = IceGreyText)
                    }
                }
            }

            item {
                Button(
                    onClick = { isAddingProject = !isAddingProject },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_add_project_btn")
                ) {
                    Icon(if (isAddingProject) Icons.Default.Close else Icons.Default.CreateNewFolder, contentDescription = "Folder add", tint = SpaceDeepAbyss)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isAddingProject) "Cancel Setup" else "Start New Project Initiative", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                }
            }

            item {
                AnimatedVisibility(
                    visible = isAddingProject,
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
                                value = projectName,
                                onValueChange = { projectName = it },
                                label = { Text("Project Name", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("project_name_input")
                            )

                            OutlinedTextField(
                                value = projectDesc,
                                onValueChange = { projectDesc = it },
                                label = { Text("Scope & Outcomes Description", color = CosmicGrey) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = projectDeadline,
                                onValueChange = { projectDeadline = it },
                                label = { Text("Completion Deadline (e.g. June 30)", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    if (projectName.isNotBlank()) {
                                        viewModel.addProject(projectName, projectDesc, projectDeadline)
                                        projectName = ""
                                        projectDesc = ""
                                        projectDeadline = ""
                                        isAddingProject = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_project_btn")
                            ) {
                                Text("Launch Project Base (+50 XP active bonus)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (projects.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("No active projects registered.", color = CosmicGrey, fontSize = 13.sp)
                    }
                }
            } else {
                items(projects, key = { it.id }) { proj ->
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
                                    Text(proj.name, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 16.sp)
                                    Text("Deadline: ${proj.deadline}", fontSize = 11.sp, color = CosmicTeal, fontWeight = FontWeight.SemiBold)
                                }
                                IconButton(onClick = { viewModel.deleteProject(proj.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Purge project", tint = CosmicRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(proj.description, color = IceGreyText, fontSize = 13.sp)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Interactive status picker
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Operational State:", fontSize = 12.sp, color = CosmicGrey)
                                
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("Planning", "Active", "Completed").forEach { status ->
                                        val isSelected = proj.status == status
                                        val statusColor = when (status) {
                                            "Planning" -> CosmicGrey
                                            "Active" -> CosmicCyan
                                            "Completed" -> MoodHappy
                                            else -> CosmicGrey
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) statusColor.copy(alpha = 0.25f) else SpaceCardBg)
                                                .clickable { viewModel.updateProjectStatus(proj, status) }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = status,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) statusColor else CosmicGrey
                                            )
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
}
