package com.ferrao.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ferrao.app.R
import com.ferrao.app.database.Notes


class NotesAdapter(
    private val context: Context,
    private val notes: List<Notes>,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.notes_view, parent, false)
        return NotesViewHolder(rootView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notes: Notes = notes[position]
        val viewHolder: NotesViewHolder = holder as NotesViewHolder

        viewHolder.notesTitle.text = notes.notesTitle
        viewHolder.notesDescription.text = notes.notesDesc
        viewHolder.itemView.setOnClickListener {
            onClickListener.onItemClick(notes)
        }

    }

    internal class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(notes: Notes, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClick(notes)
            }
        }

        var notesTitle: TextView = itemView.findViewById(R.id.notes_title)
        var notesDescription: TextView = itemView.findViewById(R.id.notes_desc)
    }

    interface OnItemClickListener {
        fun onItemClick(notes: Notes)
    }
}