package com.example.zv.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)
    
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
    
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int
}

