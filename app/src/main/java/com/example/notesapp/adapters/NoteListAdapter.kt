package com.example.notesapp.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.room.note.Note

class NoteListAdapter() : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(NotesComparator()) {
    private var notes: List<Note> = emptyList()

    var onItemClick: ((note: Note, itemPosition: Int) -> Unit)? = null
    var onLongItemClick: ((note: Note, itemPosition: Int) -> Unit)? = null

    override fun onCurrentListChanged(
        previousList: MutableList<Note>,
        currentList: MutableList<Note>
    ) {
        notes = currentList
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteContainerItemView: LinearLayout = itemView.findViewById(R.id.noteContainer)
        private val noteTitleItemView: TextView = itemView.findViewById(R.id.noteTitle)
        private val noteContentItemView: TextView = itemView.findViewById(R.id.noteContent)

        init {
            noteContainerItemView.setOnClickListener {
                onItemClick?.invoke(notes[absoluteAdapterPosition], absoluteAdapterPosition)
            }

            noteContainerItemView.setOnLongClickListener {
                onLongItemClick?.invoke(notes[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
        }

        fun bind(note: Note) {
            noteTitleItemView.text = note.title
            noteContentItemView.text = note.content

            if (note.selected) {
                noteContainerItemView.setBackgroundColor(Color.GRAY)
            } else {
                val drawable = ContextCompat.getDrawable(noteContainerItemView.context, R.drawable.shape_note_list_item)
                val color = Color.parseColor("#D9E8FC")
                val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                drawable?.colorFilter = colorFilter

                noteContainerItemView.background = drawable
            }
        }
    }
}

class NotesComparator : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }
}