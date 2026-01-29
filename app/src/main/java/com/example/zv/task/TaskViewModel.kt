package com.example.zv.task

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zv.data.database.AppDatabase
import com.example.zv.data.database.TaskEntity
import com.example.zv.data.database.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(context: Context) : ViewModel() {
    private val database = AppDatabase.getDatabase(context)
    private val taskDao = database.taskDao()
    
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    
    fun addTask(title: String, description: String, priority: TaskPriority) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                description = description,
                priority = priority
            )
            taskDao.insertTask(task)
        }
    }
    
    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
    
    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            taskDao.deleteTask(taskId)
        }
    }
    
    fun clearAllTasks() {
        viewModelScope.launch {
            taskDao.deleteAllTasks()
        }
    }
}

