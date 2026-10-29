package com.example.dogwalk.ui.pets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PetsFragment : Fragment() {

    private lateinit var viewModel: PetsViewModel
    private lateinit var adapter: PetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_pets,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this)[PetsViewModel::class.java]

        val recyclerPets =
            view.findViewById<RecyclerView>(
                R.id.recyclerPets
            )

        val fabAddPet =
            view.findViewById<FloatingActionButton>(
                R.id.fabAddPet
            )

        adapter = PetAdapter(
            viewModel.getPets()
        ) { pet ->

            openPetForm(pet.id)
        }

        recyclerPets.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerPets.adapter = adapter

        fabAddPet.setOnClickListener {
            openPetForm(0)
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            adapter.updatePets(
                viewModel.getPets()
            )
        }
    }

    private fun openPetForm(petId: Long) {

        val fragment = PetFormFragment()

        fragment.arguments = Bundle().apply {
            putLong("petId", petId)
        }

        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }
}