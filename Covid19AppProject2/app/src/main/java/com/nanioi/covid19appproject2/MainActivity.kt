package com.nanioi.covid19appproject2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val summaryFragment = SummaryFragment()
//        val NewFragment = NewFragment()
//        val clinicFragment = ClinicFragment()
        val precautionFragment = PrecautionFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(summaryFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.summary -> replaceFragment(summaryFragment)
//                R.id.New -> replaceFragment(NewFragment)
//                R.id.clinic -> replaceFragment(clinicFragment)
                R.id.precaution -> replaceFragment(precautionFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}