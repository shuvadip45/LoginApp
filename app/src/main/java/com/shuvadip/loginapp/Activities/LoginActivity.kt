package com.shuvadip.loginapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.shuvadip.loginapp.R

class LoginActivity  : AppCompatActivity(){
    private var loginbtn: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var firebaseauth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        loginbtn = findViewById(R.id.loginBtn)
        email = findViewById(R.id.userMail)
        password = findViewById(R.id.userPass)
        firebaseauth = FirebaseAuth.getInstance()

        loginbtn?.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        var emailtext = email?.text.toString().trim()
        var passtext = password?.text.toString().trim()

        if (TextUtils.isEmpty(emailtext))
            Toast.makeText(this, "E-mail field cannot be empty", Toast.LENGTH_LONG).show()
        else if(TextUtils.isEmpty(passtext))
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_LONG).show()
        else{
            firebaseauth?.signInWithEmailAndPassword(emailtext, passtext)?.addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Logged in Successfully!!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@LoginActivity, NoteActivity::class.java))
                    }
                    else{
                        val error = task.exception?.message
                        Toast.makeText(this@LoginActivity,"Error: $error", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }
}