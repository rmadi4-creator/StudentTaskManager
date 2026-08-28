package com.example.studenttaskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studenttaskmanager.data.local.entity.Task
import com.example.studenttaskmanager.data.local.relation.TaskWithSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    // جلب جميع المهام مع بيانات المادة المرتبطة بها، مرتبة حسب تاريخ الاستحقاق
    @Transaction
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasksWithSubject(): Flow<List<TaskWithSubject>>

    // جلب المهام الخاصة بمادة معينة فقط
    @Transaction
    @Query("SELECT * FROM tasks WHERE subjectId = :subjectId ORDER BY dueDate ASC")
    fun getTasksBySubject(subjectId: Int): Flow<List<TaskWithSubject>>

    // تحديث حالة الإنجاز فقط
    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE taskId = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedCount(): Int

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTotalCount(): Int
}
