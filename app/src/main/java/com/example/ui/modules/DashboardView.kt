package com.example.ui.modules

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaskItem
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val profiles by viewModel.profiles.collectAsState()
    
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskCategory by remember { mutableStateOf("Daily") }
    val categories = listOf("Daily", "Project", "Mission", "Personal")
    
    // Quick Add dialog/form state
    var isAddingTask by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Desk", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
            // Welcome Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Daily Overview",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicCyan
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val incomplete = tasks.count { !it.isCompleted }
                        Text(
                            text = if (incomplete > 0) {
                                "You have $incomplete pending tasks today. Boost your focus to earn XP!"
                            } else {
                                "All clear! You've accomplished every daily objective. Level up imminent!"
                            },
                            fontSize = 14.sp,
                            color = IceGreyText
                        )
                    }
                }
            }

            // Quick Actions Block
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { isAddingTask = !isAddingTask },
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_add_task_btn")
                    ) {
                        Icon(
                            if (isAddingTask) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = "Add Task Icon",
                            tint = SpaceDeepAbyss
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isAddingTask) "Close Form" else "Quick Add Task", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Task Form
            item {
                AnimatedVisibility(
                    visible = isAddingTask,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("New Activity", fontWeight = FontWeight.Bold, color = CosmicTeal, fontSize = 14.sp)
                            
                            OutlinedTextField(
                                value = newTaskTitle,
                                onValueChange = { newTaskTitle = it },
                                label = { Text("What needs to be done?", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CosmicCyan,
                                    unfocusedBorderColor = CosmicGrey,
                                    focusedTextColor = SmoothWhite,
                                    unfocusedTextColor = SmoothWhite
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("task_title_input")
                            )

                            // Category chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                categories.forEach { cat ->
                                    val isSelected = newTaskCategory == cat
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { newTaskCategory = cat },
                                        label = { Text(cat, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = CosmicCyan,
                                            selectedLabelColor = SpaceDeepAbyss,
                                            containerColor = SpaceDarkSteel,
                                            labelColor = IceGreyText
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (newTaskTitle.isNotBlank()) {
                                        viewModel.addTask(newTaskTitle, newTaskCategory, "Today")
                                        newTaskTitle = ""
                                        isAddingTask = false
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_task_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan)
                            ) {
                                Text("Register Task (+15 XP)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Task List
            item {
                Text(
                    text = "Local Tasks Stack",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmoothWhite
                )
            }

            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active tasks found in database.", color = CosmicGrey, fontSize = 14.sp)
                    }
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onCheckedChange = { viewModel.toggleTask(task) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            // Upcoming Dates
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Anniversaries & Important Dates",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmoothWhite
                )
            }

            val upcomingProfiles = profiles.take(3)
            if (upcomingProfiles.isEmpty()) {
                item {
                    Text(
                        "No important dates registered yet. Update 'Profiles' in the Memory section.",
                        color = CosmicGrey,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(upcomingProfiles) { prof ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
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
                                    Icons.Default.Cake,
                                    contentDescription = "Event icon",
                                    tint = CosmicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(prof.name, fontWeight = FontWeight.Bold, color = SmoothWhite)
                                    Text("${prof.dateDescription}: ${prof.importantDate}", fontSize = 12.sp, color = IceGreyText)
                                }
                            }
                            Text(
                                text = prof.relation,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTeal,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SpaceCardBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskRow(
    task: TaskItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) SpaceDarkSteel.copy(alpha = 0.5f) else SpaceDarkSteel
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = CosmicTeal,
                        uncheckedColor = CosmicGrey,
                        checkmarkColor = SpaceDeepAbyss
                    ),
                    modifier = Modifier.testTag("task_checkbox_${task.id}")
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = task.title,
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                        color = if (task.isCompleted) CosmicGrey else SmoothWhite,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        fontSize = 14.sp
                    )
                    Text(
                        task.category,
                        fontSize = 11.sp,
                        color = CosmicTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_task_${task.id}")
            ) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Remove Task",
                    tint = CosmicRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
