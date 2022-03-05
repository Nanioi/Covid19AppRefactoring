package com.nanioi.covid19appproject2.View.clinic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import com.nanioi.covid19appproject2.databinding.FragmentClinicBinding
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding
import com.nanioi.covid19appproject2.repository.Repository
import kotlinx.coroutines.*
import java.io.InputStream

class ClinicFragment : Fragment(R.layout.fragment_clinic) {

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 1001
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null

    private lateinit var binding:FragmentClinicBinding
    private lateinit var mContext: Context
    private val scope = MainScope()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentClinicBinding = FragmentClinicBinding.bind(view)
        binding = fragmentClinicBinding

        initVariables()
        requestLocationPermissions()

    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 접근권한 부여되었는지 확인하기
        val locationPermissionGranted =
            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!locationPermissionGranted) {
            activity?.finish()
        } else {
            //fetchData
            fetchClinicLocationData()
        }
    }

    private fun requestLocationPermissions() {
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
    private fun fetchClinicLocationData() {
        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            ).addOnSuccessListener { location ->
                //실제로 api를 호출하는 시점
                scope.launch {
                    try {
                        val regionInfo = Repository.getRegionInfo(
                            location.latitude,
                            location.longitude)

                        val locationList = getClinicLocationInfo(
                            regionInfo.region1depthName.toString()
                            , regionInfo.region2depthName.toString())

                        Log.d( "regionInfo region1depthName :  ", regionInfo.region1depthName.toString())
                        Log.d( "regionInfo region2depthName :  ", regionInfo.region2depthName.toString())
                        Log.d( "location List : " , locationList.toString())
                        //todo list 화면 띄우기


                    }catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
    }

    private fun getClinicLocationInfo(city : String, sigungu : String) : List<ClinicLocationEntity>{

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
            locationList = ClinicLocationDB.clinicDao().getLocationAround(city,sigungu)

        }
        return locationList
    }


}