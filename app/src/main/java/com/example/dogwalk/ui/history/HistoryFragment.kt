package com.example.dogwalk.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalk.model.Walk
import com.example.dogwalk.R
import java.util.Locale
import androidx.lifecycle.ViewModelProvider

class HistoryFragment : Fragment() {

    private lateinit var adapter: WalkAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_history,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerWalks)
        adapter = WalkAdapter(emptyList(), ::showWalkDetails)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        viewModel.walks.observe(viewLifecycleOwner, ::renderWalks)
        viewModel.pets.observe(viewLifecycleOwner, adapter::updatePets)
    }

    private fun renderWalks(walks: List<Walk>) {
        adapter.updateWalks(walks)
        requireView().findViewById<View>(R.id.emptyHistory).visibility =
            if (walks.isEmpty()) View.VISIBLE else View.GONE
        requireView().findViewById<android.widget.TextView>(R.id.historyTotalWalks).text =
            "${walks.size} passeios"
        requireView().findViewById<android.widget.TextView>(R.id.historyTotalDistance).text =
            "${String.format(Locale.getDefault(), "%.1f", walks.sumOf { it.distanceKm })} km"
        requireView().findViewById<android.widget.TextView>(R.id.historyTotalTime).text =
            formatDuration(walks.sumOf { it.durationSeconds })
    }

    private fun showWalkDetails(walk: Walk) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Detalhes do passeio")
            .setMessage(
                "${walk.petName}\n" +
                    "${                    formatDuration(durationInSeconds(walk))} • " +
                    "${String.format(Locale.getDefault(), "%.2f km", walk.distanceKm)} • " +
                    "${walk.steps} passos"
            )
            .setPositiveButton("Fechar", null)
            .show()
    }

    private fun formatDuration(seconds: Long): String =
        String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60)

    private fun durationInSeconds(walk: Walk): Long =
        walk.durationSeconds.takeIf { it > 0 } ?: walk.durationMinutes * 60L
}