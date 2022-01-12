package com.example.quizzer.activities.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzer.R
import com.example.quizzer.activities.adapters.OptionAdapter
import com.example.quizzer.activities.models.Question
import com.example.quizzer.activities.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {
    private var quizzes: MutableList<Quiz>? = null
    private var question: MutableMap<String, Question>? = null
    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        setUpFireStore()
        setUpEventListeners()
        supportActionBar?.title = "Question"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    //click event for next or previous question
    private fun setUpEventListeners() {
        buttonNextQuestion.setOnClickListener {
            if (quizzes.isNullOrEmpty()){
                Toast.makeText(this, "select option", Toast.LENGTH_SHORT).show()
            }else {
                index++
                optionBindViews()
            }
        }
        buttonPreviousQuestion.setOnClickListener {
            index--
            optionBindViews()
        }
        buttonSubmitQuestion.setOnClickListener {
            Log.d("quizResult", question.toString())
            val intent = Intent(this, ResultActivity::class.java)
            val json = Gson().toJson(quizzes?.get(0))
            intent.putExtra("QUIZ", json)
            startActivity(intent)
            finish()
        }
    }

    //fetching quiz data from firebase
    private fun setUpFireStore() {
        val db = FirebaseFirestore.getInstance()
        val date = intent.getStringExtra("DATE")
        if (date != null) {
            db.collection("quizzess").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {
                        quizzes = it.toObjects(Quiz::class.java)
                        question = quizzes!![0].question
                        optionBindViews()
                    }
                }
        }
    }

    //binding data to the views
    private fun optionBindViews() {
        buttonNextQuestion.visibility = View.GONE
        buttonPreviousQuestion.visibility = View.GONE
        buttonSubmitQuestion.visibility = View.GONE

        if (index == 1) {
            //at the start
            buttonNextQuestion.visibility = View.VISIBLE
        } else if (index == question!!.size) {
            //at the end
            buttonSubmitQuestion.visibility = View.VISIBLE
            buttonPreviousQuestion.visibility = View.VISIBLE
        } else {//in the middle
            buttonPreviousQuestion.visibility = View.VISIBLE
            buttonNextQuestion.visibility = View.VISIBLE
        }

        val question = question!!["question$index"]
        question?.let {
            textViewQuestion.text = it.desc
            val optionAdapter = OptionAdapter(this, it)
            //attaching option items to the question recyclerView
            recyclerViewQuestion.layoutManager = LinearLayoutManager(this)
            recyclerViewQuestion.adapter = optionAdapter
            recyclerViewQuestion.setHasFixedSize(true)
        }
    }
}