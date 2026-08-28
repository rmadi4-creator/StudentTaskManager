package com.example.studenttaskmanager.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.studenttaskmanager.data.local.entity.Subject
import com.example.studenttaskmanager.data.local.entity.Task

/**
 * كلاس مساعد لعرض المهمة مع بيانات المادة المرتبطة بها
 * يُستخدم في شاشة العرض الرئيسية لإظهار اسم ولون المادة بجانب كل مهمة
 */
data class TaskWithSubject(
    @Embedded
    val task: Task,

    @Relation(
        parentColumn = "subjectId",
        entityColumn = "subjectId"
    )
    val subject: Subject
)
