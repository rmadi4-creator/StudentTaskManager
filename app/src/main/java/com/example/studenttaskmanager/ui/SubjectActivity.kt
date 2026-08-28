package com.example.studenttaskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.databinding.ActivitySubjectBinding
import com.example.studenttaskmanager.ui.adapter.SubjectAdapter
import com.example.studenttaskmanager.viewmodel.SubjectViewModel

class SubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectBinding
    private lateinit var subjectViewModel: SubjectViewModel
    private lateinit var adapter: SubjectAdapter

    // ألوان جاهزة يتم توزيعها تلقائيًا على المواد الجديدة
    private val colorPalette = listOf(
        "#3F51B5", "#E91E63", "#4CAF50", "#FF9800",
        "#9C27B0", "#00BCD4", "#795548", "#607D8B"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subjectViewModel = ViewModelProvider(this)[SubjectViewModel::class.java]

        setupRecyclerView()
        observeSubjects()

        binding.fabAddSubject.setOnClickListener {
            showAddSubjectDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = SubjectAdapter(
            onDeleteClick = { subject ->
                AlertDialog.Builder(this)
                    .setMessage(R.string.confirm_delete_subject)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        subjectViewModel.deleteSubject(subject)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        )
        binding.rvSubjects.layoutManager = LinearLayoutManager(this)
        binding.rvSubjects.adapter = adapter
    }

    private fun observeSubjects() {
        subjectViewModel.allSubjects.observe(this) { subjects ->
            adapter.submitList(subjects)
        }
    }

    private fun showAddSubjectDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null)
        val etName = dialogView.findViewById<EditText>(R.id.etSubjectName)

        AlertDialog.Builder(this)
            .setTitle(R.string.btn_add_subject)
            .setView(dialogView)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val name = etName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(this, R.string.error_subject_name_required, Toast.LENGTH_SHORT).show()
                } else {
                    val color = colorPalette.random()
                    subjectViewModel.addSubject(name, color)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
