package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class signup : AppCompatActivity() {
    private lateinit var msignupemail: EditText
    private lateinit var msignuppassword: EditText
    private lateinit var msignup: RelativeLayout
    private lateinit var mgotologin: TextView


    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        msignupemail=findViewById(R.id.signupemail)
        msignuppassword=findViewById(R.id.signuppassword)
        msignup=findViewById(R.id.signup)
        mgotologin=findViewById(R.id.gotologin)

        firebaseAuth= FirebaseAuth.getInstance()


        mgotologin.setOnClickListener{
            val intent=Intent(this@signup, MainActivity::class.java)
            startActivity(intent)
        }
        fun sendEmailVerification(){
            val firebaseUser: FirebaseUser
            firebaseUser= firebaseAuth.currentUser!!
            if(firebaseUser!=null){
                firebaseUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Verification mail sent,Verify and login again", Toast.LENGTH_SHORT).show()
                        firebaseAuth.signOut()
                        finish()
                        startActivity(Intent(this@signup, MainActivity::class.java))

                    }
                }

            }
            else{
                Toast.makeText(applicationContext, "Failed to send Verification mail ", Toast.LENGTH_SHORT).show()
            }

        }

        msignup.setOnClickListener{
            val mail=msignupemail.text.toString().trim()
            val password=msignuppassword.text.toString().trim()
            if(mail.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext,"All Fields are Required ",Toast.LENGTH_SHORT).show()
            }
            else if(password.length<7){
                Toast.makeText(applicationContext,"Password must be greater than 7",Toast.LENGTH_SHORT).show()
            }
            else{

                firebaseAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            sendEmailVerification()  // Call the function to send verification email
                        } else {
                            // Registration failed
                            Toast.makeText(applicationContext, "Failed to Register: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }


            }
        }

    }



}