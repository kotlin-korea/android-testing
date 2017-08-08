/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.testing.notes.Injection
import com.example.android.testing.notes.R
import com.example.android.testing.notes.addnote.AddNoteActivity
import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.notedetail.NoteDetailActivity
import com.google.common.base.Preconditions.checkNotNull
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.fragment_notes.*
import java.util.*

/**
 * Display a grid of [Note]s
 */
class NotesFragment : Fragment(), NotesContract.View {

    private lateinit var mActionsListener: NotesContract.UserActionsListener
    private lateinit var mListAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = NotesAdapter(ArrayList<Note>(0), onNoteClick)
        mActionsListener = NotesPresenter(Injection.provideNotesRepository(), this)
    }

    override fun onResume() {
        super.onResume()
        mActionsListener.loadNotes(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // If a note was successfully added, show snackbar
        if (REQUEST_ADD_NOTE == requestCode && Activity.RESULT_OK == resultCode) {
            Snackbar.make(view!!, getString(R.string.successfully_saved_note_message), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_list.adapter = mListAdapter

        val numColumns = context.resources.getInteger(R.integer.num_notes_columns)

        notes_list.setHasFixedSize(true)
        notes_list.layoutManager = GridLayoutManager(context, numColumns)

        // Set up floating action button
        val fab = activity.findViewById(R.id.fab_add_notes) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_add)
        fab.setOnClickListener { mActionsListener.addNewNote() }

        // Pull-to-refresh
        refresh_layout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark))
        refresh_layout.setOnRefreshListener { mActionsListener.loadNotes(true) }
    }

    /**
     * Listener for clicks on notes in the RecyclerView.
     */
    internal var onNoteClick: (clickedNote: Note) -> Unit = { clickedNote: Note ->
        mActionsListener.openNoteDetails(clickedNote)
    }

    override fun setProgressIndicator(active: Boolean) {
        if (view == null) {
            return
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        refresh_layout.post { refresh_layout.isRefreshing = active }
    }

    override fun showNotes(notes: List<Note>) {
        mListAdapter.replaceData(notes)
    }

    override fun showAddNote() {
        val intent = Intent(context, AddNoteActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_NOTE)
    }

    override fun showNoteDetailUi(noteId: String) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        startActivity(
                Intent(context, NoteDetailActivity::class.java).apply {
                    putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId)
                })
    }


    private class NotesAdapter(notes: List<Note>, private val onNoteClick: (clickedNote: Note) -> Unit) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

        private lateinit var mNotes: List<Note>

        init {
            setList(notes)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val noteView = inflater.inflate(R.layout.item_note, parent, false)

            return ViewHolder(noteView, onNoteClick)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val note = mNotes[position]

            viewHolder.title.text = note.title
            viewHolder.description.text = note.description
        }

        fun replaceData(notes: List<Note>) {
            setList(notes)
            notifyDataSetChanged()
        }

        private fun setList(notes: List<Note>) {
            mNotes = notes
        }

        override fun getItemCount(): Int {
            return mNotes.size
        }

        fun getItem(position: Int): Note {
            return mNotes[position]
        }

        inner class ViewHolder(itemView: View, private val onNoteClick: (clickedNote: Note) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val title: TextView by lazy { itemView.findViewById(R.id.note_detail_title) as TextView }
            val description: TextView by lazy { itemView.findViewById(R.id.note_detail_description) as TextView }

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                onNoteClick(getItem(adapterPosition))
            }
        }
    }

    companion object {

        private val REQUEST_ADD_NOTE = 1

        fun newInstance(): NotesFragment = NotesFragment()
    }

}
// Requires empty public constructor
