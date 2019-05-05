package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.wtmberlin.R

class StudyGroupsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.study_groups_screen, container, false)
    }

    fun openContributions(view: View) {
        view.findNavController().navigate(R.id.reviews_screen)
        Toast.makeText(view.context, "Keep on clicking, it takes a while, I'm working on fixing it", Toast.LENGTH_LONG).show()
    }
}
