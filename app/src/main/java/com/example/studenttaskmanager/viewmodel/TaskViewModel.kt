package com.example.studenttaskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.studenttaskmanager.data.local.AppDatabase
import com.example.studenttaskmanager.data.local.entity.Task
import com.example.studenttaskmanager.data.local.relation.TaskWithSubject
import com.example.studenttaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val allTasks: LiveData<List<TaskWithSubject>>

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        allTasks = repository.allTasksWithSubject.asLiveData()
    }

    fun getTasksBySubject(subjectId: Int): LiveData<List<TaskWithSubject>> {
        return repository.getTasksBySubject(subjectId).asLiveData()
    }

    fun addTask(
        title: String,
        description: String,
        dueDate: Long,
        subjectId: Int
    ) = viewModelScope.launch {
        repository.insertTask(
            Task(
                title = title,
                description = description,
                dueDate = dueDate,
                subjectId = subjectId
            )
        )
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) = viewModelScope.launch {
        repository.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return repository.getTaskById(taskId)
    }
}
