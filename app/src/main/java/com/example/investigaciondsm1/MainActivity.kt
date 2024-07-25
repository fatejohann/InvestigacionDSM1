package com.example.investigaciondsm1

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskList: MutableList<Task>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dbHelper: TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDatabaseHelper(this)

        taskList = mutableListOf()
        taskAdapter = TaskAdapter(taskList, this::deleteTask, this::toggleTaskCompletion)

        findViewById<RecyclerView>(R.id.recyclerViewTasks).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            showAddTaskDialog()
        }

        loadTasks()
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val taskDescriptionEditText = dialogView.findViewById<EditText>(R.id.etTaskDescription)

        AlertDialog.Builder(this)
            .setTitle("Nueva Tarea")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val taskDescription = taskDescriptionEditText.text.toString()
                if (taskDescription.isNotEmpty()) {
                    val newTask = Task(description = taskDescription, isCompleted = false)
                    dbHelper.addTask(newTask)
                    taskList.add(newTask)
                    taskAdapter.notifyItemInserted(taskList.size - 1)
                    toggleNoTasksMessage()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun loadTasks() {
        taskList.clear()
        taskList.addAll(dbHelper.getTasks())
        taskAdapter.notifyDataSetChanged()
        toggleNoTasksMessage()
    }

    private fun toggleNoTasksMessage() {
        val noTasksMessage = findViewById<TextView>(R.id.tvNoTasks)
        if (taskList.isEmpty()) {
            noTasksMessage.visibility = View.VISIBLE
        } else {
            noTasksMessage.visibility = View.GONE
        }
    }

    private fun deleteTask(task: Task) {
        dbHelper.deleteTask(task)
        val position = taskList.indexOf(task)
        taskList.remove(task)
        taskAdapter.notifyItemRemoved(position)
        toggleNoTasksMessage()
    }

    private fun toggleTaskCompletion(task: Task) {
        // Actualiza el estado de la tarea en la base de datos
        dbHelper.updateTask(task)

        // Usa un Handler para postergar la actualización del adaptador
        val handler = Handler(mainLooper)
        handler.post {
            taskAdapter.notifyDataSetChanged()
        }
    }

}
