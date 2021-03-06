package com.wtmberlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.wtmberlin.R
import kotlinx.android.synthetic.main.social_media_screen.*

class SocialMediaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.social_media_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        community_reviews_click.setOnClickListener { openContributions(view) }
    }

    fun openContributions(view: View) {
        view.findNavController().navigate(R.id.contributions_screen)
    }
}
