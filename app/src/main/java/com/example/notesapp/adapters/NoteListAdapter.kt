package com.example.notesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.room.Note

class NoteListAdapter(private val callbackInterface: CallbackInterface) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NotesComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current, callbackInterface)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteContainerItemView: LinearLayout = itemView.findViewById(R.id.noteContainer)
        private val noteTitleItemView: TextView = itemView.findViewById(R.id.noteTitle)
        private val noteContentItemView: TextView = itemView.findViewById(R.id.noteContent)

        fun bind(note: Note, callbackInterface: CallbackInterface) {
            noteTitleItemView.text = note.title
            noteContentItemView.text = note.content

            noteContainerItemView.setOnClickListener {
                callbackInterface.openDialogWithNote(note)
            }
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_list_item, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    class NotesComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.content == newItem.content
        }
    }

    interface CallbackInterface {
        fun openDialogWithNote(note: Note)
    }
}