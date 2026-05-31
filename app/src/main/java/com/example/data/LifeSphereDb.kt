package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// ROOM ENTITIES
// ==========================================

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val dueDate: String = "",
    val category: String = "General", // General, Daily, Project: [X], Mission: [Y]
    val xpReward: Int = 15
)

@Entity(tableName = "journals")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val mood: String, // Happy, Calm, Anxious, Productive, Tired, Excited
    val log: String,
    val reflection: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "memories")
data class MemoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,
    val category: String, // Timeline, Vault, Bookmark, Anniversary
    val mediaUri: String = "", // Holds URL or local note reference
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "missions")
data class MissionGoal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val progress: Int = 0, // 0 to 100
    val isCompleted: Boolean = false,
    val xpReward: Int = 100,
    val dueDate: String = ""
)

@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val category: String, // Research, Wiki, Academic, Personal
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "projects")
data class ProjectItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val status: String = "Planning", // Planning, Active, Completed
    val deadline: String = ""
)

@Entity(tableName = "profiles")
data class ProfileItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val relation: String, // Friend, Family, Contact
    val importantDate: String, // Birthday or important calendar day
    val dateDescription: String = "Birthday",
    val notes: String = ""
)

// ==========================================
// ROOM DAO
// ==========================================

@Dao
interface LifeSphereDao {
    // Tasks
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    // Journals
    @Query("SELECT * FROM journals ORDER BY timestamp DESC")
    fun getAllJournals(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntry)

    @Query("DELETE FROM journals WHERE id = :id")
    suspend fun deleteJournalById(id: Int)

    // Memories
    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<MemoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryItem)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemoryById(id: Int)

    // Missions
    @Query("SELECT * FROM missions ORDER BY id DESC")
    fun getAllMissions(): Flow<List<MissionGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: MissionGoal)

    @Query("DELETE FROM missions WHERE id = :id")
    suspend fun deleteMissionById(id: Int)

    // Notes
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItem)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    // Projects
    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAllProjects(): Flow<List<ProjectItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectItem)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)

    // Profiles
    @Query("SELECT * FROM profiles ORDER BY name ASC")
    fun getAllProfiles(): Flow<List<ProfileItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileItem)

    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Int)
}

// ==========================================
// ROOM DATABASE
// ==========================================

@Database(
    entities = [
        TaskItem::class,
        JournalEntry::class,
        MemoryItem::class,
        MissionGoal::class,
        NoteItem::class,
        ProjectItem::class,
        ProfileItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LifeSphereDatabase : RoomDatabase() {
    abstract fun dao(): LifeSphereDao

    companion object {
        @Volatile
        private var INSTANCE: LifeSphereDatabase? = null

        fun getDatabase(context: Context): LifeSphereDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LifeSphereDatabase::class.java,
                    "lifesphere_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
