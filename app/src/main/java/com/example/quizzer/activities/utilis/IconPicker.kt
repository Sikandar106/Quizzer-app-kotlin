package com.example.quizzer.activities.utilis

import com.example.quizzer.R

object IconPicker {
    val icons = arrayOf(
        R.drawable.ic_1,
        R.drawable.ic_2,
        R.drawable.ic_3,
        R.drawable.ic_4,
        R.drawable.ic_5,
        R.drawable.ic_6,
        R.drawable.ic_7
    )
    var iconPosition = 0
    fun getIcon(): Int {
        iconPosition = (iconPosition + 1) % icons.size
        return icons[iconPosition]
    }
}