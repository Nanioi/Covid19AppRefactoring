package com.nanioi.covid19appproject2.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.Model.domain.GetAllDataUseCase
import com.nanioi.covid19appproject2.Model.domain.GetAroundDataUseCase
import com.nanioi.covid19appproject2.Model.domain.InsertDataUseCase
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.View.infectionStatus.StatusState
import com.nanioi.covid19appproject2.data.ClinicResponse
import com.nanioi.covid19appproject2.repository.ClinicLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ClinicViewModel(
    private val getAllDataUseCase: GetAllDataUseCase,
    private val insertDataUseCase: InsertDataUseCase,
    private val getAroundDataUseCase: GetAroundDataUseCase
) : ViewModel(){

    private var _clinicLiveData = MutableLiveData<ClinicLocationEntity>()
    val clinicLiveData : LiveData<ClinicLocationEntity> = _clinicLiveData

    private lateinit var clinicInfoList : List<ClinicLocationEntity>

    fun insertList() {
        insertDataUseCase
    }
//    fun fetchData(param:HashMap<String,String>) = viewModelScope.launch(Dispatchers.IO){
//
//        val responseData = clinicLocationApiService.getStatus(param)
//
//        withContext(Dispatchers.Main) {
//            _clinicLiveData.value = responseData
//        }
//
//    }

    fun fetchClinicData(x:Double,y:Double) = viewModelScope.launch(Dispatchers.IO) {
        clinicInfoList = getAroundDataUseCase(x,y)?: listOf()

        val list = getAllDataUseCase
        Log.d("clinic : ", list.toString())

        Log.d("clinicViewModel : ", clinicInfoList.toString())
    }

}