package com.example.notesapp.itemdecorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class NoteItemDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val column = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        if (column == 1) {
            outRect.left = 20
            outRect.right = 50
        } else {
            outRect.left = 50
            outRect.right = 20
        }
    }
}