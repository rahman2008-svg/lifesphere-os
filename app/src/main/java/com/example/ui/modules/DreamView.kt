package com.example.ui.modules

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DreamView(
    onBack: () -> Unit
) {
    // Standard visual inspiration list (Dream board) stored locally
    val defaultInspirations = remember {
        mutableStateListOf(
            InspirationItem("Launch Global Tech Studio", "Create high-grade educational apps and productivity ecosystems that assist million users worldwide.", "🔥 Core Focus"),
            InspirationItem("Financial Independence", "Build secure automated SaaS pipelines and open-source models.", "💎 Freedom"),
            InspirationItem("Physiological Supremacy", "Establish continuous training blocks: daily running, weights, clean organic eating habits.", "🏃 Health"),
            InspirationItem("AI Research Hub", "Deeply understand large language models, agent frameworks, and offline-first edge databases.", "🧠 Learning"),
            InspirationItem("Serene Nomadic Workspace", "Set up minimal, solar-reliant off-grid digital setups to code while retaining direct connections with nature.", "🌲 Environment"),
            InspirationItem("Create Legendary Products", "Craft polished vector details and outstanding UX layout designs that feel like tactile wood carvings.", "👑 Design")
        )
    }

    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var selectCategory by remember { mutableStateOf("🔥 Focus") }
    var isAdding by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dream Board & Vision Map", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Inspirational Quote Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "\"The best way to predict the future is to design and code it.\"",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = CosmicCyan,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "— LifeSphere OS Core Mantra",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicGrey,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Visions of Progress", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SmoothWhite)
                Button(
                    onClick = { isAdding = !isAdding },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isAdding) CosmicRed else CosmicCyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("toggle_add_dream_btn")
                ) {
                    Text(if (isAdding) "Close" else "Add Goal", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            AnimatedVisibility(
                visible = isAdding,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = SpaceCardBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Vision Title", color = CosmicGrey) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                            modifier = Modifier.fillMaxWidth().testTag("dream_title_input")
                        )
                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("What does this success look like?", color = CosmicGrey) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    defaultInspirations.add(0, InspirationItem(newTitle, newDesc, selectCategory))
                                    newTitle = ""
                                    newDesc = ""
                                    isAdding = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                            modifier = Modifier.fillMaxWidth().testTag("submit_dream_btn")
                        ) {
                            Text("Fasten Vision to Board", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // High Fidelity Responsive Grid layout for tablet and phone
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(defaultInspirations) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.badge,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CosmicCyan,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SpaceCardBg)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                IconButton(
                                    onClick = { defaultInspirations.remove(item) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Clear, contentDescription = "Purge vision", tint = CosmicRed.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(item.title, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.description, color = IceGreyText, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

data class InspirationItem(
    val title: String,
    val description: String,
    val badge: String
)
