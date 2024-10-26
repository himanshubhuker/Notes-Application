package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.lang.Exception

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var mCreateTitleOfNote: EditText
    private lateinit var mCreateContentOfNote: EditText
    private lateinit var mSaveNote: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var documentReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createnote)

        mSaveNote = findViewById(R.id.savenote)
        mCreateTitleOfNote = findViewById(R.id.createtitleofnote)
        mCreateContentOfNote = findViewById(R.id.createcontentofnote)
        val toolbar: Toolbar = findViewById(R.id.toolbarofcreatenote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize Firebase services
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Check if the user is authenticated, otherwise prompt a message
        firebaseUser = firebaseAuth.currentUser ?: run {
            Toast.makeText(this, "Authentication Error. Please log in again.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Handle Save Note button click
        mSaveNote.setOnClickListener {
            val title = mCreateTitleOfNote.text.toString().trim()
            val content = mCreateContentOfNote.text.toString().trim()

            // Validate input fields
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(applicationContext, "Both fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveNoteToFirebase(title, content)
        }
    }

    // Method to save note to Firebase Firestore with error handling
    private fun saveNoteToFirebase(title: String, content: String) {
        // Get document reference for the user's notes
        documentReference = firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document()

        val note = mutableMapOf<String, Any>(
            "title" to title,
            "content" to content
        )

        // Attempt to save the note and handle possible errors
        documentReference.set(note).addOnSuccessListener {
            Toast.makeText(applicationContext, "Note created successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@CreateNoteActivity, notesActivity::class.java))
        }.addOnFailureListener { exception ->
            // Handle Firestore-specific errors
            when (exception) {
                is FirebaseFirestoreException -> {
                    Toast.makeText(applicationContext, "Firestore error: ${exception.message}", Toast.LENGTH_LONG).show()
                }
                is Exception -> {
                    Toast.makeText(applicationContext, "Error creating note: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Handle the back button in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
