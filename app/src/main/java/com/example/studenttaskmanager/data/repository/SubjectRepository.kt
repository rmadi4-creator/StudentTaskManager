package com.example.studenttaskmanager.data.repository

import com.example.studenttaskmanager.data.local.dao.SubjectDao
import com.example.studenttaskmanager.data.local.entity.Subject
import kotlinx.coroutines.flow.Flow

/**
 * Repository يفصل طبقة البيانات (Room) عن ViewModel
 */
class SubjectRepository(private val subjectDao: SubjectDao) {

    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects()

    suspend fun insertSubject(subject: Subject): Long {
        return subjectDao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject) {
        subjectDao.deleteSubject(subject)
    }

    suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId)
    }

    suspend fun getSubjectsCount(): Int {
        return subjectDao.getSubjectsCount()
    }
}
