package com.example.quizzer.activities.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizzer.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_into.*

class IntoActivity : AppCompatActivity() {
    lateinit var getAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_into)
        getAuth = FirebaseAuth.getInstance()
        if (getAuth.currentUser != null){
            Toast.makeText(this, "User is already Logged In", Toast.LENGTH_SHORT).show()
            redirect("MAIN")
            finish()
        }
        buttonGetStarted.setOnClickListener {
            redirect("LOGIN")
            finish()
        }
    }

    private fun redirect(name: String){
        val intent = when(name){
            "LOGIN" -> Intent(this, LoginActivity::class.java)
            "MAIN" -> Intent(this, MainActivity::class.java)
            else -> throw Exception("no path exist ")
        }
        startActivity(intent)
    }
}