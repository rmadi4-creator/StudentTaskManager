package com.example.studenttaskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.local.entity.Subject
import com.example.studenttaskmanager.databinding.ActivityAddEditTaskBinding
import com.example.studenttaskmanager.viewmodel.SubjectViewModel
import com.example.studenttaskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * تُستخدم هذه الشاشة لكل من إضافة مهمة جديدة وتعديل مهمة موجودة.
 * إذا تم تمرير taskId عبر Intent -> وضع التعديل، وإلا -> وضع الإضافة.
 */
class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var subjectViewModel: SubjectViewModel

    private var subjectsList: List<Subject> = emptyList()
    private var selectedDueDate: Long = System.currentTimeMillis()
    private var editingTaskId: Int = -1
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        subjectViewModel = ViewModelProvider(this)[SubjectViewModel::class.java]

        editingTaskId = intent.getIntExtra("taskId", -1)
        supportActionBar?.title = if (editingTaskId == -1) {
            getString(R.string.add_task)
        } else {
            getString(R.string.edit_task)
        }

        binding.tvSelectedDate.text = dateFormat.format(selectedDueDate)

        setupSubjectSpinner()
        setupDatePicker()
        setupSaveButton()

        if (editingTaskId != -1) {
            loadTaskForEditing()
        }
    }

    private fun setupSubjectSpinner() {
        subjectViewModel.allSubjects.observe(this) { subjects ->
            subjectsList = subjects
            if (subjects.isEmpty()) {
                Toast.makeText(this, R.string.error_no_subjects, Toast.LENGTH_LONG).show()
            }
            val names = subjects.map { it.subjectName }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSubject.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDueDate

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)
                    selectedDueDate = cal.timeInMillis
                    binding.tvSelectedDate.text = dateFormat.format(selectedDueDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun loadTaskForEditing() {
        lifecycleScope.launch {
            val task = taskViewModel.getTaskById(editingTaskId)
            task?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
                selectedDueDate = it.dueDate
                binding.tvSelectedDate.text = dateFormat.format(selectedDueDate)

                // تحديد المادة الصحيحة في الـ Spinner بعد تحميل القائمة
                subjectViewModel.allSubjects.observe(this@AddEditTaskActivity) { subjects ->
                    val index = subjects.indexOfFirst { s -> s.subjectId == it.subjectId }
                    if (index >= 0) binding.spinnerSubject.setSelection(index)
                }
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, R.string.error_title_required, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (subjectsList.isEmpty()) {
                Toast.makeText(this, R.string.error_subject_required, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedSubject = subjectsList[binding.spinnerSubject.selectedItemPosition]

            if (editingTaskId == -1) {
                taskViewModel.addTask(title, description, selectedDueDate, selectedSubject.subjectId)
            } else {
                lifecycleScope.launch {
                    val existingTask = taskViewModel.getTaskById(editingTaskId)
                    existingTask?.let {
                        taskViewModel.updateTask(
                            it.copy(
                                title = title,
                                description = description,
                                dueDate = selectedDueDate,
                                subjectId = selectedSubject.subjectId
                            )
                        )
                    }
                }
            }
            finish()
        }
    }
}
