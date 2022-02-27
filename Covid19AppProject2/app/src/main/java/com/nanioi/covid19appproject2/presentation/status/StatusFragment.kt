package com.nanioi.covid19appproject2.presentation.status

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding
import com.nanioi.covid19appproject2.domain.extensions.toast
import java.util.*
import kotlin.collections.HashMap

class StatusFragment : Fragment(layout.fragment_status){

    private lateinit var binding: FragmentStatusBinding

    private val statusViewModel by lazy {
        ViewModelProvider(this).get(StatusViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStatusBinding = FragmentStatusBinding.bind(view)
        binding = fragmentStatusBinding

        initViews()
        statusViewModel.fetchData(setUpPrameter())
        observeStatusState()
    }
    private fun initViews() {
        statusViewModel.fetchData(setUpPrameter())
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

        Log.d("Parsing : " , state.statusList.toString())

        // 차트 그리기 todo


    }

    private fun handleError() {
       //todo
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
            "serviceKey" to BuildConfig.OPENAPI_SERVICE_KEY,
            "pageNo" to "1",
            "numOfRows" to "10",
            "startCreateDt" to "20200310",
            "endCreateDt" to dt.toString()
        )

    }
}