package com.example.investigaciondsm1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "tasks.db"
        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TASKS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_COMPLETED INTEGER)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }
    // Método para agregar una tarea
    fun addTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    // Método para obtener todas las tareas
    fun getTasks(): List<Task> {
        val db = readableDatabase
        val tasks = mutableListOf<Task>()
        val cursor = db.query(
            TABLE_TASKS,
            arrayOf(COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_COMPLETED),
            null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val completed = getInt(getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                tasks.add(Task(id, description, completed))
            }
            close()
        }
        db.close()
        return tasks
    }

    // Método para actualizar una tarea
    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
    }

    // Método para eliminar una tarea
    fun deleteTask(task: Task) {
        val db = writableDatabase
        db.delete(
            TABLE_TASKS,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
    }
}