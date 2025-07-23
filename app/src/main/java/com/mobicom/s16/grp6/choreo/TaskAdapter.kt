package com.mobicom.s16.grp6.choreo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val dbHelper: TaskDbHelper
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTask: TextView = view.findViewById(R.id.tvTask)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTask.text = task.description

        holder.btnDelete.setOnClickListener {
            dbHelper.deleteTask(task.id)
            updateTasks(dbHelper.getAllTasks())
        }
        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val editText = android.widget.EditText(context)
            editText.setText(task.description)
            AlertDialog.Builder(context)
                .setTitle("Edit Task")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    dbHelper.editTask(task.id, editText.text.toString())
                    updateTasks(dbHelper.getAllTasks())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount() = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}