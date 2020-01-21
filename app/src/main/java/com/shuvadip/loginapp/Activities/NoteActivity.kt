package com.shuvadip.loginapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shuvadip.loginapp.Adapters.NoteAdapter
import com.shuvadip.loginapp.Models.NoteModel
import com.shuvadip.loginapp.R
import kotlinx.android.synthetic.main.note_activity.*

class NoteActivity : AppCompatActivity(){

    var notes : MutableList<NoteModel> = mutableListOf()
    var firebaseauth : FirebaseAuth? = null
    var firebaseDatabase : DatabaseReference? = null
    var key : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_activity)

        firebaseauth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseauth?.currentUser!!.uid)

        firebaseDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(task: DatabaseError) {
                println("Error: $task")
            }

            override fun onDataChange(task: DataSnapshot) {
                if(task.exists()){
                    var count =0
                    for(a in task.child("notes").children){
                        val note= a.getValue(NoteModel::class.java)
                        notes.add(note!!)
                        notes[count].key = a.key
                        println("DataNote: $notes")
                        count++
                    }
                    setupRecyclerView()
                }
            }

        })


        btnNewNote.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnSignout.setOnClickListener {
            firebaseauth?.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    fun setupRecyclerView(){
        var rview = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NoteAdapter(this, notes)
        println("RecyclerView Triggered!!")

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rview.layoutManager = layoutManager
        rview.adapter = adapter
    }
}