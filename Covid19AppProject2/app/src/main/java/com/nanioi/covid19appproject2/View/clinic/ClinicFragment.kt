package com.nanioi.covid19appproject2.View.clinic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.gun0912.tedpermission.rx3.TedPermission
import com.nanioi.covid19appproject2.R
import com.nanioi.covid19appproject2.adapters.ClinicListAdapter
import com.nanioi.covid19appproject2.databinding.FragmentClinicBinding
import com.nanioi.covid19appproject2.repository.Repository
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ClinicFragment : Fragment(R.layout.fragment_clinic), OnMapReadyCallback {

    private lateinit var binding: FragmentClinicBinding
    private lateinit var mContext: Context
    private val scope = MainScope()
    private val mapView: MapView by lazy {
        binding.mapView
    }
    private lateinit var naverMap: NaverMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private var cancellationTokenSource: CancellationTokenSource? = null

    private lateinit var mGeocoder: Geocoder

    private val clinicListAdapter = ClinicListAdapter()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentClinicBinding = FragmentClinicBinding.bind(view)
        binding = fragmentClinicBinding

        //initViews()
        initVariables()
        requestLocationPermissions()

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val clinicListRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        clinicListRecyclerView?.apply {
            adapter = clinicListAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        //현위치 버튼 얻어오기
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false // 원래 버튼 안보이게

        naverMap.locationSource = locationSource

        var latitude = 37.497885
        var longitude = 127.027512

        latitude = naverMap.cameraPosition.target.latitude
        longitude = naverMap.cameraPosition.target.latitude

        Log.d(TAG, "위도 : ${latitude}, 경도 : ${longitude}")

        // 위치 변경
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(cameraUpdate)
    }


    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 접근권한 부여되었는지 확인하기
        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED


        if (!locationPermissionGranted) {
            activity?.finish()
        } else {
            //fetchData
            getCurrentLocationAddress()
        }
    }

    private fun requestLocationPermissions() {

        locationSource =
            FusedLocationSource(this, REQUEST_ACCESS_LOCATION_PERMISSIONS)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAddress() {
        mGeocoder = Geocoder(mContext, Locale.KOREA)
        var address: ArrayList<Address>

        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->
            Log.d(TAG, "위도 : ${location.latitude}, 경도 : ${location.longitude}")
            // todo 주소변환
            scope.launch {
                try {
                    val regionInfo = Repository.getRegionInfo(
                        location.latitude,
                        location.longitude)

                    Log.d(TAG, regionInfo.toString())

                }catch (exception: Exception) {
                    exception.printStackTrace()
                    Log.d(TAG, "XX")
                }
            }
        }
    }

    companion object {
        private val TAG = "ClinicFragment"
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 1001
    }
}