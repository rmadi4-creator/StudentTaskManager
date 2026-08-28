package com.example.studenttaskmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studenttaskmanager.data.local.dao.SubjectDao
import com.example.studenttaskmanager.data.local.dao.TaskDao
import com.example.studenttaskmanager.data.local.entity.Subject
import com.example.studenttaskmanager.data.local.entity.Task

@Database(
    entities = [Subject::class, Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Singleton Pattern لضمان وجود نسخة واحدة فقط من قاعدة البيانات
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_task_manager_db"
                )
                    .fallbackToDestructiveMigration() // مقبول لمشروع أكاديمي بسيط
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
