package com.nanioi.covid19appproject2.View

import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import androidx.fragment.app.Fragment
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.databinding.FragmentPrecautionBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class PrecautionFragment : Fragment(layout.fragment_precaution){

    private lateinit var binding: FragmentPrecautionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPrecautionBinding = FragmentPrecautionBinding.bind(view)
        binding = fragmentPrecautionBinding

        initViews()
    }

    private fun initViews() {
        goLinkClickedText()
    }

    private fun goLinkClickedText() {
        val transform = Linkify.TransformFilter(object : Linkify.TransformFilter, (Matcher, String) -> String { // 이건나도 구글링해서 복붙해온건데 그 텍스트 부분 클릭하면 그 url로 이동시키는 부분이여 아래 코드 전부까지
            override fun transformUrl(p0: Matcher?, p1: String?): String {
                return ""
            }
            override fun invoke(p1: Matcher, p2: String): String {
                return ""
            }
        })
        val pattern1 = Pattern.compile("중앙재난안전대책본부")
        val pattern2 = Pattern.compile("중앙사고수습본부")
        val pattern3 = Pattern.compile("중앙방역대책본부")
        val pattern4 = Pattern.compile("Johns Hopkins CSSE")

        Linkify.addLinks(binding.source, pattern1,
            "http://ncov.mohw.go.kr/",
            null,
            transform
        )
        Linkify.addLinks(binding.source, pattern2,
            "http://www.mohw.go.kr/react/index.jsp",
            null,
            transform
        )
        Linkify.addLinks(binding.source, pattern3,
            "http://www.cdc.go.kr/index.es?sid=a2",
            null,
            transform
        )
        Linkify.addLinks(binding.source, pattern4,
            "https://www.arcgis.com/apps/opsdashboard/index.html#/bda7594740fd40299423467b48e9ecf6",
            null,
            transform
        )
    }
}