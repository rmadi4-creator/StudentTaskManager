package com.example.studenttaskmanager.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.data.local.entity.Subject

class SubjectAdapter(
    private val onDeleteClick: (Subject) -> Unit
) : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(DIFF_CALLBACK) {

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewColor: View = itemView.findViewById(R.id.viewColor)
        val tvName: TextView = itemView.findViewById(R.id.tvSubjectName)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = getItem(position)
        holder.tvName.text = subject.subjectName
        try {
            holder.viewColor.background.setTint(Color.parseColor(subject.subjectColor))
        } catch (e: Exception) {
            // تجاهل إذا كان اللون غير صالح
        }
        holder.btnDelete.setOnClickListener { onDeleteClick(subject) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem.subjectId == newItem.subjectId
            }

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem == newItem
            }
        }
    }
}
