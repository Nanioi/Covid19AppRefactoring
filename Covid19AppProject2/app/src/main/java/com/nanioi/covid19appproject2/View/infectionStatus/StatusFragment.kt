package com.nanioi.covid19appproject2.View.infectionStatus

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.R
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.View.infectionStatus.StatusState
import com.nanioi.covid19appproject2.ViewModel.StatusViewModel
import com.nanioi.covid19appproject2.data.InfectionStatusResponse
import com.nanioi.covid19appproject2.data.StatusItem
import com.nanioi.covid19appproject2.databinding.FragmentStatusBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class StatusFragment : Fragment(layout.fragment_status){

    private lateinit var binding: FragmentStatusBinding
    private val key = BuildConfig.OPENAPI_SERVICE_KEY
    private lateinit var anim:Animation
    private val scope = MainScope()

    private val statusViewModel : StatusViewModel by lazy{
        ViewModelProvider(this).get(StatusViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentStatusBinding = FragmentStatusBinding.bind(view)
        binding = fragmentStatusBinding

        statusViewModel.init(requireContext())

        initViews()
        statusViewModel.fetchData(setUpPrameter())
        observeStatusState()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
    private fun initViews() = with(binding) {
        refreshLayout.setOnRefreshListener {
            statusViewModel.fetchData(setUpPrameter())
        }

        anim = AlphaAnimation(0.0f,1.0f) // ????????? ?????? ?????????
        anim.apply {
            duration = 100 // ????????????, ???????????? ???????????? ???????????????
            startOffset = 500 // ?????? ?????????????????? ?????? ??? ?????? ?????????????????? ???????????? ??? ?????? ?????? ??????
            repeatMode = Animation.REVERSE // ??????????????? ??????
            repeatCount = Animation.INFINITE // ?????? ???????????????
        }

        chartTextView.startAnimation(anim)
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

    @SuppressLint("SetTextI18n")
    private fun handleSuccess(state: StatusState.Success) = with(binding) {
        progressBar.isGone = true
        val status = state.statusList
        dailyUpNum.setText(status!![3].status_cnt.toString())
        newAdmissionNum.setText(status!![2].status_cnt.toString())
        inpatientNum.setText(status!![1].status_cnt.toString())
        deathNum.setText(status!![0].status_cnt.toString())

        // ?????? ?????????
        val newInfectionList = state.newInfectionList
        setBarChart(newInfectionList)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setBarChart(statusList : List<StatusItem>) = with(binding){
        val Chart = newChart

        Chart.run{
            description.isEnabled = false // ?????? ?????? ????????? ???????????? description
            setMaxVisibleValueCount(7) // ?????? ????????? ????????? ??????
            setPinchZoom(false) // ?????????( ??? ??????????????? ??????/??????)
            setDrawBarShadow(false) // ?????????
            setDrawGridBackground(false) // ????????????
            setScaleEnabled(false) // Zoom In/Out
        }

        var new_decide_value: ArrayList<BarEntry> = arrayListOf()
        var xLabel: ArrayList<String> = arrayListOf()

        var new_decide: Int
        var statedt: String
        var c = 0f

        //Input Data
        for (i in 6 downTo 0 ) {
            new_decide = statusList[i].decideCnt!!.toInt()
            new_decide -= statusList[i+1].decideCnt!!.toInt()
            new_decide_value.add(BarEntry(c, new_decide.toFloat()))
            c += 1f
            statedt = statusList[i].stateDt!!
            xLabel.add(statedt)
        }
        val dataSet = BarDataSet(new_decide_value, "?????? ?????? ????????????")
        dataSet.color = Color.RED

        val barDate = BarData()
        barDate.addDataSet(dataSet)
        barDate.barWidth = 0.3f // ?????? ??????

        Chart.run{
            this.data = barDate
            setFitBars(true)
            invalidate()

            axisLeft.run{ // ?????? Y???
                axisMaximum = 500000f // ?????????
                axisMinimum = 0f // ?????????
                granularity = 100000f // ???????????? ??? ?????????
                setDrawLabels(true) // ??? ????????? ??????
                setDrawAxisLine(false) // ????????????
                textSize = 10f // ????????? ??????
            }
            xAxis.run{
                position = XAxis.XAxisPosition.BOTTOM // X????????? ??????
                granularity = 1f // ??????
                textSize = 10f
                setDrawAxisLine(true) // ??? ?????????
                setDrawGridLines(false) // ??????
                setValueFormatter(object : ValueFormatter() { // ??? ????????? ??????
                    val pattern = "MM/dd"
                    private val mFormat = SimpleDateFormat(pattern)
                    private val inputFormat = SimpleDateFormat("yyyyMMdd")
                    override fun getFormattedValue(value: Float): String {
                        val intValue: Int = value.toInt()
                        if (intValue < 0 || intValue >=  xLabel.size) return ""
                        else return mFormat.format(inputFormat.parse(xLabel[intValue]))
                    }
                })
            }
            axisRight.isEnabled = false // ????????? y??? ????????????
            legend.isEnabled = false // ?????? ?????? ??????
        }
        val marker : MarkerView = MyMarkerView(activity, layoutResource = R.layout.maker)
        Chart.marker = marker
        Chart.data = barDate
    }

    private fun handleError() {
        Toast.makeText(context,"???????????? ???????????? ???????????????. ",Toast.LENGTH_SHORT).show()
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
            "serviceKey" to key,
            "pageNo" to "1",
            "numOfRows" to "10",
            "startCreateDt" to "20220306",
            "endCreateDt" to dt.toString()
        )
    }
}