package com.nanioi.covid19appproject2.Status.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.data.InfectionStatus
import com.nanioi.covid19appproject2.network.StatusRetrofitClient.statusApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel : ViewModel(){

    private var _statusLiveData = MutableLiveData<InfectionStatus>()
    val statusLiveData : LiveData<InfectionStatus> = _statusLiveData

    fun fetchData(param:HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){

        val responseData = statusApi.getStatus(param)

        withContext(Dispatchers.Main) {
            _statusLiveData.value = responseData
        }

    }
}