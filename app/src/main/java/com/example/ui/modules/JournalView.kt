package com.example.ui.modules

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.JournalEntry
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val journals by viewModel.journals.collectAsState()
    
    var logText by remember { mutableStateOf("") }
    var reflectionText by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("Calm") }
    
    val moods = listOf(
        "Excited" to "🤩",
        "Happy" to "😊",
        "Calm" to "🧘",
        "Productive" to "⚡",
        "Tired" to "💤",
        "Anxious" to "😰"
    )

    var isAddingJournal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Life Journal", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
            // Level / Reflection Promo
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📓", fontSize = 34.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Introspective Logging",
                                fontWeight = FontWeight.Bold,
                                color = CosmicCyan,
                                fontSize = 16.sp
                            )
                            Text(
                                "Clearing your cognitive database. Writing a journal logs reflections and rewards you +25 XP.",
                                fontSize = 13.sp,
                                color = IceGreyText
                            )
                        }
                    }
                }
            }

            // Quick Toggle Entry Form
            item {
                Button(
                    onClick = { isAddingJournal = !isAddingJournal },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_add_journal_btn")
                ) {
                    Icon(if (isAddingJournal) Icons.Default.Close else Icons.Default.Edit, contentDescription = "Journal Pencil", tint = SpaceDeepAbyss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isAddingJournal) "Close Reflection Form" else "Write Today's Reflections", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                }
            }

            // Entry Form Card
            item {
                AnimatedVisibility(
                    visible = isAddingJournal,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Log Cognitive State", fontWeight = FontWeight.Bold, color = CosmicTeal, fontSize = 14.sp)

                            // Mood Selector Grid/Row
                            Text("Select Current Mood Indicator:", fontSize = 12.sp, color = IceGreyText)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                moods.forEach { (moodName, emoji) ->
                                    val isSelected = selectedMood == moodName
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) CosmicCyan.copy(alpha = 0.2f) else Color.Transparent)
                                            .clickable { selectedMood = moodName }
                                            .padding(6.dp)
                                    ) {
                                        Text(emoji, fontSize = 28.sp)
                                        Text(
                                            moodName,
                                            fontSize = 10.sp,
                                            color = if (isSelected) CosmicCyan else IceGreyText,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = logText,
                                onValueChange = { logText = it },
                                label = { Text("Daily Narrative", color = CosmicGrey) },
                                minLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CosmicCyan,
                                    unfocusedBorderColor = CosmicGrey,
                                    focusedTextColor = SmoothWhite,
                                    unfocusedTextColor = SmoothWhite
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("journal_thought_input")
                            )

                            OutlinedTextField(
                                value = reflectionText,
                                onValueChange = { reflectionText = it },
                                label = { Text("Core Lesson / Reflection", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CosmicCyan,
                                    unfocusedBorderColor = CosmicGrey,
                                    focusedTextColor = SmoothWhite,
                                    unfocusedTextColor = SmoothWhite
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("journal_reflection_input")
                            )

                            Button(
                                onClick = {
                                    if (logText.isNotBlank()) {
                                        viewModel.addJournal(selectedMood, logText, reflectionText)
                                        logText = ""
                                        reflectionText = ""
                                        isAddingJournal = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_journal_btn")
                            ) {
                                Text("Commit Journal (+25 XP)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Logs
            item {
                Text(
                    text = "Reflective Timeline Logs",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SmoothWhite
                )
            }

            if (journals.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No journal logs made yet. Click raw reflections above.", color = CosmicGrey, fontSize = 14.sp)
                    }
                }
            } else {
                items(journals, key = { it.id }) { journal ->
                    JournalItemCard(
                        journal = journal,
                        onDelete = { viewModel.deleteJournal(journal.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun JournalItemCard(
    journal: JournalEntry,
    onDelete: () -> Unit
) {
    val moodEmoji = when (journal.mood) {
        "Excited" -> "🤩"
        "Happy" -> "😊"
        "Calm" -> "🧘"
        "Productive" -> "⚡"
        "Tired" -> "💤"
        "Anxious" -> "😰"
        else -> "🧘"
    }

    val moodColor = when (journal.mood) {
        "Excited" -> MoodExcited
        "Happy" -> MoodHappy
        "Calm" -> MoodCalm
        "Productive" -> MoodProductive
        "Tired" -> MoodTired
        "Anxious" -> MoodAnxious
        else -> CosmicTeal
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(moodColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(moodEmoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(journal.date, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 14.sp)
                        Text(journal.mood, color = moodColor, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.testTag("delete_journal_${journal.id}")) {
                    Icon(Icons.Default.Delete, contentDescription = "Purge entry", tint = CosmicRed.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(journal.log, color = IceGreyText, fontSize = 13.sp, lineHeight = 18.sp)

            if (journal.reflection.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceCardBg)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("Reflective Insight:", fontSize = 10.sp, color = CosmicTeal, fontWeight = FontWeight.Bold)
                        Text(journal.reflection, fontSize = 12.sp, color = SmoothWhite)
                    }
                }
            }
        }
    }
}
