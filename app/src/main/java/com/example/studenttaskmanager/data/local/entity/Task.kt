package com.example.studenttaskmanager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * جدول المهام (tasks)
 * كل مهمة ترتبط بمادة دراسية واحدة عبر subjectId (Foreign Key)
 * عند حذف المادة، تُحذف مهامها تلقائيًا (CASCADE)
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    val taskId: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "dueDate")
    val dueDate: Long,

    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "subjectId")
    val subjectId: Int,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)
