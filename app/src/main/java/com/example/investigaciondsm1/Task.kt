package com.example.investigaciondsm1

data class Task(
    val id: Long = -1,
    val description: String,
    var isCompleted: Boolean
)