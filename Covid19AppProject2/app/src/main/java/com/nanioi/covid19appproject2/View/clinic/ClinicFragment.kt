package com.nanioi.covid19appproject2.View.clinic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Color
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
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.nanioi.covid19appproject2.Model.db.ClinicDatabase
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationLatLng
import com.nanioi.covid19appproject2.R
import com.nanioi.covid19appproject2.adapters.ClinicListAdapter
import com.nanioi.covid19appproject2.adapters.ClinicViewPagerAdapter
import com.nanioi.covid19appproject2.databinding.FragmentClinicBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class ClinicFragment : Fragment(R.layout.fragment_clinic), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var binding: FragmentClinicBinding
    private lateinit var mContext: Context
    private val scope = MainScope()
    private val mapView: MapView by lazy {
        binding.mapView
    }
    private lateinit var naverMap: NaverMap
    private lateinit var currentLocationButton : LocationButtonView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mGeocoder: Geocoder

    private val clinicListAdapter = ClinicListAdapter()
    private val viewPagerAdapter = ClinicViewPagerAdapter()

    var clinicLatLngList = listOf<ClinicLocationLatLng>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentClinicBinding = FragmentClinicBinding.bind(view)
        binding = fragmentClinicBinding
        currentLocationButton = binding.currentLocationButton

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val clinicListRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        clinicListRecyclerView?.apply {
            adapter = clinicListAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
        binding.clinicViewPager.apply{
            adapter = viewPagerAdapter

            // viewPage?????? ??? ??????, ?????? ????????? ?????? ???
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val selectedClinic = viewPagerAdapter.currentList[position]
                    val clinic = clinicLatLngList.find { it.id == selectedClinic.id }
                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(clinic!!.x, clinic.y))
                        .animate(CameraAnimation.Easing)
                    naverMap.moveCamera(cameraUpdate)
                }
            })
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
        scope.cancel()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap){
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        var latitude = 37.497885
        var longitude = 127.027512

        //????????? ?????? ????????????
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false // ?????? ?????? ????????????
        currentLocationButton.map=naverMap

        locationSource = FusedLocationSource(this, REQUEST_ACCESS_LOCATION_PERMISSIONS)
        naverMap.locationSource = locationSource

        latitude = naverMap.cameraPosition.target.latitude
        longitude = naverMap.cameraPosition.target.longitude

        Log.d(TAG, "?????? : ${latitude}, ?????? : ${longitude}")

        mGeocoder = Geocoder(mContext, Locale.KOREA)
        val address = mGeocoder.getFromLocation(latitude,longitude,1)
        Log.d(TAG, "?????? : ${address}")
        Log.d(TAG, "??? : ${address[0].subLocality}")
        Log.d(TAG, "??? : ${address[0].adminArea.substring(0,2)}")

        getClinicLocationInfo(address[0].adminArea.substring(0,2),address[0].subLocality)

        //?????? ????????? ?????? ?????? ??????
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(cameraUpdate)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != REQUEST_ACCESS_LOCATION_PERMISSIONS) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }

    }

    // ?????? ??????????????? ????????? ????????????
    private fun getClinicLocationInfo(city:String,sigungu:String){

        val ClinicLocationDB = Room.databaseBuilder(
            requireContext(),
            ClinicDatabase::class.java,
            ClinicDatabase.DB_NAME
        ).build()

        val assetManager : AssetManager = resources.assets
        val inputStream : InputStream = assetManager.open("clinic_info.txt")

        inputStream.bufferedReader().readLines().forEach {
            var token = it.split("\t")
            Log.d("db_test","${token}")
            var item = ClinicLocationEntity(token[0].toInt(),token[1],token[2],token[3],token[4],token[5],token[6],token[7],token[8],token[9])
            CoroutineScope(Dispatchers.Main).launch {
                ClinicLocationDB.clinicDao().insert(item)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            val output = ClinicLocationDB.clinicDao().getLocationAround(city,sigungu)

            clinicListAdapter.submitList(output)
            viewPagerAdapter.submitList(output)
            getClinicLocationLatLng(output?: listOf())
        }
    }

    //?????? -> ?????? ??????
    private fun getClinicLocationLatLng(clinicList: List<ClinicLocationEntity>){
        val LatLngList = mutableListOf<ClinicLocationLatLng>()
        clinicList.forEach { clinic->
            //todo ?????? -> ????????????
            var item : ClinicLocationLatLng
            val output = mGeocoder.getFromLocationName(clinic.address,1)
            Log.d(TAG, "?????? : ${output[0].latitude}, ?????? : ${output[0].longitude}")
            item = ClinicLocationLatLng(clinic.id,output[0].latitude,output[0].longitude)
            LatLngList.add(item)
        }
        Log.d(TAG,"??????????????? : ${LatLngList}")
        clinicLatLngList = LatLngList
        updateMarker(LatLngList)
    }

    //?????? ????????????
    private fun updateMarker(clinicList: List<ClinicLocationLatLng>) {

        clinicList.forEach { clinic->
            //????????????
            val marker = Marker()
            marker.position = LatLng(clinic.x, clinic.y)
            marker.map = naverMap
            marker.onClickListener = this
            marker.tag= clinic.id
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
        }
    }

    companion object {
        private val TAG = "ClinicFragment"
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
    }

    //?????? ?????? ??? ?????? ??????????????? ??????
    override fun onClick(overlay: Overlay): Boolean = with(binding) {
        val seletedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag
        }
        seletedModel?.let{
            val position = viewPagerAdapter.currentList.indexOf(it)
            clinicViewPager.currentItem = position
        }
        return true
    }

}