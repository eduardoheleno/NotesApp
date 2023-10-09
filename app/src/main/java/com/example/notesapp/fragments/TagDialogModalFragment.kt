package com.example.notesapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentTagDialogModalBinding
import com.example.notesapp.room.NotesApplication
import com.example.notesapp.room.tag.Tag
import com.example.notesapp.viewmodels.TagViewModel
import com.example.notesapp.viewmodels.TagViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [TagDialogModalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TagDialogModalFragment(private val dialogMode: DialogMode) : BottomSheetDialogFragment() {
    private val tagViewModel: TagViewModel by activityViewModels {
        TagViewModelFactory((activity?.application as NotesApplication).tagRepository)
    }
    private lateinit var binding: FragmentTagDialogModalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tag_dialog_modal, container, false)

        binding.tagModalActBtn.text = dialogMode.textLabel

        binding.tagColor.setOnClickListener { it ->
            val popMenu = PopupMenu(requireContext(), it)

            popMenu.gravity = Gravity.END
            popMenu.inflate(R.menu.tag_colors_menu)
            popMenu.setOnMenuItemClickListener {
                val color: Int?
                val colorString: String?

                when(it.itemId) {
                    R.id.tagColorBlue -> {
                        colorString = Tag.Colors.BLUE.colorCode
                        color = Color.parseColor(Tag.Colors.BLUE.colorCode)
                    }
                    R.id.tagColorYellow -> {
                        colorString = Tag.Colors.YELLOW.colorCode
                        color = Color.parseColor(Tag.Colors.YELLOW.colorCode)
                    }
                    R.id.tagColorOrange -> {
                        colorString = Tag.Colors.ORANGE.colorCode
                        color = Color.parseColor(Tag.Colors.ORANGE.colorCode)
                    }
                    R.id.tagColorPink -> {
                        colorString = Tag.Colors.PINK.colorCode
                        color = Color.parseColor(Tag.Colors.PINK.colorCode)
                    }
                    R.id.tagColorGreen -> {
                        colorString = Tag.Colors.GREEN.colorCode
                        color = Color.parseColor(Tag.Colors.GREEN.colorCode)
                    }
                    else -> {
                        colorString = null
                        color = null
                    }
                }

                if (colorString != null && color != null) {
                    tagViewModel.tagColor.value = colorString
                    binding.tagColor.setBackgroundColor(color)

                    true
                } else {
                    false
                }
            }
            popMenu.show()
        }

        binding.labelText.addTextChangedListener { tagViewModel.tagLabel.value = it.toString() }

        binding.tagModalActBtn.setOnClickListener {
            println(tagViewModel.tagLabel.value)
            println(tagViewModel.tagColor.value)
        }

        return binding.root
    }

    companion object {
        const val TAG_DIALOG_FRAGMENT_TAG = "TAG_DIALOG_FRAGMENT_TAG"
    }

    enum class DialogMode(val textLabel: String) {
        CREATE("Create"),
        EDIT("Edit")
    }
}