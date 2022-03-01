package com.nanioi.covid19appproject2.Model.network

import android.os.AsyncTask
import android.util.Log
import com.nanioi.covid19appproject2.Model.data.DataSource
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.lang.Exception
import kotlin.system.measureTimeMillis

class RemoteStatusData : DataSource {
    override fun getStatusData(): List<InfectionStatus>? {
        val statusList = getStatusFromWeb(Url.Infection_Status_URL)
        return statusList
    }

    private fun getStatusFromWeb(url : String): List<InfectionStatus>?{
        var statusList = mutableListOf<InfectionStatus>()
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
}