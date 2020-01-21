package com.shuvadip.loginapp.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shuvadip.loginapp.Models.NoteModel
import com.shuvadip.loginapp.R
import kotlinx.android.synthetic.main.list_item.view.*

class NoteAdapter(val context: Context, val notes: MutableList<NoteModel>) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        println("DataEntry triggered!! $notes")
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notes[position]
        holder.setData(note, position)
    }

    fun removeItem(notes : MutableList<NoteModel>, position: Int){
        notes.clear()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var pos : Int? = null
        var currentkey : String? = null
        var firebaseauth = FirebaseAuth.getInstance()
        var firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseauth?.currentUser!!.uid).child("notes")
        init {
            itemView.imgDelete.setOnClickListener {
                firebaseDatabase.child(currentkey!!).setValue(null)
                removeItem(notes, pos!!)
                Toast.makeText(context, "The selected item has been deleted!!", Toast.LENGTH_LONG).show()
            }
            itemView.txvNote.setOnLongClickListener {
                var myClipboard = getSystemService(context, ClipboardManager::class.java) as ClipboardManager
                var clipData = ClipData.newPlainText("text", itemView.txvNote.text)
                myClipboard.setPrimaryClip(clipData)

                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener true
            }
        }
        fun setData(note: NoteModel, pos: Int) {
            itemView.txvTitle.text = note.title
            itemView.txvNote.text = note.note
            this.currentkey = note.key
            this.pos = pos
        }
    }
}