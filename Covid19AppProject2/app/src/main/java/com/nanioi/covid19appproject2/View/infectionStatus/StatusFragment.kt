package com.nanioi.covid19appproject2.View.infectionStatus

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.View.infectionStatus.StatusState
import com.nanioi.covid19appproject2.ViewModel.StatusViewModel
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding

class StatusFragment : Fragment(layout.fragment_status){

    private lateinit var binding: FragmentStatusBinding

    //    private val key = BuildConfig.OPENAPI_SERVICE_KEY

    private val statusViewModel : StatusViewModel by lazy{
        ViewModelProvider(this).get(StatusViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStatusBinding = FragmentStatusBinding.bind(view)
        binding = fragmentStatusBinding

        statusViewModel.init(requireContext())

        initViews()
        statusViewModel.fetchData()
        observeStatusState()
    }
    private fun initViews() {
        statusViewModel.fetchData()
        // todo date 업데이트
    }

    private fun observeStatusState() = statusViewModel.statusState.observe(viewLifecycleOwner) {
        when (it) {
            is StatusState.UnInitialized -> initViews()
            is StatusState.Loading -> handleLoading()
            is StatusState.Success -> handleSuccess(it)
            is StatusState.Error -> handleError()
        }
    }
    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: StatusState.Success) = with(binding) {
        progressBar.isGone = true
        val status = state.statusList
        dailyUpNum.setText(status!![3].status_cnt.toString())
        newAdmissionNum.setText(status!![2].status_cnt.toString())
        inpatientNum.setText(status!![1].status_cnt.toString())
        deathNum.setText(status!![0].status_cnt.toString())

        Log.d("Parsing : " , state.statusList.toString())

        // 차트 그리기 todo


    }

    private fun handleError() {
        //todo
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