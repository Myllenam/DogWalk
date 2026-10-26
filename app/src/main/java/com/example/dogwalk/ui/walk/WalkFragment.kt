package com.example.dogwalk.ui.walk

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.dogwalk.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.util.Locale

class WalkFragment : Fragment() {

    private val locationRequestCode = 10
    private val handler = Handler(Looper.getMainLooper())
    private var map: MapView? = null
    private var lastLocation: Location? = null
    private var distanceMeters = 0f
    private var elapsedSeconds = 0L
    private lateinit var timerText: TextView
    private lateinit var distanceText: TextView

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach(::updateLocation)
        }
    }

    private val timer = object : Runnable {
        override fun run() {
            elapsedSeconds++
            timerText.text = formatDuration(elapsedSeconds)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            requireContext(),
            requireContext().getSharedPreferences("osmdroid", 0)
        )
        val mapDirectory = File(requireContext().cacheDir, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = mapDirectory
        Configuration.getInstance().osmdroidTileCache = File(mapDirectory, "tiles")
        val userAgent = "DogWalk/1.0 (Android; https://github.com/Myllenam/DogWalk)"
        Configuration.getInstance().userAgentValue = userAgent
        Configuration.getInstance().userAgentHttpHeader = "User-Agent"
        Configuration.getInstance().additionalHttpRequestProperties["User-Agent"] = userAgent
        Configuration.getInstance().additionalHttpRequestProperties["Referer"] =
            "https://github.com/Myllenam/DogWalk"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_walk, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        timerText = view.findViewById(R.id.walkTimer)
        distanceText = view.findViewById(R.id.walkDistance)
        map = view.findViewById<MapView>(R.id.walkMap).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(-23.5505, -46.6333))
        }
        view.findViewById<View>(R.id.btnFinishWalk).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        view.findViewById<View>(R.id.btnCloseWalk).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        handler.post(timer)
        enableLocation()
    }

    private fun enableLocation() {
        val activity = activity ?: return
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationRequestCode
            )
            return
        }

        val locationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationClient.lastLocation.addOnSuccessListener { location ->
            location?.let(::updateLocation)
        }
        locationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateDistanceMeters(5f)
                .build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun updateLocation(location: Location) {
        val currentPoint = GeoPoint(location.latitude, location.longitude)
        lastLocation?.let {
            val previousPoint = GeoPoint(it.latitude, it.longitude)
            distanceMeters += it.distanceTo(location)
            distanceText.text = String.format(
                Locale.getDefault(),
                "%.2f km",
                distanceMeters / 1000
            )
            (map?.overlays?.firstOrNull { overlay -> overlay is Polyline } as? Polyline)
                ?.addPoint(currentPoint)
                ?: map?.overlays?.add(
                    Polyline().apply {
                        outlinePaint.strokeWidth = 8f
                        setPoints(listOf(previousPoint, currentPoint))
                    }
                )
            map?.invalidate()
        }
        lastLocation = location
        map?.controller?.animateTo(currentPoint)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode &&
            grantResults.any { it == PackageManager.PERMISSION_GRANTED }
        ) {
            enableLocation()
        }
    }

    private fun formatDuration(seconds: Long): String =
        String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            seconds / 3600,
            seconds / 60 % 60,
            seconds % 60
        )

    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        map?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        handler.removeCallbacks(timer)
        activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
                .removeLocationUpdates(locationCallback)
        }
        map = null
        super.onDestroyView()
    }
}
