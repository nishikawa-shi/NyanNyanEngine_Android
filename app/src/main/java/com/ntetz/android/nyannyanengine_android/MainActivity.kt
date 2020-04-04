package com.ntetz.android.nyannyanengine_android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, main_drawer)
        setupActionBarWithNavController(navController, appBarConfiguration)
        main_nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp() = (
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        main_drawer.closeDrawers()
        when (item.itemId) {
            R.id.nav_settings_hash_tag -> navController.navigate(R.id.action_mainFragment_to_hashtagSettingFragment)
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }
}
