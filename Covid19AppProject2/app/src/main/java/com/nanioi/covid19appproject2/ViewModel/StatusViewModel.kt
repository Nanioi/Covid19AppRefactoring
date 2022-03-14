package com.nanioi.covid19appproject2.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.Model.network.StatusRetrofitClient.infectionStatusApiService
import com.nanioi.covid19appproject2.View.infectionStatus.StatusState
import com.nanioi.covid19appproject2.data.InfectionStatusResponse
import com.nanioi.covid19appproject2.data.StatusItem
import com.nanioi.covid19appproject2.repository.StatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusViewModel:ViewModel() {
    private var _statusState = MutableLiveData<StatusState>(StatusState.UnInitialized)
    val statusState : LiveData<StatusState> = _statusState

    private lateinit var statusList : List<InfectionStatus>
    private lateinit var newInfectionList : List<StatusItem>
    private val statusRepository = StatusRepository()

    fun init(context: Context){
        if (_statusState.value !=null)
            return
    }

    fun fetchData(param : HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){
        setState(StatusState.Loading)

        val response = infectionStatusApiService.getStatus(param)
        newInfectionList = response.body.items.item

        statusList = statusRepository.getStatusData()

        if (statusList.isEmpty() or newInfectionList.isEmpty()) {
            setState(StatusState.Error)
            Log.d("StatusViewModel Fetch Error ", statusList.toString())
        }else{
            setState(StatusState.Success(statusList,newInfectionList))
            Log.d("StatusViewModel Fetch Success ",statusList.toString() )
            Log.d("NewStatusViewModel Fetch Success ",newInfectionList.toString() )
        }


    }
    private fun setState(state: StatusState) {
        _statusState.postValue(state)
    }
}