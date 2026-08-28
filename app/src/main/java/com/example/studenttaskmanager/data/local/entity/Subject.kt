package com.example.studenttaskmanager.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * جدول المواد الدراسية (subjects)
 * كل مادة يمكن أن ترتبط بها عدة مهام (One-to-Many مع Task)
 */
@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subjectId")
    val subjectId: Int = 0,

    @ColumnInfo(name = "subjectName")
    val subjectName: String,

    @ColumnInfo(name = "subjectColor")
    val subjectColor: String = "#3F51B5"
)
