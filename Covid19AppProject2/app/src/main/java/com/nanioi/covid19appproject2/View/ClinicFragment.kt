package com.nanioi.covid19appproject2.View

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.R
import com.nanioi.covid19appproject2.ViewModel.ClinicViewModel
import java.util.*
import kotlin.collections.HashMap

class ClinicFragment : Fragment(R.layout.fragment_clinic) {

    private val key = BuildConfig.OPENAPI_SERVICE_KEY

    private val clinicViewModel by viewModels<ClinicViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clinicViewModel.fetchData(setUpPrameter())
        observeStatusData()
    }

    private fun observeStatusData(){
        clinicViewModel.clinicLiveData.observe(viewLifecycleOwner){
            it?.let {
                Log.e("Parsing Data ::", it.toString())
            }
        }
    }

    private fun setUpPrameter(): HashMap<String, String>{

        return hashMapOf(
            "serviceKey" to key.toString(),
            "pageNo" to "1",
            "numOfRows" to "100"
        )
    }
}