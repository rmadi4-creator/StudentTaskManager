package com.example.studenttaskmanager.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.local.relation.TaskWithSubject
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val onItemClick: (TaskWithSubject) -> Unit,
    private val onCheckChanged: (TaskWithSubject, Boolean) -> Unit
) : ListAdapter<TaskWithSubject, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    inner class TaskViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val viewColor: android.view.View = itemView.findViewById(R.id.viewSubjectColor)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubjectName)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        holder.tvTitle.text = item.task.title
        holder.tvSubject.text = item.subject.subjectName
        holder.tvDueDate.text = "تاريخ التسليم: ${dateFormat.format(item.task.dueDate)}"

        try {
            holder.viewColor.background.setTint(Color.parseColor(item.subject.subjectColor))
        } catch (e: Exception) {
            // لون افتراضي إذا فشل التحويل
        }

        // إزالة المستمع مؤقتًا لتجنب استدعاءات غير مرغوبة أثناء إعادة تدوير العناصر
        holder.cbCompleted.setOnCheckedChangeListener(null)
        holder.cbCompleted.isChecked = item.task.isCompleted
        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            onCheckChanged(item, isChecked)
        }

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskWithSubject>() {
            override fun areItemsTheSame(oldItem: TaskWithSubject, newItem: TaskWithSubject): Boolean {
                return oldItem.task.taskId == newItem.task.taskId
            }

            override fun areContentsTheSame(oldItem: TaskWithSubject, newItem: TaskWithSubject): Boolean {
                return oldItem == newItem
            }
        }
    }
}
