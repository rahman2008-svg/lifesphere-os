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
import com.example.data.MemoryItem
import com.example.data.ProfileItem
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val memories by viewModel.memories.collectAsState()
    val profiles by viewModel.profiles.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Vault Timeline, 1 = Family Profiles

    // Form inputs for Memories
    var memoryTitle by remember { mutableStateOf("") }
    var memoryDesc by remember { mutableStateOf("") }
    var memoryCategory by remember { mutableStateOf("Timeline") } // Timeline, Vault, Bookmark
    var memoryUrl by remember { mutableStateOf("") }
    var isAddingMemory by remember { mutableStateOf(false) }

    // Form inputs for Profiles
    var profileName by remember { mutableStateOf("") }
    var profileRelation by remember { mutableStateOf("") }
    var profileDate by remember { mutableStateOf("") }
    var profileDateDesc by remember { mutableStateOf("Birthday") }
    var profileNote by remember { mutableStateOf("") }
    var isAddingProfile by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memory Desk & Vault", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
        ) {
            // Elegant Navigation TabRow for modules
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SpaceDarkSteel)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTab = 0 }
                        .background(if (activeTab == 0) CosmicCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Local Vault Timeline",
                        color = if (activeTab == 0) CosmicCyan else IceGreyText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeTab = 1 }
                        .background(if (activeTab == 1) CosmicCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Family & Kin profiles",
                        color = if (activeTab == 1) CosmicCyan else IceGreyText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
            ) {
                if (activeTab == 0) {
                    // TAB 0: VAULT TIMELINE
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SpaceDarkSteel)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("🔒 Secure Local Log Engine", fontWeight = FontWeight.Bold, color = CosmicCyan)
                                Text("Your memories are kept strict and encrypted layout offline. Log timelines to look back years down the path.", fontSize = 12.sp, color = IceGreyText)
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { isAddingMemory = !isAddingMemory },
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("toggle_add_memory_btn")
                        ) {
                            Icon(if (isAddingMemory) Icons.Default.Close else Icons.Default.Add, contentDescription = "Memory icon", tint = SpaceDeepAbyss)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isAddingMemory) "Close Form" else "Archive a Memory Instance", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                        }
                    }

                    item {
                        AnimatedVisibility(
                            visible = isAddingMemory,
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
                                        value = memoryTitle,
                                        onValueChange = { memoryTitle = it },
                                        label = { Text("Memory Title", color = CosmicGrey) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("memory_title_input")
                                    )

                                    OutlinedTextField(
                                        value = memoryDesc,
                                        onValueChange = { memoryDesc = it },
                                        label = { Text("Describe the Moment", color = CosmicGrey) },
                                        minLines = 2,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    OutlinedTextField(
                                        value = memoryUrl,
                                        onValueChange = { memoryUrl = it },
                                        label = { Text("Bookmark Link or Image Resource (Optional)", color = CosmicGrey) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    // Category chips
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("Timeline", "Vault", "Bookmark").forEach { cat ->
                                            val isSelected = memoryCategory == cat
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = { memoryCategory = cat },
                                                label = { Text(cat, fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CosmicCyan, selectedLabelColor = SpaceDeepAbyss)
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (memoryTitle.isNotBlank()) {
                                                viewModel.addMemory(memoryTitle, memoryDesc, memoryCategory, memoryUrl)
                                                memoryTitle = ""
                                                memoryDesc = ""
                                                memoryUrl = ""
                                                isAddingMemory = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("submit_memory_btn")
                                    ) {
                                        Text("Seal in Vault (+10 XP)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (memories.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                Text("No timelines sealed in Vault.", color = CosmicGrey, fontSize = 13.sp)
                            }
                        }
                    } else {
                        items(memories, key = { it.id }) { mem ->
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
                                            Text(mem.title, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                                            Text(mem.date, fontSize = 11.sp, color = CosmicTeal, fontWeight = FontWeight.SemiBold)
                                        }
                                        IconButton(onClick = { viewModel.deleteMemory(mem.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Purge entry", tint = CosmicRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(mem.description, color = IceGreyText, fontSize = 13.sp)
                                    if (mem.mediaUri.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("🔗 Resource: ${mem.mediaUri}", color = CosmicCyan, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // TAB 1: FAMILY PROFILES & IMPORTANT DATES
                    item {
                        Button(
                            onClick = { isAddingProfile = !isAddingProfile },
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("toggle_add_profile_btn")
                        ) {
                            Icon(if (isAddingProfile) Icons.Default.Close else Icons.Default.PersonAdd, contentDescription = "Register", tint = SpaceDeepAbyss)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isAddingProfile) "Close Form" else "Register Kin / Friend Profile", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                        }
                    }

                    item {
                        AnimatedVisibility(
                            visible = isAddingProfile,
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
                                        value = profileName,
                                        onValueChange = { profileName = it },
                                        label = { Text("Individual Name", color = CosmicGrey) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("profile_name_input")
                                    )

                                    OutlinedTextField(
                                        value = profileRelation,
                                        onValueChange = { profileRelation = it },
                                        label = { Text("Relation (e.g. Sister, Mentor)", color = CosmicGrey) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    OutlinedTextField(
                                        value = profileDate,
                                        onValueChange = { profileDate = it },
                                        label = { Text("Key Date (e.g. November 18)", color = CosmicGrey) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    OutlinedTextField(
                                        value = profileNote,
                                        onValueChange = { profileNote = it },
                                        label = { Text("Personal details, bio, or gift options", color = CosmicGrey) },
                                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Button(
                                        onClick = {
                                            if (profileName.isNotBlank()) {
                                                viewModel.addProfile(profileName, profileRelation, profileDate, profileDateDesc, profileNote)
                                                profileName = ""
                                                profileRelation = ""
                                                profileDate = ""
                                                profileNote = ""
                                                isAddingProfile = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("submit_profile_btn")
                                    ) {
                                        Text("Commit Kin Connection", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (profiles.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                Text("No relations registered. Add connection details.", color = CosmicGrey, fontSize = 13.sp)
                            }
                        }
                    } else {
                        items(profiles, key = { it.id }) { prof ->
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
                                            Text(prof.name, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 16.sp)
                                            Text(prof.relation, fontSize = 12.sp, color = CosmicTeal, fontWeight = FontWeight.SemiBold)
                                        }
                                        IconButton(onClick = { viewModel.deleteProfile(prof.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Purge contact", tint = CosmicRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("📅 ${prof.dateDescription}: ${prof.importantDate}", color = CosmicGold, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    if (prof.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(prof.notes, color = IceGreyText, fontSize = 12.sp)
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
