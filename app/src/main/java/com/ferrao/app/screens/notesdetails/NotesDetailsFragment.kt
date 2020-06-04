package com.ferrao.app.screens.notesdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.ferrao.app.R
import kotlinx.android.synthetic.main.fragment_notes_details.*
import kotlinx.android.synthetic.main.fragment_notes_details.view.*


class NotesDetailsFragment : Fragment() {

    private var notesId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes_details, container, false)
        val notes = NotesDetailsFragmentArgs.fromBundle(requireArguments()).notes

        view.topAppBar.setNavigationIcon(R.drawable.ic_back)
        view.topAppBar.setNavigationOnClickListener{ findNavController().popBackStack() }

        notesId = notes?.notesId ?: 0L

        if (notes?.notesTitle.isNullOrEmpty()) {
            view.notes_title.hint = resources.getString(R.string.title)
        } else {
            view.notes_title.setText(notes?.notesTitle.toString())
        }
        if (notes?.notesTitle.isNullOrEmpty()) {
            view.notes_desc.hint = resources.getString(R.string.description)
        } else {
            view.notes_desc.setText(notes?.notesDesc.toString())
        }

        setUpListener(view)

        return view
    }

    private fun setUpListener(view: View) {
        view.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    val title = notes_title.text ?: ""
                    val description = notes_desc.text ?: ""
                    setFragmentResult(
                        "notes", bundleOf(
                            "notesTitle" to title.toString(),
                            "notesDesc" to description.toString(),
                            "notesId" to notesId
                        )
                    )
                    findNavController().popBackStack()
                    true
                }
                R.id.delete -> {
                    setFragmentResult(
                        "notesDelete", bundleOf("notesId" to notesId)
                    )
                    findNavController().popBackStack()
                    true
                }

                else -> false
            }
        }
    }

}