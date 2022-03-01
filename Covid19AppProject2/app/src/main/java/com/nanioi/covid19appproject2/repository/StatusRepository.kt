package com.nanioi.covid19appproject2.repository

import android.content.Context
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.Model.network.Url
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class StatusRepository{

    companion object {
        private var instance: StatusRepository? = null
        lateinit var mContext : Context

    }

    fun getInstance(context: Context) : StatusRepository {
        mContext = context
        if(instance == null)
            instance = StatusRepository()

        return instance!!
    }

    fun getStatusData(): List<InfectionStatus> {
        var statusList = mutableListOf<InfectionStatus>()

        try {
            val doc: Document = Jsoup.connect(Url.Infection_Status_URL).get()
            val elts: Elements = doc.select("div.caseTable div")


            elts.forEachIndexed { index, elem ->
                val status_name = elem.select("div strong.ca_top").text()
                val status_cnt = elem.select("div ul li dl dd.ca_value").text().replace(" ", "*")

                val arr = status_cnt.split("*")
                val daily_cnt = arr.get(0)
                val percent = arr.get(1)

                var status = InfectionStatus(status_name, daily_cnt)
                statusList.add(status)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return statusList
    }
}