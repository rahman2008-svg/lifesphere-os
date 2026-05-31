package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LifeSphereViewModel(application: Application) : AndroidViewModel(application) {

    private val database = LifeSphereDatabase.getDatabase(application)
    private val repository = LifeSphereRepository(database.dao())

    // UI exposed state flows
    val tasks: StateFlow<List<TaskItem>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journals: StateFlow<List<JournalEntry>> = repository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memories: StateFlow<List<MemoryItem>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val missions: StateFlow<List<MissionGoal>> = repository.allMissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<NoteItem>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectItem>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profiles: StateFlow<List<ProfileItem>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Dynamic Gamification State ---
    private val taskJournalNotesFlow = combine(tasks, journals, notes) { t, j, n ->
        Triple(t, j, n)
    }

    private val memoriesMissionsProjectsFlow = combine(memories, missions, projects) { mem, mis, proj ->
        Triple(mem, mis, proj)
    }

    val xpState: StateFlow<GamificationState> = combine(
        taskJournalNotesFlow,
        memoriesMissionsProjectsFlow
    ) { part1, part2 ->
        val (tList, jList, nList) = part1
        val (mList, miList, pList) = part2
        // Calculate XP
        val completedTasksXp = tList.count { it.isCompleted } * 15
        val journalsXp = jList.size * 25
        val memoriesXp = mList.size * 10
        val completedMissionsXp = miList.count { it.isCompleted } * 100
        val inProgressMissionsXp = miList.filter { !it.isCompleted }.sumOf { (it.progress * 0.5).toInt() }
        val notesXp = nList.size * 10
        val completedProjectsXp = pList.count { it.status == "Completed" } * 150
        val activeProjectsXp = pList.count { it.status == "Active" } * 50

        val totalXp = completedTasksXp + journalsXp + memoriesXp + completedMissionsXp + inProgressMissionsXp + notesXp + completedProjectsXp + activeProjectsXp
        
        // Define level threshold at 100 XP per level
        val level = (totalXp / 100) + 1
        val xpInLevel = totalXp % 100
        val levelName = when {
            level >= 100 -> "Legend"
            level >= 50 -> "Achiever"
            level >= 25 -> "Builder"
            level >= 10 -> "Learner"
            level >= 5 -> "Novice Cultivator"
            else -> "Explorer"
        }

        // Streaks: consecutive journal entry days (mock computation based on unique days logged)
        val streak = jList.map { it.date }.distinct().size

        GamificationState(
            totalXp = totalXp,
            level = level,
            xpProgress = xpInLevel,
            levelTitle = levelName,
            streakCount = streak
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        GamificationState(0, 1, 0, "Explorer", 0)
    )

    // --- Dynamic Analytics State ---
    val statsState: StateFlow<LifeStats> = combine(
        tasks, journals, missions, projects
    ) { t, j, m, p ->
        val totalTasks = t.size
        val compTasks = t.count { it.isCompleted }
        val taskCompletionRate = if (totalTasks > 0) (compTasks * 100 / totalTasks) else 0

        val compMissions = m.count { it.isCompleted }
        val activeProjects = p.count { it.status == "Active" }
        
        val journalCount = j.size
        // Mood map
        val moodCounts = j.groupingBy { it.mood }.eachCount()
        val dominantMood = moodCounts.maxByOrNull { it.value }?.key ?: "Productive"

        val productivityScore = ((taskCompletionRate * 0.5) + (compMissions * 15) + (activeProjects * 10)).coerceIn(0.0, 100.0).toInt()

        LifeStats(
            taskCompletionRate = taskCompletionRate,
            completedMissions = compMissions,
            activeProjects = activeProjects,
            journalCount = journalCount,
            dominantMood = dominantMood,
            productivityScore = if (productivityScore == 0) 50 else productivityScore
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LifeStats(0, 0, 0, 0, "Calm", 50)
    )

    init {
        // Prepopulate default items if database is totally fresh
        viewModelScope.launch {
            tasks.first() // Wait till ready
            val isDbEmpty = tasks.value.isEmpty() && journals.value.isEmpty() && notes.value.isEmpty()
            if (isDbEmpty) {
                prepopulateWorkspace()
            }
        }
    }

    private suspend fun prepopulateWorkspace() {
        // Default Tasks
        repository.insertTask(TaskItem(title = "Boot LifeSphere OS for the first time", isCompleted = true, category = "Daily"))
        repository.insertTask(TaskItem(title = "Set my first long-term Life Mission", isCompleted = false, category = "Daily"))
        repository.insertTask(TaskItem(title = "Log my daily Mood and reflection in Life Journal", isCompleted = false, category = "Daily"))
        repository.insertTask(TaskItem(title = "Define core milestones for Project Hub", isCompleted = false, category = "Project: LifeSphere"))

        // Default Journal
        repository.insertJournal(JournalEntry(
            date = "2026-05-31",
            mood = "Excited",
            log = "Initial boot of LifeSphere OS completed. I'm excited to streamline my life goals, track habits offline-first, and establish absolute privacy for my journals and memory timeline.",
            reflection = "Taking charge of my mind, focus, and digital identity."
        ))

        // Default Mission
        repository.insertMission(MissionGoal(
            title = "Personal Sovereignty Initiative",
            description = "Evolve physically, mentally, and financially. Standardize daily notes, organize memory vault, and focus on clean learning.",
            progress = 30,
            dueDate = "2026-12-31"
        ))

        // Default Note
        repository.insertNote(NoteItem(
            title = "Life Desk Wiki & Manifesto",
            content = "1. Keep data locally stored, encrypt backups manually.\n2. Dedicate 90-minute blocks to Deep Work.\n3. Log journals daily to clear the cognitive stack.\n4. Revisit Memories on their anniversary to leverage Past Lessons.",
            category = "Wiki"
        ))

        // Default Project
        repository.insertProject(ProjectItem(
            name = "Life Operating System Upgrades",
            description = "Improve life consistency and review growth data. Refine tracking for goals.",
            status = "Active",
            deadline = "2026-06-30"
        ))

        // Default Profile
        repository.insertProfile(ProfileItem(
            name = "Prince AR Abdur Rahman",
            relation = "Creator & Founder",
            importantDate = "August 12",
            dateDescription = "Founder Birthday",
            notes = "Lead developer interested in user experience design, offline-first systems, and educational tech."
        ))

        // Default Memory
        repository.insertMemory(MemoryItem(
            title = "LifeSphere OS Creation",
            description = "Officially launched life operating system architecture. Secured all local variables.",
            date = "2026-05-31",
            category = "Timeline"
        ))
    }

    // --- Action Handlers ---

    fun toggleTask(task: TaskItem) {
        viewModelScope.launch {
            repository.insertTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addTask(title: String, category: String, dueDate: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertTask(TaskItem(title = title, category = category, dueDate = dueDate))
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
        }
    }

    fun addJournal(mood: String, log: String, reflection: String) {
        if (log.isBlank()) return
        viewModelScope.launch {
            val dateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            repository.insertJournal(JournalEntry(date = dateStr, mood = mood, log = log, reflection = reflection))
        }
    }

    fun deleteJournal(id: Int) {
        viewModelScope.launch {
            repository.deleteJournalById(id)
        }
    }

    fun addMemory(title: String, description: String, category: String, mediaUri: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val dateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            repository.insertMemory(MemoryItem(title = title, description = description, category = category, mediaUri = mediaUri, date = dateStr))
        }
    }

    fun deleteMemory(id: Int) {
        viewModelScope.launch {
            repository.deleteMemoryById(id)
        }
    }

    fun addMission(title: String, description: String, dueDate: String, progress: Int = 0) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertMission(MissionGoal(title = title, description = description, progress = progress, dueDate = dueDate))
        }
    }

    fun updateMissionProgress(mission: MissionGoal, newProgress: Int) {
        viewModelScope.launch {
            val isComp = newProgress >= 100
            repository.insertMission(mission.copy(progress = newProgress, isCompleted = isComp))
        }
    }

    fun deleteMission(id: Int) {
        viewModelScope.launch {
            repository.deleteMissionById(id)
        }
    }

    fun addNote(title: String, content: String, category: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertNote(NoteItem(title = title, content = content, category = category))
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNoteById(id)
        }
    }

    fun addProject(name: String, description: String, deadline: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insertProject(ProjectItem(name = name, description = description, status = "Active", deadline = deadline))
        }
    }

    fun updateProjectStatus(project: ProjectItem, status: String) {
        viewModelScope.launch {
            repository.insertProject(project.copy(status = status))
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.deleteProjectById(id)
        }
    }

    fun addProfile(name: String, relation: String, importantDate: String, dateDescription: String, notes: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insertProfile(ProfileItem(name = name, relation = relation, importantDate = importantDate, dateDescription = dateDescription, notes = notes))
        }
    }

    fun deleteProfile(id: Int) {
        viewModelScope.launch {
            repository.deleteProfileById(id)
        }
    }
}

// Data structures for non-persistent computed UI states
data class GamificationState(
    val totalXp: Int,
    val level: Int,
    val xpProgress: Int, // XP earned in current level (0 to 99)
    val levelTitle: String,
    val streakCount: Int
)

data class LifeStats(
    val taskCompletionRate: Int,
    val completedMissions: Int,
    val activeProjects: Int,
    val journalCount: Int,
    val dominantMood: String,
    val productivityScore: Int
)
