package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class forgotpassword : AppCompatActivity() {
    private lateinit var mforgotpassword: EditText
    private lateinit var mpasswordrecoverbutton: Button
    private lateinit var mgobacktologin: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_forgotpassword)
        mforgotpassword=findViewById(R.id.forgotpassword)
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton)
        mgobacktologin=findViewById(R.id.gotologin)
        firebaseAuth= FirebaseAuth.getInstance()

        mgobacktologin.setOnClickListener{
            val intent=Intent(this@forgotpassword, MainActivity::class.java)
            startActivity(intent)
        }
        mpasswordrecoverbutton.setOnClickListener{
            val mail=mforgotpassword.text.toString().trim()
            if(mail.isEmpty()){
                Toast.makeText(applicationContext,"Enter your email First ",Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"Recover mail sent to your registered mail id",Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this@forgotpassword, MainActivity::class.java))

                    }else{
                        Toast.makeText(applicationContext,"Wrong Email or Account don't exist ",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}