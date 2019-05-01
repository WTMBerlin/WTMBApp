package com.wtmberlin.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
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
            .into(logo)

        view.setOnClickListener { openReviews(view) }
    }

    fun openReviews(view:View){
        NavDeepLinkBuilder(view.context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.reviews_screen)
            .createPendingIntent()
    }
}