package com.example.dogwalk.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dogwalk.R
import com.example.dogwalk.MainActivity
import com.example.dogwalk.model.Walk
import com.example.dogwalk.model.Pet
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var pets: List<Pet> = emptyList()
    private var walks: List<Walk> = emptyList()

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
            .setOnClickListener { startWalkWithPet() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.walks.observe(viewLifecycleOwner) {
            walks = it
            renderWalks()
        }
        viewModel.pets.observe(viewLifecycleOwner) {
            pets = it
            renderWalks()
        }
    }

    private fun startWalkWithPet() {
        when {
            pets.isEmpty() -> {
                android.widget.Toast.makeText(
                    requireContext(),
                    "Cadastre um pet antes de iniciar um passeio",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            pets.size == 1 -> openWalk(pets.first())
            else -> showPetPicker(pets)
        }
    }

    private fun showPetPicker(pets: List<Pet>) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Escolha o pet")
            .setItems(pets.map { it.name }.toTypedArray()) { _, which ->
                openWalk(pets[which])
            }
            .show()
    }

    private fun openWalk(pet: Pet) {
        (activity as? MainActivity)?.showWalkScreen(pet.id)
    }

    private fun renderWalks() {
        val view = view ?: return
        val weekWalks = walks.filter { it.startedAt >= startOfCurrentWeek() }
        val totalSeconds = weekWalks.sumOf(::durationInSeconds)
        val totalDistance = weekWalks.sumOf { it.distanceKm }
        val totalSteps = weekWalks.sumOf { it.steps }

        view.findViewById<android.widget.TextView>(R.id.homeWeekWalks).text =
            "${weekWalks.size} passeios"
        view.findViewById<android.widget.TextView>(R.id.homeWeekSummary).text =
            "${formatDistance(totalDistance)} • ${formatDuration(totalSeconds)} • " +
                "${formatSteps(totalSteps)} passos"

        val latest = walks.firstOrNull()
        val latestPet = latest?.let { walk ->
            pets.firstOrNull { it.id == walk.petId }
                ?: pets.firstOrNull { it.name == walk.petName }
        }
        val latestImage = view.findViewById<android.widget.ImageView>(R.id.homeLastWalkImage)
        if (latestPet?.photoUri != null) {
            latestImage.setImageURI(android.net.Uri.parse(latestPet.photoUri))
        } else {
            latestImage.setImageResource(R.drawable.ic_pets)
        }
        view.findViewById<android.widget.TextView>(R.id.homeLastWalkPet).text =
            latest?.petName ?: "Nenhum passeio"
        view.findViewById<android.widget.TextView>(R.id.homeLastWalkDate).text =
            latest?.let { formatDate(it.startedAt) } ?: "Ainda não há passeios registrados"
        view.findViewById<android.widget.TextView>(R.id.homeLastWalkDetails).text =
            latest?.let {
                "${formatDuration(durationInSeconds(it))} • ${formatDistance(it.distanceKm)} • " +
                    "${formatSteps(it.steps)} passos"
            } ?: "Inicie um passeio para vê-lo aqui"
    }

    private fun startOfCurrentWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun formatDate(timestamp: Long): String =
        DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT,
            Locale.getDefault()
        ).format(Date(timestamp))

    private fun formatDistance(distanceKm: Double): String =
        String.format(Locale.getDefault(), "%.1f km", distanceKm)

    private fun formatSteps(steps: Int): String =
        String.format(Locale.getDefault(), "%,d", steps)

    private fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = seconds / 60 % 60
        val remainingSeconds = seconds % 60
        return when {
            hours > 0 -> "${hours}h ${minutes}min"
            minutes > 0 -> "${minutes}min"
            else -> "${remainingSeconds}s"
        }
    }

    private fun durationInSeconds(walk: Walk): Long =
        walk.durationSeconds.takeIf { it > 0 } ?: walk.durationMinutes * 60L
}