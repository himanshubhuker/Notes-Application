package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class editnoteactivity : AppCompatActivity() {
    private lateinit var data: Intent
    private lateinit var medittitleofnote:EditText
    private lateinit var meditcontentofnote: EditText
    private lateinit var msaveeditnote: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editnoteactivity)
        medittitleofnote=findViewById(R.id.edittitleofnote)
        meditcontentofnote=findViewById(R.id.editcontentofnote)
        msaveeditnote=findViewById(R.id.saveeditnote)
        data=intent
        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseUser= FirebaseAuth.getInstance().currentUser!!

        var toolbar: Toolbar=findViewById(R.id.toolbarofeditnote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        msaveeditnote.setOnClickListener{v->
            //Toast.makeText(applicationContext,"Save Button cliclked",Toast.LENGTH_SHORT).show()
            var newtitle:String=medittitleofnote.text.toString()
            var newcontent:String=meditcontentofnote.text.toString()

            if(newtitle.isEmpty() || newcontent.isEmpty()){
                Toast.makeText(applicationContext,"Some fields are empty ", Toast.LENGTH_SHORT).show()
            }
            else{
                var documentReference: DocumentReference=firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document(data.getStringExtra("noteId")?: "")
                val note = HashMap<String, Any>()
                note["title"] = newtitle
                note["content"] = newcontent
                documentReference.set(note).addOnSuccessListener{
                    Toast.makeText(applicationContext,"Note Updated Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@editnoteactivity, notesActivity::class.java))
                }.addOnFailureListener{
                    Toast.makeText(applicationContext,"Failed to Update ",Toast.LENGTH_SHORT).show()
                }
            }


        }

        val noteTitle: String? = data.getStringExtra("title")
        val notecontent: String?=data.getStringExtra("content")
        meditcontentofnote.setText(notecontent)
        medittitleofnote.setText(noteTitle)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}