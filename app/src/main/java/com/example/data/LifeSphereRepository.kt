package com.example.data

import kotlinx.coroutines.flow.Flow

class LifeSphereRepository(private val dao: LifeSphereDao) {

    // Tasks
    val allTasks: Flow<List<TaskItem>> = dao.getAllTasks()
    suspend fun insertTask(task: TaskItem) = dao.insertTask(task)
    suspend fun deleteTaskById(id: Int) = dao.deleteTaskById(id)

    // Journals
    val allJournals: Flow<List<JournalEntry>> = dao.getAllJournals()
    suspend fun insertJournal(journal: JournalEntry) = dao.insertJournal(journal)
    suspend fun deleteJournalById(id: Int) = dao.deleteJournalById(id)

    // Memories
    val allMemories: Flow<List<MemoryItem>> = dao.getAllMemories()
    suspend fun insertMemory(memory: MemoryItem) = dao.insertMemory(memory)
    suspend fun deleteMemoryById(id: Int) = dao.deleteMemoryById(id)

    // Missions
    val allMissions: Flow<List<MissionGoal>> = dao.getAllMissions()
    suspend fun insertMission(mission: MissionGoal) = dao.insertMission(mission)
    suspend fun deleteMissionById(id: Int) = dao.deleteMissionById(id)

    // Notes
    val allNotes: Flow<List<NoteItem>> = dao.getAllNotes()
    suspend fun insertNote(note: NoteItem) = dao.insertNote(note)
    suspend fun deleteNoteById(id: Int) = dao.deleteNoteById(id)

    // Projects
    val allProjects: Flow<List<ProjectItem>> = dao.getAllProjects()
    suspend fun insertProject(project: ProjectItem) = dao.insertProject(project)
    suspend fun deleteProjectById(id: Int) = dao.deleteProjectById(id)

    // Profiles
    val allProfiles: Flow<List<ProfileItem>> = dao.getAllProfiles()
    suspend fun insertProfile(profile: ProfileItem) = dao.insertProfile(profile)
    suspend fun deleteProfileById(id: Int) = dao.deleteProfileById(id)
}
