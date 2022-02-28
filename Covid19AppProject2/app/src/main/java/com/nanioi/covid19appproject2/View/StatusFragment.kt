package com.nanioi.covid19appproject2.View

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.Model.network.JSoupParsingTask
import com.nanioi.covid19appproject2.R.layout
import com.nanioi.covid19appproject2.ViewModel.ClinicViewModel
import java.util.*
import kotlin.collections.HashMap

class StatusFragment : Fragment(layout.fragment_status){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        JSoupParsingTask().execute()
    }

}