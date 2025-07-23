package com.mobicom.s16.grp6.choreo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDbHelper(context: Context) :
    SQLiteOpenHelper(context, "tasks.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL)")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun addTask(description: String) {
        val db = writableDatabase
        val values = ContentValues().apply { put("description", description) }
        db.insert("tasks", null, values)
        db.close()
    }

    fun deleteTask(id: Int) {
        val db = writableDatabase
        db.delete("tasks", "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun editTask(id: Int, newDescription: String) {
        val db = writableDatabase
        val values = ContentValues().apply { put("description", newDescription) }
        db.update("tasks", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        val tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            tasks.add(Task(id, desc))
        }
        cursor.close()
        db.close()
        return tasks
    }
}