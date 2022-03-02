package com.nanioi.covid19appproject2.View.clinic

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.nanioi.covid19appproject2.Model.db.ClinicDatabase
import com.nanioi.covid19appproject2.Model.db.ClinicDatabase.Companion.DB_NAME
import com.nanioi.covid19appproject2.Model.db.dao.ClinicLocationDao
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class ClinicFragment : Fragment(R.layout.fragment_clinic) {

//    private val key = BuildConfig.OPENAPI_SERVICE_KEY
//    private val clinicViewModel by viewModels<ClinicViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getClinicLocationInfo()

//        clinicViewModel.fetchData(setUpPrameter())
//        observeStatusData()
    }

    private fun getClinicLocationInfo(){

        val ClinicLocationDB = Room.databaseBuilder(requireContext(),ClinicDatabase::class.java,DB_NAME).build()

        val assetManager : AssetManager = resources.assets
        val inputStream :InputStream = assetManager.open("clinic_location_info.txt")

        inputStream.bufferedReader().readLines().forEach {
            var token = it.split("\t")
            var item = ClinicLocationEntity(token[0].toLong(),token[1],token[2],token[3],token[4],token[5],token[6],token[7],token[8])
            CoroutineScope(Dispatchers.Main).launch {
                ClinicLocationDB.clinicDao().insert(item)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            var output = ClinicLocationDB.clinicDao().getAllLocaion()
            Log.d("db_test", "$output")
        }
    }


//    private fun observeStatusData(){
//        clinicViewModel.clinicLiveData.observe(viewLifecycleOwner){
//            it?.let {
//                Log.e("Parsing Data ::", it.toString())
//            }
//        }
//    }
//    private fun setUpPrameter(): HashMap<String, String>{
//
//        return hashMapOf(
//            "serviceKey" to key.toString(),
//            "pageNo" to "1",
//            "numOfRows" to "100"
//        )
//    }
}