package com.example.dogwalk.ui.pets

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalk.R
import com.example.dogwalk.model.Pet

class PetAdapter(
    private var pets: List<Pet>,
    private val onPetClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val image: ImageView =
            itemView.findViewById(R.id.imgPet)

        val name: TextView =
            itemView.findViewById(R.id.tvPetName)

        val breed: TextView =
            itemView.findViewById(R.id.tvPetBreed)

        val info: TextView =
            itemView.findViewById(R.id.tvPetInfo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_pet, parent, false)

        return PetViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PetViewHolder,
        position: Int
    ) {

        val pet = pets[position]

        holder.name.text = pet.name
        holder.breed.text = pet.breed
        holder.info.text = pet.sex

        if (pet.photoUri != null) {

            holder.image.setImageURI(
                Uri.parse(pet.photoUri)
            )

        } else {

            holder.image.setImageResource(
                R.drawable.ic_pets
            )
        }

        holder.itemView.setOnClickListener {
            onPetClick(pet)
        }
    }

    override fun getItemCount(): Int {
        return pets.size
    }

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }
}