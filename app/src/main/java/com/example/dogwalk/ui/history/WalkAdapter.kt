package com.example.dogwalk.ui.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalk.R
import com.example.dogwalk.model.Walk
import com.example.dogwalk.model.Pet
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class WalkAdapter(
    private var walks: List<Walk>,
    private val onWalkClick: (Walk) -> Unit
) : RecyclerView.Adapter<WalkAdapter.WalkViewHolder>() {

    class WalkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.walkPetImage)
        val title: TextView = view.findViewById(R.id.walkTitle)
        val details: TextView = view.findViewById(R.id.walkDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkViewHolder =
        WalkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_walk, parent, false)
        )

    override fun onBindViewHolder(holder: WalkViewHolder, position: Int) {
        val walk = walks[position]
        val pet = pets.firstOrNull { it.id == walk.petId }
            ?: pets.firstOrNull { it.name == walk.petName }
        if (pet?.photoUri != null) {
            holder.image.setImageURI(Uri.parse(pet.photoUri))
        } else {
            holder.image.setImageResource(R.drawable.ic_pets)
        }
        holder.title.text = "${walk.petName} • ${
            DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
                .format(Date(walk.startedAt))
        }"
        holder.details.text = "${formatDuration(durationInSeconds(walk))} • ${
            String.format(Locale.getDefault(), "%.2f km", walk.distanceKm)
        } • ${walk.steps} passos"
        holder.itemView.setOnClickListener { onWalkClick(walk) }
    }

    override fun getItemCount(): Int = walks.size

    fun updateWalks(newWalks: List<Walk>) {
        walks = newWalks
        notifyDataSetChanged()
    }

    private var pets: List<Pet> = emptyList()

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

    private fun formatDuration(seconds: Long): String =
        String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60)

    private fun durationInSeconds(walk: Walk): Long =
        walk.durationSeconds.takeIf { it > 0 } ?: walk.durationMinutes * 60L
}
