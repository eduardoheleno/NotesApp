package com.example.notesapp.fragments

import android.content.DialogInterface
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
class TagDialogModalFragment : BottomSheetDialogFragment() {
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

        binding.tagViewModel = tagViewModel
        binding.lifecycleOwner = requireActivity()

        initBtnText()
        initObservers()
        initColorPicker()

        return binding.root
    }

    private fun initBtnText() {
        if (tagViewModel.tag.value != null) {
            binding.tagModalActBtn.text = getString(R.string.save)
        } else {
            binding.tagModalActBtn.text = getString(R.string.create)
        }

        binding.tagModalActBtn.setOnClickListener { validateAndSaveTag() }
    }

    private fun initObservers() {
        tagViewModel.tag.observe(viewLifecycleOwner) {
            it?.apply {
                binding.labelText.setText(it.label)

                binding.tagColor.setBackgroundColor(Color.parseColor(it.color))
                tagViewModel.tagColor.value = it.color
            }
        }
    }

    private fun initColorPicker() {
        binding.tagColor.setOnClickListener { it ->
            val popMenu = PopupMenu(requireContext(), it)

            popMenu.gravity = Gravity.END
            popMenu.inflate(R.menu.tag_colors_menu)
            popMenu.setOnMenuItemClickListener {
                val color: Int?
                val colorString: String?

                when(it.itemId) {
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
    }

    private fun validateAndSaveTag() {
        if (tagViewModel.tagLabel.value.isNullOrBlank()) {
            binding.labelText.error = "Required field!"
            return
        }

        tagViewModel.tag.value?.apply {
            val tag = Tag(
                tagViewModel.tagLabel.value.toString(),
                tagViewModel.tagColor.value.toString(),
                this.id
            )
            tagViewModel.saveTag(tag)
            dismiss()

            return
        }

        tagViewModel.saveTag(null)
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        tagViewModel.tag.value = null
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG_DIALOG_FRAGMENT_TAG = "TAG_DIALOG_FRAGMENT_TAG"
    }
}