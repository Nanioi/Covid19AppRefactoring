package com.nanioi.covid19appproject2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.Model.network.ClinicLocationRetrofitClient.CLINIC_API
import com.nanioi.covid19appproject2.data.ClinicResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClinicViewModel : ViewModel(){

    private var _clinicLiveData = MutableLiveData<ClinicResponse>()
    val clinicLiveData : LiveData<ClinicResponse> = _clinicLiveData

    fun fetchData(param:HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){

        val responseData = CLINIC_API.getStatus(param)

        withContext(Dispatchers.Main) {
            _clinicLiveData.value = responseData
        }

    }
}