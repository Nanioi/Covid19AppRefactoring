package com.nanioi.covid19appproject2.View.clinic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.nanioi.covid19appproject2.Model.db.ClinicDatabase
import com.nanioi.covid19appproject2.Model.db.ClinicDatabase.Companion.DB_NAME
import com.nanioi.covid19appproject2.Model.db.dao.ClinicLocationDao
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.R
import com.nanioi.covid19appproject2.adapters.ClinicListAdapter
import com.nanioi.covid19appproject2.adapters.ClinicViewPagerAdapter
import com.nanioi.covid19appproject2.databinding.FragmentClinicBinding
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding
import com.nanioi.covid19appproject2.repository.Repository
import com.nanioi.covid19appproject2.repository.StatusRepository.Companion.mContext
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.*
import org.koin.android.ext.android.bind
import java.io.InputStream

class ClinicFragment : Fragment(R.layout.fragment_clinic) ,OnMapReadyCallback{

//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private var cancellationTokenSource: CancellationTokenSource? = null

    private lateinit var binding:FragmentClinicBinding
    private lateinit var mContext: Context
    private val scope = MainScope()
    private val mapView : MapView by lazy {
        binding.mapView
    }
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    //private val viewPagerAdapter = ClinicViewPagerAdapter()
    private val clinicListAdapter = ClinicListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentClinicBinding = FragmentClinicBinding.bind(view)
        binding = fragmentClinicBinding

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)

        val clinicListRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        clinicListRecyclerView?.apply {
            adapter = clinicListAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
//        initViews()
//        initVariables()
//        requestLocationPermissions()

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
       // cancellationTokenSource?.cancel()
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

        //초기값위치 변경
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497885, 127.027512))
        naverMap.moveCamera(cameraUpdate)

        //현위치 버튼 얻어오기
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false // 원래 버튼 안보이

        locationSource = FusedLocationSource(this@ClinicFragment,REQUEST_ACCESS_LOCATION_PERMISSIONS  )
        naverMap.locationSource = locationSource

        getClinicLocationInfo()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode != REQUEST_ACCESS_LOCATION_PERMISSIONS){
            return
        }
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if(!locationSource.isActivated){
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }

//        // 접근권한 부여되었는지 확인하기
//        val locationPermissionGranted =
//            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//
//        if (!locationPermissionGranted) {
//            activity?.finish()
//        } else {
//            //fetchData
//            fetchClinicLocationData()
//        }
    }

    private fun initViews() = with(binding) {

        //fetchClinicLocationData()
    }

//    private fun initVariables() {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
//    }
//
//    private fun requestLocationPermissions() {
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            REQUEST_ACCESS_LOCATION_PERMISSIONS
//        )
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun fetchClinicLocationData() {
//        cancellationTokenSource = CancellationTokenSource()
//
//        fusedLocationProviderClient
//            .getCurrentLocation(
//                LocationRequest.PRIORITY_HIGH_ACCURACY,
//                cancellationTokenSource!!.token
//            ).addOnSuccessListener { location ->
//
//                Log.d( "location :  ",  location.latitude.toString())
//
//                //실제로 api를 호출하는 시점
//                scope.launch {
//                    val regionInfo = Repository.getRegionInfo(
//                        location.latitude,
//                        location.longitude)
////                    val locationList = getClinicLocationInfo(
////                        regionInfo.region1depthName.toString()
////                        , regionInfo.region2depthName.toString())
//
//
//                    Log.e("location1 : ", location.latitude.toString() )
//                    Log.e("location2 : ", location.longitude.toString() )
//
////                    regionInfo.region2depthName?.let {
////
////                        val sigungu = regionInfo.region2depthName
////
////                        sigungu.replace(' ','*')
////                        var locationList : List<ClinicLocationEntity> ?= listOf()
////
////                        if ( '*' in sigungu){
////                            val arr = sigungu.split("*")
////                            val sigungu1 = arr.get(0)
////                            val sigungu2 = arr.get(1)
////
////                            val locationList = getClinicLocationInfo(
////                                regionInfo.region1depthName.toString()
////                                , sigungu1)
////                            Log.d( "location List1 : " , locationList.toString())
////                        }else {
////                            locationList = getClinicLocationInfo(
////                                regionInfo.region1depthName.toString(), sigungu
////                            )
////                            Log.d( "location List2 : " , locationList.toString())
////                        }
////                        clinicListAdapter.submitList(locationList)
////                        //initViewsClinicLocation(locationList)
////                    }
////
////                    Log.d( "regionInfo region1depthName :  ", regionInfo.region1depthName.toString())
////                    Log.d( "regionInfo region2depthName :  ", regionInfo.region2depthName.toString())
////                    //todo list 화면 띄우기
//                }
//            }
//    }

    private fun initViewsClinicLocation(locationList: List<ClinicLocationEntity>?) {

    }

    private fun getClinicLocationInfo(){

        val ClinicLocationDB = Room.databaseBuilder(mContext,
            ClinicDatabase::class.java,
            ClinicDatabase.DB_NAME
        ).build()

        val assetManager : AssetManager = resources.assets
        val inputStream : InputStream = assetManager.open("clinic_location_info.txt")
        var locationList : List<ClinicLocationEntity> = listOf()

        inputStream.bufferedReader().readLines().forEach {
            var token = it.split("\t")
            var item = ClinicLocationEntity(token[0].toLong(),token[1],token[2],token[3],token[4],token[5],token[6],token[7],token[8])
            CoroutineScope(Dispatchers.Main).launch {
                ClinicLocationDB.clinicDao().insert(item)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {

            locationList = ClinicLocationDB.clinicDao().getAllLocation()!!
            Log.d("db_test", "$locationList")

//            val location = ClinicLocationDB.clinicDao().getLocationAround(city,sigungu)
//            Log.d("getClinic : " , location.toString())
        }
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 1001
    }
}