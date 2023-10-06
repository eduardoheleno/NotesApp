package com.example.notesapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.viewmodels.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).noteRepository)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        binding.menuBtn.setOnClickListener { showPopupMenu(it) }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        popupMenu.gravity = Gravity.END
        popupMenu.inflate(R.menu.floating_popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.createTagItem -> {
                    val intent = Intent(this, TagsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}