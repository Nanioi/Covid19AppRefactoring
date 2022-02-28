package com.nanioi.covid19appproject2.Model.network

import android.os.AsyncTask
import android.util.Log
import com.nanioi.covid19appproject2.Model.data.InfectionStatusss
import com.nanioi.covid19appproject2.Model.network.Url.Infection_Status_URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class JSoupParsingTask () :
    AsyncTask<Any?, Any?, List<InfectionStatusss>>() { //input, progress update type, result type
    var statusList = mutableListOf<InfectionStatusss>()
    override fun doInBackground(vararg params: Any?): List<InfectionStatusss>? {
        val doc: Document = Jsoup.connect(Infection_Status_URL).get()
        val elts: Elements = doc.select("div.caseTable div")


        elts.forEachIndexed { index, elem ->
            val status_name = elem.select("div strong.ca_top").text()
            val status_cnt = elem.select("div ul li dl dd.ca_value").text().replace(" ","*")

            val arr = status_cnt.split("*")
            val daily_cnt = arr.get(0)
            val percent = arr.get(1)

            var status = InfectionStatusss(status_name,daily_cnt)
            statusList.add(status)

        }
        return statusList
    }

    override fun onPostExecute(result: List<InfectionStatusss>) {
        super.onPostExecute(result)
        //todo 데이터 result 전달
        Log.d("Data : ", statusList.toString())
    }
}