package com.example.quizzer.activities.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.example.quizzer.R
import com.example.quizzer.activities.models.Quiz
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private lateinit var quiz: Quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setUpView()
        supportActionBar?.title = "Result"
    }

    private fun setUpView() {
        val getResult = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(getResult, Quiz::class.java)
        calculateScore()
        setQuizAnswerView()
        setUserAnswerView()
    }

    private fun setUserAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.question.entries){
            val question = entry.value
            builder.append("<font color'#18206f'><b>Question: ${question.desc}</b></font><br/><br/>")
            builder.append("<font color'#8000ff'>Answer: ${question.userAnswer}</font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            textViewUserResult.text = Html.fromHtml(builder.toString(),Html.FROM_HTML_MODE_COMPACT)
        }else{
            textViewUserResult.text = Html.fromHtml(builder.toString())
        }
    }

    private fun setQuizAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.question.entries){
            val question = entry.value
            builder.append("<font color'#18206f'><b>Question: ${question.desc}</b></font><br/><br/>")
            builder.append("<font color'#8000ff'>Answer: ${question.quizAnswer}</font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            textViewQuizAnswer.text = Html.fromHtml(builder.toString(),Html.FROM_HTML_MODE_COMPACT)
        }else{
            textViewQuizAnswer.text = Html.fromHtml(builder.toString())
        }
    }

    private fun calculateScore() {
        var score = 0
        for (entry in quiz.question.entries){
            val question = entry.value
            if (question.quizAnswer == question.userAnswer) {
                score += 10
            }
        }
        buttonTryAgain.setOnClickListener {
            val nextQuiz = Intent(
                this,MainActivity::class.java)
            startActivity(nextQuiz)
            finish()
        }
        textViewScoreBoard.text = "Your Score : $score"
    }
}