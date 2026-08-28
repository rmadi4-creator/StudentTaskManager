package com.example.studenttaskmanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.local.AppDatabase
import com.example.studenttaskmanager.databinding.ActivityTaskDetailsBinding
import com.example.studenttaskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskId = intent.getIntExtra("taskId", -1)

        loadTaskDetails()

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            intent.putExtra("taskId", taskId)
            startActivity(intent)
            finish()
        }

        binding.btnDelete.setOnClickListener {
            confirmDelete()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTaskDetails()
    }

    private fun loadTaskDetails() {
        lifecycleScope.launch {
            val task = taskViewModel.getTaskById(taskId)
            task?.let {
                val subject = AppDatabase.getDatabase(applicationContext)
                    .subjectDao().getSubjectById(it.subjectId)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                binding.tvTitle.text = it.title
                binding.tvSubject.text = "المادة: ${subject?.subjectName ?: ""}"
                binding.tvDueDate.text = "تاريخ الاستحقاق: ${dateFormat.format(it.dueDate)}"
                binding.tvStatus.text = "الحالة: " + if (it.isCompleted) {
                    getString(R.string.status_completed)
                } else {
                    getString(R.string.status_pending)
                }
                binding.tvDescription.text = it.description.ifEmpty { "لا يوجد وصف" }
            }
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setMessage(R.string.confirm_delete_task)
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch {
                    val task = taskViewModel.getTaskById(taskId)
                    task?.let {
                        taskViewModel.deleteTask(it)
                        finish()
                    }
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
