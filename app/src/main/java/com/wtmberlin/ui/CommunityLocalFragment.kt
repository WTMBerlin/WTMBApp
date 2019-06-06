package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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

        community_reviews_click.setOnClickListener { openReviews(community_reviews_click) }
        logo.setOnClickListener { openReviews(logo) }
    }

    fun openReviews(view: View) {
        view.findNavController().navigate(R.id.reviews_screen)
    }
}
