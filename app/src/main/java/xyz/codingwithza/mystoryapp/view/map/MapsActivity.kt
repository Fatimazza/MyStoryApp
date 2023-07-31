package xyz.codingwithza.mystoryapp.view.map

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.databinding.ActivityMapsBinding
import xyz.codingwithza.mystoryapp.databinding.DialogStoryMapBinding
import xyz.codingwithza.mystoryapp.util.loadImage
import xyz.codingwithza.mystoryapp.view.ViewModelFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    companion object {
        private const val DEFAULT_LAT = -6.2088
        private const val DEFAULT_LNG = 106.8456
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initViewModel()
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory)[MapsViewModel::class.java]
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        addMapsControl()
        zoomToDefaultLocation()
        showAllStoriesByLocation()
    }

    private fun showAllStoriesByLocation() {
        val boundsBuilder = LatLngBounds.Builder()
        mapsViewModel.getUserData().observe(this) {
            mapsViewModel.getAllStoriesByLocation().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                        }
                        is Result.Success -> {
                            val stories = result.data.listStory
                            stories.forEach { story ->
                                val latLng = LatLng(story.lat, story.lon)
                                val pin = mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(story.name)
                                        .snippet(story.description)
                                )
                                pin?.tag = story.photoUrl

                                boundsBuilder.include(latLng)
                                val bounds: LatLngBounds = boundsBuilder.build()
                                mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngBounds(
                                        bounds,
                                        resources.displayMetrics.widthPixels,
                                        resources.displayMetrics.heightPixels,
                                        150
                                    )
                                )
                            }
                        }
                        is Result.Error -> {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.load_error) + result.error,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun addMapsControl() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun zoomToDefaultLocation() {
        val jakarta = LatLng(DEFAULT_LAT, DEFAULT_LNG)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 10f))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val dialogMap : DialogStoryMapBinding = DialogStoryMapBinding.inflate(layoutInflater)
        val dialog = Dialog(this@MapsActivity)
        dialog.setContentView(dialogMap.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogMap.apply {
            btnMapsExit.setOnClickListener {
                dialog.dismiss()
            }
            tvMapsName.text = marker.title.toString()
            tvMapsDesc.text = marker.snippet.toString()
            ivMapsImage.loadImage(marker.tag.toString())
        }
        dialog.setCancelable(true)
        dialog.show()
        return false
    }
}
