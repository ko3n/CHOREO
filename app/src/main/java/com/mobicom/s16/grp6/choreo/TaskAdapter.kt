package com.mobicom.s16.grp6.choreo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val dbHelper: TaskDbHelper
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvDueDate: TextView = view.findViewById(R.id.tvDueDate)
        val tvPriority: TextView = view.findViewById(R.id.tvPriority)
        val tvAssignee: TextView = view.findViewById(R.id.tvAssignee)
        val tvRecurrence: TextView = view.findViewById(R.id.tvRecurrence)
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
        holder.tvTitle.text = task.title
        holder.tvDescription.text = task.description
        holder.tvDueDate.text = "Due: ${task.dueDate ?: "N/A"}"
        holder.tvPriority.text = "Priority: ${task.priority ?: "N/A"}"
        holder.tvAssignee.text = "Assignee: ${task.assignee ?: "N/A"}"
        holder.tvRecurrence.text = if(task.isRecurring) "Recurring: ${task.recurrenceInterval}" else "One-time"

        holder.btnDelete.setOnClickListener {
            dbHelper.deleteTask(task.id)
            updateTasks(dbHelper.getAllTasks())
        }
        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_task, null)
            // Fill in current values
            dialogView.findViewById<EditText>(R.id.etTitle).setText(task.title)
            dialogView.findViewById<EditText>(R.id.etDescription).setText(task.description)
            dialogView.findViewById<EditText>(R.id.etDueDate).setText(task.dueDate ?: "")
            dialogView.findViewById<EditText>(R.id.etPriority).setText(task.priority?.toString() ?: "")
            dialogView.findViewById<EditText>(R.id.etAssignee).setText(task.assignee ?: "")
            dialogView.findViewById<CheckBox>(R.id.cbRecurring).isChecked = task.isRecurring
            dialogView.findViewById<EditText>(R.id.etRecurrenceInterval).setText(task.recurrenceInterval ?: "")

            AlertDialog.Builder(context)
                .setTitle("Edit Task")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val title = dialogView.findViewById<EditText>(R.id.etTitle).text.toString()
                    val description = dialogView.findViewById<EditText>(R.id.etDescription).text.toString()
                    val dueDate = dialogView.findViewById<EditText>(R.id.etDueDate).text.toString()
                    val priorityStr = dialogView.findViewById<EditText>(R.id.etPriority).text.toString()
                    val priority = priorityStr.toIntOrNull()
                    val assignee = dialogView.findViewById<EditText>(R.id.etAssignee).text.toString()
                    val isRecurring = dialogView.findViewById<CheckBox>(R.id.cbRecurring).isChecked
                    val recurrenceInterval = dialogView.findViewById<EditText>(R.id.etRecurrenceInterval).text.toString()
                    dbHelper.editTask(task.id, title, description, dueDate, priority, assignee, isRecurring, recurrenceInterval)
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