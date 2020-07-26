package com.ntetz.android.nyannyanengine_android

import android.content.Intent
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
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlin.coroutines.CoroutineContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), CoroutineScope, NavigationView.OnNavigationItemSelectedListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val accountUsecase: IAccountUsecase by inject()

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
            R.id.nav_auth -> {
                launch {
                    val authorizePageUri = accountUsecase.createAuthorizationEndpoint(this) ?: return@launch
                    startActivity(Intent(Intent.ACTION_VIEW, authorizePageUri))
                }
            }
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }
}
