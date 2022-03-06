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
import com.ntetz.android.nyannyanengine_android.databinding.ActivityMainBinding
import com.ntetz.android.nyannyanengine_android.databinding.NavHeaderMainBinding
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
import com.ntetz.android.nyannyanengine_android.model.entity.dao.room.TwitterUserRecord
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.account.NyanNyanUserComponent
import com.ntetz.android.nyannyanengine_android.model.entity.usecase.screen_transition.UserAction
import com.ntetz.android.nyannyanengine_android.model.usecase.IAccountUsecase
import com.ntetz.android.nyannyanengine_android.model.usecase.IUserActionUsecase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CoroutineScope, NavigationView.OnNavigationItemSelectedListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderMainBinding: NavHeaderMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Inject
    lateinit var accountUsecase: IAccountUsecase

    @Inject
    lateinit var userActionUsecase: IUserActionUsecase

    private var isSignedIn: Boolean = false

    private val _refreshTweetListEvent = MutableLiveData(false)
    val refreshTweetListEvent: LiveData<Boolean>
        get() = _refreshTweetListEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navHeaderMainBinding = NavHeaderMainBinding.bind(this.binding.mainNavView.getHeaderView(0))
        setContentView(this.binding.root)
        setSupportActionBar(binding.mainBar.mainToolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.mainDrawer)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainNavView.setNavigationItemSelectedListener(this)
    }

    fun updateUserInfo(userInfo: TwitterUserRecord?) {
        isSignedIn = (userInfo != DefaultUserConfig.getNotSignInUser(this))
        val name = userInfo?.name ?: getString(R.string.default_twitter_name)
        val screenName = "@${userInfo?.screenName ?: getString(R.string.default_twitter_id)}"

        navHeaderMainBinding.twitterImage.load(userInfo?.fineImageUrl) {
            transformations(RoundedCornersTransformation(16f))
        }
        navHeaderMainBinding.nyanNyanUserStatuses.isVisible = isSignedIn
        navHeaderMainBinding.twitterName.text = name
        navHeaderMainBinding.twitterScreenName.text = screenName
        binding.mainNavView.menu.findItem(R.id.nav_sign_in).isVisible = !isSignedIn
        binding.mainNavView.menu.findItem(R.id.nav_settings_hash_tag).isVisible = isSignedIn
        binding.mainNavView.menu.findItem(R.id.nav_sign_out).isVisible = isSignedIn
    }

    fun updateNyanNyanUserInfo(userInfo: NyanNyanUserComponent?) {
        navHeaderMainBinding.currentRank.text = userInfo?.getCurrentRank(this)
        navHeaderMainBinding.currentPoint.text = userInfo?.nyanNyanUser?.nekosanPoint.toString()
        navHeaderMainBinding.currentExtends.text = userInfo?.currentExtends
    }

    fun updateTweetList() {
        _refreshTweetListEvent.postValue(true)
    }

    override fun onSupportNavigateUp() = (
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp())

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.mainDrawer.closeDrawers()
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
            val authorizePageUri = accountUsecase.createAuthorizationEndpoint(this) ?: return@launch
            startActivity(Intent(Intent.ACTION_VIEW, authorizePageUri))
            userActionUsecase.tap(userAction = UserAction.SIGN_IN, scope = this)
        }
    }

    private fun openSignOutDialog(scope: CoroutineScope) {
        scope.launch {
            userActionUsecase.tap(userAction = UserAction.SIGN_OUT, scope = this)
        }
        AlertDialog.Builder(this, R.style.NyanNyanDialog)
            .setMessage(getString(R.string.sign_out_sheet_body))
            .setPositiveButton(getString(R.string.sign_out_sheet_exec)) { _, _ ->
                navController.navigate(R.id.action_mainFragment_to_signOutFragment)
            }
            .setNegativeButton(getString(R.string.sign_out_sheet_cancel), null)
            .show()
    }
}
