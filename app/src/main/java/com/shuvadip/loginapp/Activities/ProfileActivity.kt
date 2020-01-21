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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shuvadip.loginapp.R

class ProfileActivity : AppCompatActivity() {
    var title : EditText? = null
    var note : EditText? = null
    var newnotebtn : Button? = null
    var firebaseauth : FirebaseAuth? = null
    var firebaseDatabase : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newnote_activity)

        title = findViewById(R.id.titleText)
        note = findViewById(R.id.noteText)
        newnotebtn = findViewById(R.id.btnAddNote)
        firebaseauth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseauth?.currentUser!!.uid).child("notes")

        newnotebtn?.setOnClickListener {
            saveNote()
            startActivity(Intent(this, NoteActivity::class.java))
        }
    }

    fun saveNote(){
        val titletext = title?.text.toString().trim()
        val notetext = note?.text.toString().trim()

        if(TextUtils.isEmpty(titletext))
            Toast.makeText(this@ProfileActivity, "Plese enter something in this field", Toast.LENGTH_SHORT).show()
        else if (TextUtils.isEmpty(notetext))
            Toast.makeText(this@ProfileActivity, "Plese enter something in this field", Toast.LENGTH_SHORT).show()
        else {
            val notes = HashMap<String,Any>()
            notes.put("title",titletext)
            notes.put("note",notetext)

            firebaseDatabase?.push()?.setValue(notes)?.addOnCompleteListener(object : OnCompleteListener<Void>{
                override fun onComplete(task: Task<Void>) {
                    if(task.isSuccessful)
                        Toast.makeText(this@ProfileActivity, "Note added successfully", Toast.LENGTH_LONG).show()
                    else{
                        val error = task.exception?.message
                        Toast.makeText(this@ProfileActivity, "Error: $error", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
    }
}