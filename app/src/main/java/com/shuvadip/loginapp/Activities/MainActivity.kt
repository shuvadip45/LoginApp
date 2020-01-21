package com.shuvadip.loginapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.shuvadip.loginapp.R

class MainActivity : AppCompatActivity() {

    private var loginbtn:Button? = null
    private var signupbtn:Button? = null
    private var firebaseauth:FirebaseAuth? = null
    private var firebasedatabase:DatabaseReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        loginbtn = findViewById(R.id.LoginBtnMain)
        signupbtn = findViewById(R.id.SignupBtn)
        firebaseauth = FirebaseAuth.getInstance()

        firebaseauth?.addAuthStateListener {

            if (firebaseauth?.currentUser!=null){
                startActivity(Intent(this, NoteActivity::class.java))
            }
        }



        loginbtn?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        signupbtn?.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
