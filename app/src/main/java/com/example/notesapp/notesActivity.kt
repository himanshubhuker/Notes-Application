package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.random.Random

class notesActivity : AppCompatActivity() {

    private lateinit var mCreateNotesFab: FloatingActionButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var noteAdapter: FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)



        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Notes"

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Set up floating action button to create notes
        mCreateNotesFab = findViewById(R.id.createnotefab)
        mCreateNotesFab.setOnClickListener {
            startActivity(Intent(this@notesActivity, CreateNoteActivity::class.java))
        }

        // Set up RecyclerView
        mRecyclerView = findViewById(R.id.recyclerview)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = staggeredGridLayoutManager

        // Set up Firestore query and adapter
        val query = firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").orderBy("title", Query.Direction.ASCENDING)

        val allUserNotes = FirestoreRecyclerOptions.Builder< firebasemodel>().setQuery(query, firebasemodel::class.java).build()

        noteAdapter = object : FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUserNotes) {
            override fun onBindViewHolder(holder: NoteViewHolder, i: Int, firebasemodel: firebasemodel) {

                var popupbutton: ImageView=holder.itemView.findViewById(R.id.menupopbutton)
                var colourcode=getRandomcolor()
                holder.mnote.setBackgroundColor(holder.itemView.resources.getColor(colourcode,null))
                holder.notetitle.text = firebasemodel.title
                holder.notecontent.text = firebasemodel.content

                var docid:String=noteAdapter.snapshots.getSnapshot(i).id


                holder.itemView.setOnClickListener{ v ->

                    val intent = Intent(v.context, notedetails::class.java)
                    intent.putExtra("title", firebasemodel.title)
                    intent.putExtra("content", firebasemodel.content)
                    intent.putExtra("noteId",docid)
                    v.context.startActivity(intent)

                }

                popupbutton.setOnClickListener { v ->
                    val popupMenu = PopupMenu(v.context, v)
                    popupMenu.gravity = Gravity.END
                    popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                        val intent = Intent(v.context, editnoteactivity::class.java)
                        intent.putExtra("title", firebasemodel.title)
                        intent.putExtra("content", firebasemodel.content)
                        intent.putExtra("noteId",docid)
                        v.context.startActivity(intent)
                        false
                    }
                    popupMenu.menu.add("Delete").setOnMenuItemClickListener{
                        //
                        var documentReference: DocumentReference=firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document(docid)
                        documentReference.delete().addOnSuccessListener(){
                            Toast.makeText(v.context,"Note Deleted Successfully ",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener(){
                            Toast.makeText(v.context,"Failed to delete",Toast.LENGTH_SHORT).show()
                        }
                        false
                    }
                     popupMenu.show()

                }


            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_layout, parent, false)
                return NoteViewHolder(view)
            }
        }

        mRecyclerView=findViewById(R.id.recyclerview)
        mRecyclerView.setHasFixedSize(true)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = staggeredGridLayoutManager
        mRecyclerView.adapter=noteAdapter
    }

    // Define NoteViewHolder
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notetitle: TextView = itemView.findViewById(R.id.notetitle)
        val notecontent: TextView = itemView.findViewById(R.id.notecontent)
        val mnote: LinearLayout = itemView.findViewById(R.id.note)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                firebaseAuth.signOut()
                finish()
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Lifecycle methods to handle FirestoreRecyclerAdapter
    override fun onStart() {
        super.onStart()
        noteAdapter.startListening() // Start listening to Firestore data changes
    }

    override fun onStop() {
        super.onStop()
        if (::noteAdapter.isInitialized) {
            noteAdapter.stopListening() // Stop listening to avoid memory leaks
        }
    }
    private fun getRandomcolor(): Int {
        val colorcode: MutableList<Int> = ArrayList()
        colorcode.add(R.color.grey)
        colorcode.add(R.color.purple_200)
        colorcode.add(R.color.teal_200)
        colorcode.add(R.color.teal_700)
        colorcode.add(R.color.green)
        colorcode.add(R.color.lightgreen)
        colorcode.add(R.color.skyblue)
        colorcode.add(R.color.pink)
        colorcode.add(R.color.lightpink)

        val random = kotlin.random.Random
        val number: Int=random.nextInt(colorcode.size)
        return colorcode[number]

    }
}
