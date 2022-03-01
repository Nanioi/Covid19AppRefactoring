package com.nanioi.covid19appproject2.Model.network

import android.os.AsyncTask
import android.util.Log
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.Model.network.Url.Infection_Status_URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class JSoupParsingTask () :
    AsyncTask<Any?, Any?, List<InfectionStatus>>() { //input, progress update type, result type
    var statusList = mutableListOf<InfectionStatus>()
    override fun doInBackground(vararg params: Any?): List<InfectionStatus>? {
        val doc: Document = Jsoup.connect(Infection_Status_URL).get()
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

    override fun onPostExecute(result: List<InfectionStatus>) {
        super.onPostExecute(result)
        //todo 데이터 result 전달
        Log.d("Data : ", statusList.toString())
    }
}