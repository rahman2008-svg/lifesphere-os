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
import com.example.data.NoteItem
import com.example.ui.theme.*
import com.example.viewmodel.LifeSphereViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesView(
    viewModel: LifeSphereViewModel,
    onBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var noteCategory by remember { mutableStateOf("Wiki") } // Wiki, Research, Academic, Personal
    var searchQuery by remember { mutableStateOf("") }
    var isAddingNote by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Knowledge Hub (& Personal Wiki)", color = SmoothWhite, fontWeight = FontWeight.Bold) },
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
                        Text("📚 Cyber-Notes & Learning Silos", fontWeight = FontWeight.Bold, color = CosmicCyan)
                        Text("Establish a decentralized personal wiki offline. Track literature, deep academic insights, or mental paradigms (+10 XP per node created).", fontSize = 12.sp, color = IceGreyText)
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Wiki Vault...", color = CosmicGrey) },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = CosmicCyan) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                    modifier = Modifier.fillMaxWidth().testTag("notes_search_input")
                )
            }

            item {
                Button(
                    onClick = { isAddingNote = !isAddingNote },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_add_note_btn")
                ) {
                    Icon(if (isAddingNote) Icons.Default.Close else Icons.Default.MenuBook, contentDescription = "Book note icon", tint = SpaceDeepAbyss)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isAddingNote) "Dismiss Editor" else "Create Creative Note", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                }
            }

            item {
                AnimatedVisibility(
                    visible = isAddingNote,
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
                                value = noteTitle,
                                onValueChange = { noteTitle = it },
                                label = { Text("Note Title", color = CosmicGrey) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("note_title_input")
                            )

                            OutlinedTextField(
                                value = noteContent,
                                onValueChange = { noteContent = it },
                                label = { Text("Note Content / Text Markdown", color = CosmicGrey) },
                                minLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CosmicCyan, focusedTextColor = SmoothWhite, unfocusedTextColor = SmoothWhite),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Wiki", "Research", "Academic", "Personal").forEach { cat ->
                                    val isSelected = noteCategory == cat
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { noteCategory = cat },
                                        label = { Text(cat, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CosmicCyan, selectedLabelColor = SpaceDeepAbyss)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (noteTitle.isNotBlank()) {
                                        viewModel.addNote(noteTitle, noteContent, noteCategory)
                                        noteTitle = ""
                                        noteContent = ""
                                        isAddingNote = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CosmicCyan),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_note_btn")
                            ) {
                                Text("Engrave Note (+10 XP)", color = SpaceDeepAbyss, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            val filteredNotes = if (searchQuery.isBlank()) {
                notes
            } else {
                notes.filter { it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true) }
            }

            if (filteredNotes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("No notes found matching query.", color = CosmicGrey, fontSize = 13.sp)
                    }
                }
            } else {
                items(filteredNotes, key = { it.id }) { note ->
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
                                    Text(note.title, fontWeight = FontWeight.Bold, color = SmoothWhite, fontSize = 15.sp)
                                    Text(note.category, fontSize = 11.sp, color = CosmicTeal, fontWeight = FontWeight.SemiBold)
                                }
                                IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Purge note", tint = CosmicRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(note.content, color = IceGreyText, fontSize = 13.sp, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
