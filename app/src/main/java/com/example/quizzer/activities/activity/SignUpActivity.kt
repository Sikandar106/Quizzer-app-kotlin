package com.example.quizzer.activities.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizzer.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpAuth = FirebaseAuth.getInstance()
        textViewLogIn.setOnClickListener { moveToLogIn() }
        buttonSignUp.setOnClickListener { signUpUser() }
    }


    private fun moveToLogIn(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUpUser(){
        val signUpEmail = editTextSignUpEmail.text.toString()
        val signUpPassword = editTextSignUpPassword.text.toString()
        val signUpConfirmPassword = editTextSignUpPasswordConfirm.text.toString()

        if (signUpEmail.isBlank() || signUpPassword.isBlank()){
            Toast.makeText(this, "Field Cant be Empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (signUpPassword != signUpConfirmPassword){
            Toast.makeText(this, "Password Cant Match", Toast.LENGTH_SHORT).show()
            return
        }
        signUpAuth.createUserWithEmailAndPassword(signUpEmail,signUpPassword).addOnCompleteListener(this ) {
            if (it.isSuccessful){
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(this, "error creating user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}