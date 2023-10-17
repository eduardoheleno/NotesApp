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
import com.example.notesapp.room.NoteWithTag
import com.example.notesapp.room.tag.Tag

class NoteListAdapter : ListAdapter<NoteWithTag, NoteListAdapter.NoteViewHolder>(NotesComparator()) {
    private var notes: List<NoteWithTag> = emptyList()

    var onItemClick: ((note: NoteWithTag, itemPosition: Int) -> Unit)? = null
    var onLongItemClick: ((note: NoteWithTag, itemPosition: Int) -> Unit)? = null

    override fun onCurrentListChanged(
        previousList: MutableList<NoteWithTag>,
        currentList: MutableList<NoteWithTag>
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
        return getItem(position).note.id.toLong()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val maxQtdCharactersTitle = 20
        private val maxQtdCharactersContent = 70

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

        private fun getShorterText(originalText: String, limiter: Int): String {
            var shorterText = originalText.substring(0, limiter)
            shorterText = "$shorterText..."

            return shorterText
        }

        fun bind(noteWithTag: NoteWithTag) {
            var noteTitle = noteWithTag.note.title
            var noteContent = noteWithTag.note.content

            if (noteTitle.length > maxQtdCharactersTitle) {
                noteTitle = getShorterText(noteTitle, maxQtdCharactersTitle)
            }

            if (noteContent.length > maxQtdCharactersContent) {
                noteContent = getShorterText(noteContent, maxQtdCharactersContent)
            }

            noteTitleItemView.text = noteTitle
            noteContentItemView.text = noteContent

            if (noteWithTag.note.selected) {
                noteContainerItemView.setBackgroundColor(Color.GRAY)
            } else {
                val drawable = ContextCompat.getDrawable(noteContainerItemView.context, R.drawable.shape_note_list_item)
                val color: Int
                val colorFilter: PorterDuffColorFilter

                if (noteWithTag.tag?.color != null) {
                    color = Color.parseColor(noteWithTag.tag.color)
                    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                } else {
                    color = Color.parseColor(Tag.Colors.DEFAULT.colorCode)
                    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
                }

                drawable?.colorFilter = colorFilter
                noteContainerItemView.background = drawable
            }
        }
    }
}

class NotesComparator : DiffUtil.ItemCallback<NoteWithTag>() {
    override fun areItemsTheSame(oldItem: NoteWithTag, newItem: NoteWithTag): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NoteWithTag, newItem: NoteWithTag): Boolean {
        return oldItem.note.id == newItem.note.id
    }
}