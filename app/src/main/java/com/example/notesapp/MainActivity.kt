package com.example.notesapp

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.fragments.NoteDialogFragment
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val test = ObjectAnimator.ofFloat(binding.removeNoteBtn, View.TRANSLATION_X, 200f, 0f)
        val hideAnimation = createHideAnimation(test)

        binding.addNewNoteBtn.setOnClickListener {
            hideAnimation.start()
//            val dialog = NoteDialogFragment()
//            dialog.show(supportFragmentManager, NoteDialogFragment.NOTE_DIALOG_FRAGMENT_TAG)
//
//            noteViewModel.setEditingNewNote()
        }
    }

    private fun createHideAnimation(testAnimator: ObjectAnimator): ObjectAnimator {
        val hideAnimation = ObjectAnimator.ofFloat(binding.addNewNoteBtn, View.TRANSLATION_X, 0f, 200f)
        hideAnimation.duration = 300

        hideAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                binding.removeNoteBtn.visibility = View.VISIBLE
                testAnimator.start()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        return hideAnimation
    }
}