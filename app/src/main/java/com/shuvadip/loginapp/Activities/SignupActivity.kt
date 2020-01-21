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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shuvadip.loginapp.R

class SignupActivity : AppCompatActivity() {

    private var signupbtn: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var dateofBirth: EditText? = null
    private var phno: EditText? = null
    private var firebaseauth: FirebaseAuth? = null
    private var firebaseDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        email = findViewById(R.id.userMailSignUp)
        password = findViewById(R.id.userPasswordSignUp)
        firstName = findViewById(R.id.userFirstName)
        lastName = findViewById(R.id.userLastName)
        dateofBirth = findViewById(R.id.userDateOfBirth)
        phno = findViewById(R.id.userPhoneNo)
        signupbtn = findViewById(R.id.btnSignup)
        firebaseauth = FirebaseAuth.getInstance()

        signupbtn?.setOnClickListener {
            registerNewUser()
            saveUserInfo()
            loginUser()
        }


    }
    fun registerNewUser() {
        var emailtext = email?.text.toString().trim()
        var passtext = password?.text.toString().trim()


        if (TextUtils.isEmpty(emailtext)) {
            Toast.makeText(this, "E-mail field cannot be empty", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(passtext)) {
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_LONG).show()
        } else {
            firebaseauth?.createUserWithEmailAndPassword(emailtext, passtext)
                ?.addOnCompleteListener(object :
                    OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Account created successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val error = task.exception?.message
                            Toast.makeText(
                                this@SignupActivity,
                                "Error: $error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                })
        }
    }

    fun saveUserInfo(){
        var emailtext = email?.text.toString().trim()
        var name = firstName?.text.toString().trim() + " " + lastName?.text.toString().trim()
        var dob = dateofBirth?.text.toString().trim()
        var phno = phno?.text.toString().trim()

        val userinfo = HashMap<String, Any>()


        userinfo.put("email", emailtext)
        userinfo.put("name", name)
        userinfo.put("dob", dob)
        userinfo.put("phno", phno)

        firebaseauth?.addAuthStateListener {
            if (firebaseauth?.currentUser != null) {
                println("My User: " + firebaseauth?.currentUser)
                firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(firebaseauth?.currentUser!!.uid).child("userinfo")
                firebaseDatabase?.setValue(userinfo)
                    ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            if (task.isSuccessful) {
                                println("UserInfo: $userinfo")
                                Toast.makeText(
                                    this@SignupActivity,
                                    "Details added successfully!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else {
                                val error = task.exception?.message
                                println(userinfo)
                                Toast.makeText(
                                    this@SignupActivity,
                                    "Error: $error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
            }
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
            firebaseauth?.signInWithEmailAndPassword(emailtext, passtext)?.addOnCompleteListener(object:
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {

                    if(task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity,
                            "Logged in Successfully!!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@SignupActivity, NoteActivity::class.java))
                    }
                    else{
                        val error = task.exception?.message
                        Toast.makeText(this@SignupActivity,"Error: $error", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
    }
}