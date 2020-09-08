package hu.prooktatas.djs.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hu.prooktatas.djs.R
import hu.prooktatas.djs.TAG
import java.io.IOException

class PreferredLoacationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private var googleMap: GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var tvZipCode: TextView
    private lateinit var tvCountry: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvAddress: TextView

    private val favPlacesMap = mapOf<LatLng, String>(
        LatLng(46.251872, 20.160686) to "Szeged",
        LatLng(46.522234, 17.532774) to "Libickozma",
        LatLng(48.033358, 19.574947) to "Nagylóc"
    )

    private val requestCodeLocationPermission = 4567

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_preferred_location_with_info, container, false)

        tvZipCode = rootView.findViewById(R.id.tvZipCode)
        tvCountry = rootView.findViewById(R.id.tvCountry)
        tvCity = rootView.findViewById(R.id.tvCity)
        tvAddress = rootView.findViewById(R.id.tvAddress)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        Log.d(TAG, "mapFragment: $mapFragment")
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.d(TAG, "onMapReady")
        googleMap = p0
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        val sydney = LatLng(-34.0, 151.0)
//        googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setOnMapClickListener(this)

        favPlacesMap.forEach {
            val useCustomColor = it.value == "Libickozma"
            val marker = createMarker(it.key, it.value, useCustomColor)
            googleMap?.addMarker(marker)
        }

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(getZoomCenter(), 12.0f))

        requestLocationPermissionIfNeeded()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCodeLocationPermission) {
            Log.d(TAG, "permissions: ${permissions.toList()}")
            Log.d(TAG, "grantResults: ${grantResults.toList()}")

            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted")
            } else {
                Log.d(TAG, "Permission denied")
            }

        }
    }

    override fun onMapClick(p0: LatLng?) {
        val address = p0?.let {
            retrieveAddress(it)
        }

        address?.let {
            tvZipCode.text = it.postalCode
            tvCountry.text = it.countryName
            tvCity.text = it.locality
            tvAddress.text = "${it.thoroughfare} ${it.featureName}"
        }

        Log.d(TAG, "Adress: $address")
    }

    private fun createMarker(
        pos: LatLng,
        text: String,
        useCustomColor: Boolean = false,
        useCustomIcon: Boolean = false
    ): MarkerOptions {
        return MarkerOptions().also {
            it.position(pos)
            it.title(text)

            if (useCustomIcon) {

                it.icon(
                    BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(
                            resources,
                            R.mipmap.ic_user_location
                        )
                    )
                )
            } else {
                if (useCustomColor) {
                    it.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                }
            }
        }
    }

    private fun getZoomCenter(): LatLng {
        return favPlacesMap.filter {
            it.value == "Nagylóc"
        }.keys.first()
    }

    private fun requestLocationPermissionIfNeeded() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestCodeLocationPermission
            )

            return
        } else {
            googleMap?.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener {

                val current = LatLng(it.latitude, it.longitude)
                Log.d(TAG, "Device location:  ${it.latitude}, ${it.longitude}")

                val currentLocationMarker = createMarker(current, "Itt állsz!", useCustomIcon = true)
                googleMap?.addMarker(currentLocationMarker)

                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f))

            }
        }
    }


    private fun retrieveAddress(position: LatLng): Address? {
        val geocoder = Geocoder(activity)
        try {
            return geocoder.getFromLocation(position.latitude, position.longitude, 1).firstOrNull()
        } catch (e: IOException) {
            Log.e(TAG, e.localizedMessage)
        }

        return null
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d(TAG, "Marker clicked! ${p0?.title}")
        return false
    }


}