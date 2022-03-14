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

        anim = AlphaAnimation(0.0f,1.0f) // 투명도 조절 클래스
        anim.apply {
            duration = 100 // 지속시간, 투명도를 몇초동안 실행할건지
            startOffset = 500 // 한번 애니메이션이 끝난 후 다음 애니메이션이 시작되기 전 잠시 대기 시간
            repeatMode = Animation.REVERSE // 애니메이션 반복
            repeatCount = Animation.INFINITE // 몇번 반복할건지
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

        // 차트 그리기
        val newInfectionList = state.newInfectionList
        setBarChart(newInfectionList)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setBarChart(statusList : List<StatusItem>) = with(binding){
        val Chart = newChart

        Chart.run{
            description.isEnabled = false // 차트 옆에 벼도로 표기되는 description
            setMaxVisibleValueCount(7) // 최대 보이는 그래프 개수
            setPinchZoom(false) // 핀치줌( 두 손가락으로 줌인/아웃)
            setDrawBarShadow(false) // 그림자
            setDrawGridBackground(false) // 격자구조
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
        val dataSet = BarDataSet(new_decide_value, "일별 신규 확진자수")
        dataSet.color = Color.RED

        val barDate = BarData()
        barDate.addDataSet(dataSet)
        barDate.barWidth = 0.3f // 막대 너비

        Chart.run{
            this.data = barDate
            setFitBars(true)
            invalidate()

            axisLeft.run{ // 왼쪽 Y축
                axisMaximum = 500000f // 최대값
                axisMinimum = 0f // 최소값
                granularity = 100000f // 단위마다 선 그리기
                setDrawLabels(true) // 값 적는거 허용
                setDrawAxisLine(false) // 축그리기
                textSize = 10f // 텍스트 크기
            }
            xAxis.run{
                position = XAxis.XAxisPosition.BOTTOM // X축위치 아래
                granularity = 1f // 간격
                textSize = 10f
                setDrawAxisLine(true) // 축 그리기
                setDrawGridLines(false) // 격자
                setValueFormatter(object : ValueFormatter() { // 축 라벨값 설정
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
            axisRight.isEnabled = false // 오른쪽 y축 안보이게
            legend.isEnabled = false // 차트 범례 설정
        }
        val marker : MarkerView = MyMarkerView(activity, layoutResource = R.layout.maker)
        Chart.marker = marker
        Chart.data = barDate
    }

    private fun handleError() {
        Toast.makeText(context,"데이터를 불러오지 못했습니다. ",Toast.LENGTH_SHORT).show()
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