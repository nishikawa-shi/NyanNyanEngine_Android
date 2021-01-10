package com.ntetz.android.nyannyanengine_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.navigation.NavigationView
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.NyanNyanUserComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
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
    private val userActionUsecase: IUserActionUsecase by inject()

    private var isSignedIn: Boolean = false

    private val _refreshTweetListEvent = MutableLiveData(false)
    val refreshTweetListEvent: LiveData<Boolean>
        get() = _refreshTweetListEvent

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
        isSignedIn = (userInfo != DefaultUserConfig.notSignInUser)
        val name = userInfo?.name ?: getString(R.string.default_twitter_name)
        val screenName = "@${userInfo?.screenName ?: getString(R.string.default_twitter_id)}"

        main_nav_view.getHeaderView(0).twitter_image.load(userInfo?.fineImageUrl) {
            transformations(RoundedCornersTransformation(16f))
        }
        main_nav_view.getHeaderView(0).nyan_nyan_user_statuses.isVisible = isSignedIn
        main_nav_view.getHeaderView(0).twitter_name.text = name
        main_nav_view.getHeaderView(0).twitter_screen_name.text = screenName
        main_nav_view.menu.findItem(R.id.nav_sign_in).isVisible = !isSignedIn
        main_nav_view.menu.findItem(R.id.nav_settings_hash_tag).isVisible = isSignedIn
        main_nav_view.menu.findItem(R.id.nav_sign_out).isVisible = isSignedIn
    }

    fun updateNyanNyanUserInfo(userInfo: NyanNyanUserComponent?) {
        main_nav_view.getHeaderView(0).current_rank.text = userInfo?.getCurrentRank(this)
        main_nav_view.getHeaderView(0).current_point.text = userInfo?.nyanNyanUser?.nekosanPoint.toString()
        main_nav_view.getHeaderView(0).current_extends.text = userInfo?.currentExtends
    }

    fun updateTweetList() {
        _refreshTweetListEvent.postValue(true)
    }

    override fun onSupportNavigateUp() = (
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        main_drawer.closeDrawers()
        when (item.itemId) {
            R.id.nav_settings_hash_tag -> openHashtagSettingScreen()
            R.id.nav_sign_in -> openAuthorizePage(this)
            R.id.nav_sign_out -> openSignOutDialog(this)
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }

    private fun openHashtagSettingScreen() {
        navController.navigate(R.id.action_mainFragment_to_hashtagSettingFragment)
        launch {
            userActionUsecase.tap(userAction = UserAction.SETTING_HASH_TAG, scope = this)
        }
    }

    private fun openAuthorizePage(scope: CoroutineScope) {
        scope.launch {
            val authorizePageUri = accountUsecase.createAuthorizationEndpoint(this, this@MainActivity) ?: return@launch
            startActivity(Intent(Intent.ACTION_VIEW, authorizePageUri))
            userActionUsecase.tap(userAction = UserAction.SIGN_IN, scope = this)
        }
    }

    private fun openSignOutDialog(scope: CoroutineScope) {
        scope.launch {
            userActionUsecase.tap(userAction = UserAction.SIGN_OUT, scope = this)
        }
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.logout_sheet_body))
            .setPositiveButton(getString(R.string.logout_sheet_exec)) { _, _ ->
                navController.navigate(R.id.action_mainFragment_to_signOutFragment)
            }
            .setNegativeButton(getString(R.string.logout_sheet_cancel), null)
            .show()
    }
}
