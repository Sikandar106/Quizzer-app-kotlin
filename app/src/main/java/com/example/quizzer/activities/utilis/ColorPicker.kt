package com.example.quizzer.activities.utilis

object ColorPicker {
    val color = arrayOf(
        "#F08080",
        "#ADD8E6",
        "#20B2AA",
        "#32CD32",
        "#7B68EE",
        "#4B0082",
        "#FF69B4",
        "#D3D3D3",
        "#778899",
        "#FF69B4",
        "#7B68EE",
        "#20B2AA"
    )
    var colorIndex = 0
    fun getColor(): String {
        colorIndex = (colorIndex + 1) % color.size
        return color[colorIndex]
    }
}