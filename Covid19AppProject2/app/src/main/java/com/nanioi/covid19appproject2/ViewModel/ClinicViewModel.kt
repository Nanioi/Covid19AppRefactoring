package com.nanioi.covid19appproject2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.data.ClinicResponse
import com.nanioi.covid19appproject2.repository.Repository.clinicLocationApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClinicViewModel : ViewModel(){

    private var _clinicLiveData = MutableLiveData<ClinicResponse>()
    val clinicLiveData : LiveData<ClinicResponse> = _clinicLiveData

    fun fetchData(param:HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){

        val responseData = clinicLocationApiService.getStatus(param)

        withContext(Dispatchers.Main) {
            _clinicLiveData.value = responseData
        }

    }
}