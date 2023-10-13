package com.example.notesapp.activities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.adapters.TagListAdapter
import com.example.notesapp.databinding.ActivityTagsBinding
import com.example.notesapp.fragments.TagDialogModalFragment
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.viewmodels.TagViewModel
import com.example.notesapp.viewmodels.TagViewModelFactory
import com.google.android.material.snackbar.Snackbar

class TagsActivity : AppCompatActivity() {
    private val tagViewModel: TagViewModel by viewModels {
        TagViewModelFactory((application as NotesApplication).tagRepository)
    }
    private lateinit var adapter: TagListAdapter
    private lateinit var binding: ActivityTagsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tags)

        initTagRecyclerView()
        initClickListeners()
    }

    private fun initTagRecyclerView() {
        val recyclerView = binding.tagsRecyclerView
        adapter = TagListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private val deleteIcon = ContextCompat.getDrawable(baseContext, R.drawable.baseline_delete_24)
            private val background = ColorDrawable()
            private val backgroundColor = Color.parseColor("#F44336")
            private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        false
                    )

                    return
                }

                background.color = backgroundColor
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)

                deleteIcon?.let {
                    val intrinsicWidth = deleteIcon.intrinsicWidth
                    val intrinsicHeight = deleteIcon.intrinsicHeight

                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
                c.drawRect(left, top, right, bottom, clearPaint)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTag = adapter.currentList[viewHolder.absoluteAdapterPosition]
                tagViewModel.deleteTag(deletedTag)

                Snackbar.make(binding.root, "Deleted " + deletedTag.label, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) {
                        tagViewModel.saveTag(deletedTag)
                    }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        tagViewModel.allTags.observe(this) {
            it?.let {
                adapter.submitList(it)
            }
        }

        adapter.onItemClick = { tag: Tag, _itemPosition: Int ->
            tagViewModel.tag.value = tag
            val tagModal = TagDialogModalFragment()
            tagModal.show(supportFragmentManager, TagDialogModalFragment.TAG_DIALOG_FRAGMENT_TAG)
        }
    }

    private fun initClickListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.addTagBtn.setOnClickListener {
            tagViewModel.tagLabel.value = ""
            tagViewModel.tagColor.value = ""
            TagDialogModalFragment().show(supportFragmentManager, TagDialogModalFragment.TAG_DIALOG_FRAGMENT_TAG)
        }
    }
}