package com.mobicom.s16.grp6.choreo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDbHelper(context: Context) :
    SQLiteOpenHelper(context, "tasks.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                dueDate TEXT,
                priority INTEGER,
                assignee TEXT,
                isRecurring INTEGER,
                recurrenceInterval TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun addTask(
        title: String,
        description: String,
        dueDate: String?,
        priority: Int?,
        assignee: String?,
        isRecurring: Boolean,
        recurrenceInterval: String?
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("dueDate", dueDate)
            put("priority", priority)
            put("assignee", assignee)
            put("isRecurring", if (isRecurring) 1 else 0)
            put("recurrenceInterval", recurrenceInterval)
        }
        db.insert("tasks", null, values)
        db.close()
    }

    fun editTask(
        id: Int,
        title: String,
        description: String,
        dueDate: String?,
        priority: Int?,
        assignee: String?,
        isRecurring: Boolean,
        recurrenceInterval: String?
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("dueDate", dueDate)
            put("priority", priority)
            put("assignee", assignee)
            put("isRecurring", if (isRecurring) 1 else 0)
            put("recurrenceInterval", recurrenceInterval)
        }
        db.update("tasks", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteTask(id: Int) {
        val db = writableDatabase
        db.delete("tasks", "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        val tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val dueDate = cursor.getString(cursor.getColumnIndexOrThrow("dueDate"))
            val priority = if (!cursor.isNull(cursor.getColumnIndexOrThrow("priority"))) cursor.getInt(cursor.getColumnIndexOrThrow("priority")) else null
            val assignee = cursor.getString(cursor.getColumnIndexOrThrow("assignee"))
            val isRecurring = cursor.getInt(cursor.getColumnIndexOrThrow("isRecurring")) == 1
            val recurrenceInterval = cursor.getString(cursor.getColumnIndexOrThrow("recurrenceInterval"))
            tasks.add(Task(id, title, desc, dueDate, priority, assignee, isRecurring, recurrenceInterval))
        }
        cursor.close()
        db.close()
        return tasks
    }
}