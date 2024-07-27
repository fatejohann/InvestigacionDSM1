package com.example.investigaciondsm1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<Task>,
    private val deleteTask: (Task) -> Unit,
    private val toggleTaskCompletion: (Task) -> Unit,
    private val editTask: (Task) -> Unit // Agregado
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount() = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbTaskCompleted: CheckBox = itemView.findViewById(R.id.cbTaskCompleted)
        private val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val ivEditTask: ImageView = itemView.findViewById(R.id.ivEditTask)
        private val ivDeleteTask: ImageView = itemView.findViewById(R.id.ivDeleteTask)

        init {
            cbTaskCompleted.setOnCheckedChangeListener { _, isChecked ->
                val task = tasks[adapterPosition]
                task.isCompleted = isChecked
                toggleTaskCompletion(task)
            }

            ivDeleteTask.setOnClickListener {
                val task = tasks[adapterPosition]
                deleteTask(task)
            }

            ivEditTask.setOnClickListener {
                val task = tasks[adapterPosition]
                editTask(task)
            }
        }

        fun bind(task: Task) {
            cbTaskCompleted.isChecked = task.isCompleted
            tvTaskDescription.text = task.description
            tvTaskDescription.paint.isStrikeThruText = task.isCompleted
        }
    }
}