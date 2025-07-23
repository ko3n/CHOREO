package com.mobicom.s16.grp6.choreo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val etTask = findViewById<EditText>(R.id.etTask)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            val taskText = etTask.text.toString()
            if (taskText.isNotBlank()) {
                dbHelper.addTask(taskText)
                adapter.updateTasks(dbHelper.getAllTasks())
                etTask.text.clear()
            }
        }
    }
}