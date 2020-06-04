package com.ferrao.app.screens.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferrao.app.R
import com.ferrao.app.adapters.NotesAdapter
import com.ferrao.app.database.Notes
import com.ferrao.app.database.NotesDatabase
import com.ferrao.app.databinding.FragmentNotesBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NotesFragment : Fragment(), NotesAdapter.OnItemClickListener {


    private lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel

    private var listOfNotes = ArrayList<Notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentNotesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes, container, false
        )


        val application = requireNotNull(this.activity).application

        val dataSource = NotesDatabase.getInstance(application).notesDatabaseDao

        val viewModelFactory = NotesViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NotesViewModel::class.java)


        viewModel.notesList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                listOfNotes.clear()
                listOfNotes.addAll(it)
                notesAdapter.notifyDataSetChanged()
                binding.notesList.visibility = View.VISIBLE
                binding.noNotes.visibility = View.GONE
                binding.noNotesTxt.visibility = View.GONE
            } else {
                binding.noNotes.visibility = View.VISIBLE
                binding.noNotesTxt.visibility = View.VISIBLE
                binding.notesList.visibility = View.GONE
            }
        })

        binding.addNotes.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(NotesFragmentDirections.actionNotesToDetails())
        }


        setupNotesView(binding)

        setFragmentResultListener("notes") { key, bundle ->
            val notesTitle = bundle.getString("notesTitle") ?: ""
            val notesDesc = bundle.getString("notesDesc") ?: ""
            val notesId = bundle.getLong("notesId")
            if (notesId == 0L) {
                viewModel.insertNote(Notes(notesTitle = notesTitle, notesDesc = notesDesc))
            } else {
                viewModel.updateNotes(
                    Notes(
                        notesId = notesId,
                        notesTitle = notesTitle,
                        notesDesc = notesDesc
                    )
                )
            }
        }

        setFragmentResultListener("notesDelete") { key, bundle ->
            viewModel.onDelete(bundle.getLong("notesId"))
        }

        return binding.root
    }

    private fun setupNotesView(binding: FragmentNotesBinding) {
        listOfNotes.clear()
        notesAdapter = NotesAdapter(requireContext(), listOfNotes, this)
        binding.notesList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.notesList.adapter = notesAdapter
    }

    override fun onItemClick(notes: Notes) {
        val action = NotesFragmentDirections.actionNotesToDetails()
        action.notes = notes
        NavHostFragment.findNavController(this).navigate(action)
    }

}