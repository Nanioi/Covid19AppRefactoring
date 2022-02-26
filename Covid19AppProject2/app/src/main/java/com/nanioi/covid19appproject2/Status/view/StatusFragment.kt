package com.nanioi.covid19appproject2.Status.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.Status.viewModel.StatusViewModel
import java.util.*
import kotlin.collections.HashMap

class StatusFragment : Fragment(layout.fragment_status){

    private val key = BuildConfig.OPENAPI_SERVICE_KEY

    private val statusViewModel by viewModels<StatusViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusViewModel.fetchData(setUpPrameter())
        observeStatusData()
    }

    private fun observeStatusData(){
        statusViewModel.statusLiveData.observe(viewLifecycleOwner){
            it?.let {
                Log.e("Parsing Status ::", it.toString())
            }
        }
    }

    private fun setUpPrameter(): HashMap<String, String>{

        var dt : Int = 0
        val cal = Calendar.getInstance()
        dt += cal.get(Calendar.YEAR)
        dt *= 100
        dt += cal.get((Calendar.MONTH)+1)
        dt *= 100
        dt += cal.get(Calendar.DATE)

        return hashMapOf(
            "serviceKey" to key.toString(),
            "pageNo" to "1",
            "numOfRows" to "10",
            "startCreateDt" to "20200310",
            "endCreateDt" to "20220101"
        )

    }

}