package com.example.studenttaskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.studenttaskmanager.data.local.AppDatabase
import com.example.studenttaskmanager.data.local.entity.Subject
import com.example.studenttaskmanager.data.repository.SubjectRepository
import kotlinx.coroutines.launch

class SubjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SubjectRepository

    val allSubjects: LiveData<List<Subject>>

    init {
        val dao = AppDatabase.getDatabase(application).subjectDao()
        repository = SubjectRepository(dao)
        allSubjects = repository.allSubjects.asLiveData()
    }

    fun addSubject(name: String, color: String = "#3F51B5") = viewModelScope.launch {
        repository.insertSubject(Subject(subjectName = name, subjectColor = color))
    }

    fun updateSubject(subject: Subject) = viewModelScope.launch {
        repository.updateSubject(subject)
    }

    fun deleteSubject(subject: Subject) = viewModelScope.launch {
        repository.deleteSubject(subject)
    }
}
