package com.ntetz.android.nyannyanengine_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import kotlin.coroutines.CoroutineContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
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

    private var isSignedIn: Boolean = false

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

    fun updateUserInfo(userInfo: TwitterUserRecord?) {
        isSignedIn = (userInfo != null)
        val name = userInfo?.name ?: getString(R.string.default_twitter_name)
        val screenName = "@${userInfo?.screenName ?: getString(R.string.default_twitter_id)}"
        val authMenuTitle = if (isSignedIn) getString(R.string.menu_sign_out) else getString(R.string.menu_sign_in)

        main_nav_view.getHeaderView(0).twitter_name.text = name
        main_nav_view.getHeaderView(0).twitter_screen_name.text = screenName
        main_nav_view.menu.findItem(R.id.nav_auth).title = authMenuTitle
    }

    override fun onSupportNavigateUp() = (
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        main_drawer.closeDrawers()
        when (item.itemId) {
            R.id.nav_settings_hash_tag -> navController.navigate(R.id.action_mainFragment_to_hashtagSettingFragment)
            R.id.nav_auth -> openPageWithSignedInStatus(this)
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }

    private fun openPageWithSignedInStatus(scope: CoroutineScope) {
        if (!isSignedIn) {
            openAuthorizePage(scope)
            return
        }
        openSignOutDialog()
    }

    private fun openAuthorizePage(scope: CoroutineScope) {
        scope.launch {
            val authorizePageUri = accountUsecase.createAuthorizationEndpoint(this) ?: return@launch
            startActivity(Intent(Intent.ACTION_VIEW, authorizePageUri))
        }
    }

    private fun openSignOutDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.logout_sheet_body))
            .setPositiveButton(getString(R.string.logout_sheet_exec)) { _, _ ->
                navController.navigate(R.id.action_mainFragment_to_signOutFragment)
            }
            .setNegativeButton(getString(R.string.logout_sheet_cancel), null)
            .show()
    }
}
