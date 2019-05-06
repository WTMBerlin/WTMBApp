package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wtmberlin.R
import kotlinx.android.synthetic.main.kids_friendly_screen.*

class KidsFriendlyEventsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.kids_friendly_screen,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kids_screen_toy.setOnClickListener {
            it
                .animate()
                .rotation(
                    kids_screen_toy.rotation + 180.0f
                )
        }
    }

}