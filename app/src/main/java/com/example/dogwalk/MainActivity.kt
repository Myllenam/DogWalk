package com.example.dogwalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogwalk.ui.history.HistoryFragment
import com.example.dogwalk.ui.home.HomeFragment
import com.example.dogwalk.ui.pets.PetsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        val bottomNavigation =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->

            val fragment = when (item.itemId) {

                R.id.nav_home -> HomeFragment()

                R.id.nav_pets -> PetsFragment()

                R.id.nav_history -> HistoryFragment()

                else -> null
            }

            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }

            fragment != null
        }
    }
}