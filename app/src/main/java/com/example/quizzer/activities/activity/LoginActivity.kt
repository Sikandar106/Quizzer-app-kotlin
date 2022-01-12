package com.example.quizzer.activities.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizzer.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var logInAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logInAuth  = FirebaseAuth.getInstance()
        button.setOnClickListener { validationLoginEmailAndPassword() }
        textViewSignUp.setOnClickListener { moveToSignUp() }
    }


    private fun validationLoginEmailAndPassword() {
        val getEmail = editTextTextLoginEmail.text.toString()
        val getPassword = editTextLoginPassword.text.toString()
        if (getEmail.isEmpty() || getPassword.isEmpty()) {
            Toast.makeText(this, "Fields Cant be Empty", Toast.LENGTH_SHORT).show()
        } else {
            logInAuth.signInWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                        Toast.makeText(this, "$getEmail Succesfully logged In", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Create An Account", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun moveToSignUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}