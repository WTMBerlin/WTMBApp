package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.wtmberlin.R
import kotlinx.android.synthetic.main.community_local_screen.*

class CommunityLocalFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.community_local_screen,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Picasso.get()
            .load("https://raw.githubusercontent.com/WTMBerlin/slides/master/logos/fb-cover.jpg")
            .placeholder(R.drawable.events_event_image_placeholder)
            .into(logo)
    }
}