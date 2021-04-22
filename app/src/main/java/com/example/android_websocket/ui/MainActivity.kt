package com.example.android_websocket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.android_websocket.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "AppDebug: MainActivity"
    private val viewModel: MainViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val textFragment = TextFragment()
        val binaryFragment = BinaryFragment()

        bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.text_frag_mi -> {
                    setCurrentFragment(textFragment)
                    true
                }
                R.id.binary_frag_mi -> {
                    setCurrentFragment(binaryFragment)
                    true
                }
                else -> false

            }
        }

        setCurrentFragment(textFragment)

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_fl, fragment)
            addToBackStack(null)
            commit()
        }
}
