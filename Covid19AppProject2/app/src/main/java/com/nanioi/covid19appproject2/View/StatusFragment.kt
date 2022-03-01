package com.nanioi.covid19appproject2.View

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.Model.network.JSoupParsingTask
import com.nanioi.covid19appproject2.Model.network.Url
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.ViewModel.ClinicViewModel
import com.nanioi.covid19appproject2.ViewModel.StatusState
import com.nanioi.covid19appproject2.ViewModel.StatusViewModel
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.HashMap

class StatusFragment : Fragment(layout.fragment_status){

    private lateinit var binding: FragmentStatusBinding

    //private val statusViewModel by viewModels<StatusViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStatusBinding = FragmentStatusBinding.bind(view)
        binding = fragmentStatusBinding

        JSoupParsingTask().execute()

//        initViews()
//        statusViewModel.fetchData()
//        observeStatusState()
    }
//    private fun initViews() {
//        statusViewModel.fetchData()
//        // todo date 업데이트
//    }
//
//    private fun observeStatusState() = statusViewModel.statusState.observe(viewLifecycleOwner) {
//        when (it) {
//            is StatusState.UnInitialized -> initViews()
//            is StatusState.Loading -> handleLoading()
//            is StatusState.Success -> handleSuccess(it)
//            is StatusState.Error -> handleError()
//        }
//    }
//    private fun handleLoading() = with(binding) {
//        progressBar.isVisible = true
//    }
//
//    private fun handleSuccess(state: StatusState.Success) = with(binding) {
//        progressBar.isGone = true
//        val status = state.statusList
//
//        Log.d("Parsing : " , state.statusList.toString())
//
//        // 차트 그리기 todo
//
//
//    }
//
//    private fun handleError() {
//        //todo
//    }

    inner class JSoupParsingTask () :
        AsyncTask<Any?, Any?, List<InfectionStatus>>() { //input, progress update type, result type
        var statusList = mutableListOf<InfectionStatus>()
        override fun doInBackground(vararg params: Any?): List<InfectionStatus>? {
            val doc: Document = Jsoup.connect(Url.Infection_Status_URL).get()
            val elts: Elements = doc.select("div.caseTable div")


            elts.forEachIndexed { index, elem ->
                val status_name = elem.select("div strong.ca_top").text()
                val status_cnt = elem.select("div ul li dl dd.ca_value").text().replace(" ","*")

                val arr = status_cnt.split("*")
                val daily_cnt = arr.get(0)
                val percent = arr.get(1)

                var status = InfectionStatus(status_name,daily_cnt)
                statusList.add(status)

            }
            return statusList
        }

        override fun onPostExecute(result: List<InfectionStatus>?) = with(binding) {
            super.onPostExecute(result)

            dailyUpNum.setText(result!![3].status_cnt.toString())
            newAdmissionNum.setText(result!![2].status_cnt.toString())
            inpatientNum.setText(result!![1].status_cnt.toString())
            deathNum.setText(result!![0].status_cnt.toString())

        }
    }
}