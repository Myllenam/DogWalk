package com.example.dogwalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogwalk.ui.history.HistoryFragment
import com.example.dogwalk.ui.home.HomeFragment
import com.example.dogwalk.ui.pets.PetsFragment
import com.example.dogwalk.ui.walk.WalkFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    fun showWalkScreen() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = android.view.View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WalkFragment())
            .addToBackStack("walk")
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = android.view.View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

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