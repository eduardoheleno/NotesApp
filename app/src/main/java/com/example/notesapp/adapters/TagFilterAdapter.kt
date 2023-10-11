package com.example.notesapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
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

class TagFilterAdapter : ListAdapter<Tag, TagFilterAdapter.TagFilterViewHolder>(TagFilterComparator()) {
    private var tags: List<Tag> = emptyList()

    var onItemClick: ((tag: Tag, itemPosition: Int) -> Unit)? = null

    override fun onCurrentListChanged(
        previousList: MutableList<Tag>,
        currentList: MutableList<Tag>
    ) {
        tags = currentList
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagFilterViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_filter_list_item, parent, false)
        return TagFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagFilterViewHolder, position: Int) {
        val tag = getItem(position)
        holder.bind(tag)
    }

    inner class TagFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagFilterContainerItemView: LinearLayout = itemView.findViewById(R.id.tagFilterContainer)
        private val tagLabel: TextView = itemView.findViewById(R.id.tagFilterLabel)

        init {
            tagFilterContainerItemView.setOnClickListener {
                onItemClick?.invoke(tags[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }

        fun bind(tag: Tag) {
            tagLabel.text = tag.label

            if (tag.selected) {
                tagFilterContainerItemView.setBackgroundResource(R.drawable.shape_tag_filter_selected)
                tagLabel.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                tagFilterContainerItemView.setBackgroundResource(R.drawable.shape_tag_filter)
                tagLabel.setTextColor(Color.parseColor("#7C7C7C"))
            }
        }
    }
}

class TagFilterComparator : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.id == newItem.id
    }
}

class TagFilterAdapterDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = context.resources.getDimensionPixelSize(R.dimen.first_item_margin)
        }
    }
}