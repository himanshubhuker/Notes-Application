package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ThreadContextElement

class MainActivity : AppCompatActivity() {
    private lateinit var mloginemail: EditText
    private lateinit var mloginpassword: EditText
    private lateinit var mlogin: RelativeLayout
    private lateinit var mgotosignup: RelativeLayout
    private lateinit var mgotoforgotpassword: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mprogressbar: ProgressBar

    private lateinit var firebaseUser: FirebaseUser
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        mloginemail=findViewById(R.id.loginemail)
        mloginpassword=findViewById(R.id.loginpassword)
        mlogin=findViewById(R.id.login)
        mprogressbar=findViewById(R.id.progressbarofmainactivity)
        mgotosignup=findViewById(R.id.gotosignup)
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword)
        firebaseAuth= FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        if(firebaseUser!=null){
            finish()
            startActivity(Intent(this@MainActivity, notesActivity::class.java))
        }

        mgotosignup.setOnClickListener{
            startActivity(Intent(this@MainActivity, signup::class.java))
        }

        mgotoforgotpassword.setOnClickListener{
            startActivity(Intent(this@MainActivity, forgotpassword::class.java))
        }
        fun checkmailverification(){
            var firebaseUser=firebaseAuth.currentUser
            if(firebaseUser?.isEmailVerified() ==true){
                Toast.makeText(applicationContext,"Successfully Logged In ",Toast.LENGTH_SHORT).show()
                finish()
                startActivity(Intent(this@MainActivity,notesActivity::class.java))
            }
            else{
                mprogressbar.visibility=View.INVISIBLE
                Toast.makeText(applicationContext,"Verify your mail first ",Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
            }
        }

        mlogin.setOnClickListener{
            val mail=mloginemail.text.toString().trim()
            val password=mloginpassword.text.toString().trim()

            if(mail.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext,"All fields are required ",Toast.LENGTH_SHORT).show()
            }
            else{
                mprogressbar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        checkmailverification()
                    }
                    else{
                        Toast.makeText(applicationContext,"Account doesn't exist ",Toast.LENGTH_SHORT).show()
                        mprogressbar.visibility=View.INVISIBLE
                    }
                }
            }

        }
    }
}