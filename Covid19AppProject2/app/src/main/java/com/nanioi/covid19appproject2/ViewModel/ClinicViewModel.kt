package com.nanioi.covid19appproject2.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.repository.ClinicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClinicViewModel(
    private val clinicRepository : ClinicRepository
) : ViewModel(){

    private var _clinicLiveData = MutableLiveData<ClinicLocationEntity>()
    val clinicLiveData : LiveData<ClinicLocationEntity> = _clinicLiveData

    private lateinit var clinicInfoList : List<ClinicLocationEntity>


    fun fetchClinicData(city:String,sigungu:String) = viewModelScope.launch(Dispatchers.IO) {
        clinicInfoList = clinicRepository.getClinicLocationAroundInfo(city,sigungu)

        Log.d("clinicViewModel : ", clinicInfoList.toString())
    }

}