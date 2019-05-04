package com.wtmberlin.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wtmberlin.R
import kotlinx.android.synthetic.main.notes_screen.*
import kotlinx.android.synthetic.main.notes_screen.view.*
import java.util.prefs.Preferences

class NotesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.notes_screen,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notes_button_save.setOnClickListener { saveNotes(view) }
        notes_button_clean.setOnClickListener { deleteAllNotes() }
    }

    private fun deleteAllNotes() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun saveNotes(view:View){
        val note = view.notes_input.text.toString()
        persist(view, note)
    }

    //TODO persist in one common shared prefs, pass event name to the notes screen
    private fun persist(view: View, note: String) {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?:return
        with(prefs.edit()){
            putString("note", view.notes_input.text.toString()).apply()
        }.also{
            val oldNote = prefs.getString("note", "")
            val newNote = oldNote.plus("\n" + note)
            display(view, newNote)
        }
    }

    private fun display(view: View, text: String? ?= "See? This is how a default note looks like!") {
        view.notes_output.text = text
    }


    override fun onResume() {
        super.onResume()
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        activity?.notes_output?.text = prefs?.getString("name", "")
    }
}
