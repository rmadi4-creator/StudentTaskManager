package com.example.studenttaskmanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.local.relation.TaskWithSubject
import com.example.studenttaskmanager.databinding.ActivityMainBinding
import com.example.studenttaskmanager.ui.adapter.TaskAdapter
import com.example.studenttaskmanager.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setupRecyclerView()
        observeTasks()

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            onItemClick = { taskWithSubject ->
                val intent = Intent(this, TaskDetailsActivity::class.java)
                intent.putExtra("taskId", taskWithSubject.task.taskId)
                startActivity(intent)
            },
            onCheckChanged = { taskWithSubject, isChecked ->
                taskViewModel.toggleTaskCompletion(taskWithSubject.task.taskId, isChecked)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun observeTasks() {
        taskViewModel.allTasks.observe(this) { tasks: List<TaskWithSubject> ->
            adapter.submitList(tasks)
            binding.tvEmpty.visibility = if (tasks.isEmpty()) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_manage_subjects -> {
                startActivity(Intent(this, SubjectActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
