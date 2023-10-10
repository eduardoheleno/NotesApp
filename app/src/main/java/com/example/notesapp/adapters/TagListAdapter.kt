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
import com.example.notesapp.room.tag.Tag

class TagListAdapter() : ListAdapter<Tag, TagListAdapter.TagViewHolder>(TagComparator()) {
    private var tags: List<Tag> = emptyList()

    var onItemClick: ((tag: Tag, itemPosition: Int) -> Unit)? = null

    override fun onCurrentListChanged(
        previousList: MutableList<Tag>,
        currentList: MutableList<Tag>
    ) {
        tags = currentList
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_list_item, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = getItem(position)
        holder.bind(tag)
    }

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagContainerItemView: LinearLayout = itemView.findViewById(R.id.tagContainer)
        private val tagLabel: TextView = itemView.findViewById(R.id.tagLabel)

        init {
            tagContainerItemView.setOnClickListener {
                onItemClick?.invoke(tags[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }

        fun bind(tag: Tag) {
            tagContainerItemView.setBackgroundColor(Color.parseColor(tag.color))
            tagLabel.text = tag.label
        }
    }
}

class TagComparator : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.id == newItem.id
    }
}