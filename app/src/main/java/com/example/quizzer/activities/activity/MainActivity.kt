package com.example.quizzer.activities.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.quizzer.R
import com.example.quizzer.activities.adapters.QuizAdapter
import com.example.quizzer.activities.models.Quiz
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val IMAGE_CODE = 101
    private lateinit var adapter: QuizAdapter
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var db: FirebaseFirestore
    private val quizList = mutableListOf<Quiz>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpDatePicker()
        setUpViews()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpDatePicker() {
        buttonDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "Data picker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DatePicker", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-mm-yyyy")
                val date = dateFormatter.format(Date(it))
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)

            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DatePicker", datePicker.headerText)
            }
            datePicker.addOnCancelListener {
                Log.d("DatePicker", "Cancel called")
            }
        }
    }

    private fun setUpViews() {
        setUpDrawerLayout()
        setRecyclerView()
        setFireStoreFireBase()
        updateDrawerHeader()
    }

    private fun setFireStoreFireBase() {
        db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("quizzess")
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "error fetching data", Toast.LENGTH_SHORT).show()
            }
            Log.d("DATA", value?.toObjects(Quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value!!.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        recyclerViewQuiz.layoutManager = GridLayoutManager(this, 2)
        recyclerViewQuiz.adapter = adapter
    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(topAppBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, mainDrawer,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()

        navigationDrawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuItemProfile -> {
                    val profileIntent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    return@setNavigationItemSelectedListener true
                }
                R.id.menuItemFollowUs -> {
                    Toast.makeText(this@MainActivity, "Coming soon!", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                R.id.menuItemRateUs -> {
                    Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun updateDrawerHeader() {
        val userAuth = FirebaseAuth.getInstance().currentUser
        val navView: NavigationView = findViewById(R.id.navigationDrawer)
        val navHeaderView = navView.getHeaderView(0)
        val setProfilePic = navHeaderView.imageViewProfilePic
        setProfilePic.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePicture.resolveActivity(packageManager) != null) {
                startActivityForResult(takePicture, IMAGE_CODE)
            }
        }
        if (userAuth?.photoUrl != null) {
            Glide.with(this).load(userAuth.photoUrl).into(setProfilePic)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                val getBitmap = data?.extras?.get("data")
                imageViewProfilePic.setImageBitmap(getBitmap as Bitmap?)
                if (getBitmap != null) {
                    uploadImage(getBitmap)
                    Toast.makeText(this, "Profile Picture is Uploaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

   private fun uploadImage(bitmap: Bitmap) {
        val bao = ByteArrayOutputStream()
        val getUid = FirebaseAuth.getInstance().currentUser?.uid
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
        val storageRef =
            FirebaseStorage.getInstance().reference.child("ProfileImages").child("$getUid .jpeg")
        storageRef.putBytes(bao.toByteArray()).addOnSuccessListener { getDownloadUrl(storageRef) }
            .addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    "Faill to upload ",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("onSuccess", uri.toString())
            setUserProfileImage(uri)
        }
    }

    private fun setUserProfileImage(uri: Uri?) {
        val getUserAuth = FirebaseAuth.getInstance().currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        getUserAuth?.updateProfile(profileChangeRequest)?.addOnSuccessListener {
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(
                this@MainActivity,
                "profile image failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}