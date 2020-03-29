package com.ntetz.android.nyannyanengine_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainNavFragment)
        val configuration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, configuration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.mainNavFragment).navigateUp()
    }
}
