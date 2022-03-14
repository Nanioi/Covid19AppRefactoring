package com.nanioi.covid19appproject2.View.infectionStatus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.nanioi.covid19appproject2.R

@SuppressLint("SetTextI18n")
class MyMarkerView : MarkerView {
    private lateinit var Content1: TextView

    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource) {
        Content1 = findViewById(R.id.marker_text)
    }
    override fun refreshContent(e: Entry?, highlight: Highlight?) { //좌표 받아와서 text띄우는 거
        val text = e?.y!!.toInt().toString()
        Content1.text = "${text.substring(0,3)},${text.substring(3)}명"
        super.refreshContent(e, highlight)
    }

    //marker 위치 상단 중앙으로 조정
    override fun draw(canvas: Canvas?) {
        canvas!!.translate(-(width / 2).toFloat(), -height.toFloat() )
        super.draw(canvas)
    }
}