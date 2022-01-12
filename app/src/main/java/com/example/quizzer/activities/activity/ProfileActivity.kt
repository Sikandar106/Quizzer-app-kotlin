package com.example.quizzer.activities.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.quizzer.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        firebaseAuth = FirebaseAuth.getInstance()
        textViewProfileEmail.text = firebaseAuth.currentUser?.email
        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializer()
    }

    private fun initializer() {
        setListeners()
    }

    private fun setListeners() {
        buttonLogOut.setOnClickListener(this)
        textViewBirthday.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            buttonLogOut.id -> {
                    val toLogin = Intent(this,LoginActivity::class.java)
                    startActivity(toLogin)
                    firebaseAuth.signOut()
                    finish()
            }
            textViewBirthday.id -> {

            }
        }
    }
}