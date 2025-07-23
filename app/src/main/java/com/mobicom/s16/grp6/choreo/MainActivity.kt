package com.mobicom.s16.grp6.choreo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.CheckBox

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: TaskDbHelper
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDbHelper(this)
        adapter = TaskAdapter(dbHelper.getAllTasks(), dbHelper)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_task, null)
            AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val title = dialogView.findViewById<EditText>(R.id.etTitle).text.toString()
                    val description = dialogView.findViewById<EditText>(R.id.etDescription).text.toString()
                    val dueDate = dialogView.findViewById<EditText>(R.id.etDueDate).text.toString()
                    val priorityStr = dialogView.findViewById<EditText>(R.id.etPriority).text.toString()
                    val priority = priorityStr.toIntOrNull()
                    val assignee = dialogView.findViewById<EditText>(R.id.etAssignee).text.toString()
                    val isRecurring = dialogView.findViewById<CheckBox>(R.id.cbRecurring).isChecked
                    val recurrenceInterval = dialogView.findViewById<EditText>(R.id.etRecurrenceInterval).text.toString()
                    dbHelper.addTask(title, description, dueDate, priority, assignee, isRecurring, recurrenceInterval)
                    adapter.updateTasks(dbHelper.getAllTasks())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}