package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wtmberlin.R
import kotlinx.android.synthetic.main.notes_screen.*
import kotlinx.android.synthetic.main.notes_screen.view.*

class NotesFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(
            R.layout.notes_screen,
            container, false
        )
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_button_save.setOnClickListener { saveNotes(view) }
        notes_button_clear.setOnClickListener { deleteAllNotes(view) }
        display(view, AppPreferences.note)
    }

    private fun deleteAllNotes(view: View) {
        AppPreferences.note = ""
        display(view, AppPreferences.note)
    }

    fun saveNotes(view: View) {
        val note = view.notes_input.text.toString()
        persist(view, note)
    }

    private fun persist(view: View, newNote: String) {
        val oldNote = AppPreferences.note
        val finalNote = newNote + "\n" + oldNote

        AppPreferences.note = finalNote
        display(view, AppPreferences.note)

    }

    private fun display(view: View, text: String? = "See? This is how a default note looks like!") {
        view.notes_output.text = text
    }


    override fun onResume() {
        super.onResume()
        display(root, AppPreferences.note)
    }
}
