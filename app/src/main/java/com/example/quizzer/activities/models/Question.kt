package com.example.quizzer.activities.models

data class Question(
    val desc: String = "",
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",
    val quizAnswer: String = "",
    var userAnswer: String = "",
    var isSelected: Boolean = false
)