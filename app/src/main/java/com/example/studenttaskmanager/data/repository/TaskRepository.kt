package com.example.studenttaskmanager.data.repository

import com.example.studenttaskmanager.data.local.dao.TaskDao
import com.example.studenttaskmanager.data.local.entity.Task
import com.example.studenttaskmanager.data.local.relation.TaskWithSubject
import kotlinx.coroutines.flow.Flow

/**
 * Repository يفصل طبقة البيانات (Room) عن ViewModel
 */
class TaskRepository(private val taskDao: TaskDao) {

    val allTasksWithSubject: Flow<List<TaskWithSubject>> = taskDao.getAllTasksWithSubject()

    fun getTasksBySubject(subjectId: Int): Flow<List<TaskWithSubject>> {
        return taskDao.getTasksBySubject(subjectId)
    }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun getCompletedCount(): Int = taskDao.getCompletedCount()

    suspend fun getTotalCount(): Int = taskDao.getTotalCount()
}
