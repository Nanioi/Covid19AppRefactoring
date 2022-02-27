package com.nanioi.covid19appproject2.presentation.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.data.InfectionStatus
import com.nanioi.covid19appproject2.domain.status.GetStatusListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class StatusViewModel(
    private val getStatusListUseCase: GetStatusListUseCase
) : ViewModel(){

    private var _statusState = MutableLiveData<StatusState>(StatusState.UnInitialized)
    val statusState : LiveData<StatusState> = _statusState

    private lateinit var statusList : List<InfectionStatus>

    fun fetchData(param : HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){
        setState(StatusState.Loading)

        getStatusListUseCase(param)?.let { list ->
            statusList = list
            setState(
                StatusState.Success(list)
            )
        } ?: kotlin.run {
            setState(StatusState.Error)
        }

    }
    private fun setState(state: StatusState) {
        _statusState.postValue(state)
    }
}

