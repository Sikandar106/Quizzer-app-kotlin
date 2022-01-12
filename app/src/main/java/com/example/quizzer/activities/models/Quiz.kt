package com.example.quizzer.activities.models

data class Quiz(
    val id: String = "",
    val title: String = "",
    val question: MutableMap<String,Question> = mutableMapOf()
)