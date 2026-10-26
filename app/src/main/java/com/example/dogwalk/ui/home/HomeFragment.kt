package com.example.dogwalk.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalk.R
import com.example.dogwalk.MainActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
        view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnStartWalk)
            .setOnClickListener { (activity as? MainActivity)?.showWalkScreen() }
        return view
    }
}