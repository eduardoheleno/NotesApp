package com.example.notesapp.adapters

import android.graphics.Color
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

        holder.noteContainerItemView.setOnLongClickListener {
            current.selected = true
            notifyItemChanged(position)
            callbackInterface.setIsOnSelectMode()

            true
        }

        holder.bind(current, position, callbackInterface)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteContainerItemView: LinearLayout = itemView.findViewById(R.id.noteContainer)
        private val noteTitleItemView: TextView = itemView.findViewById(R.id.noteTitle)
        private val noteContentItemView: TextView = itemView.findViewById(R.id.noteContent)

        fun bind(note: Note, itemPosition: Int, callbackInterface: CallbackInterface) {
            noteTitleItemView.text = note.title
            noteContentItemView.text = note.content

            if (note.selected) {
                noteContainerItemView.setBackgroundColor(Color.GRAY)
            } else {
                noteContainerItemView.setBackgroundResource(R.drawable.note_list_item_shape)
            }

            noteContainerItemView.setOnClickListener {
                callbackInterface.onClickNote(note, itemPosition)
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
        fun onClickNote(note: Note, itemPosition: Int)
        fun setIsOnSelectMode()
    }
}