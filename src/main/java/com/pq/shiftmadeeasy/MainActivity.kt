package com.pq.shiftmadeeasy

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pq.shiftmadeeasy.color.Colors
import com.pq.shiftmadeeasy.databinding.ActivityMainBinding
import com.pq.shiftmadeeasy.settings.SettingsManager
import com.pq.shiftmadeeasy.settings.UiMode
import com.pq.shiftmadeeasy.ui.addtodobottomsheet.AddTodoBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AddTodoBottomSheetFragment.SaveTaskListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var bottomBar: BottomAppBar
    private var bottomNavDrawerFragment: BottomNavigationDrawerFragment? = null
    private lateinit var addTodoBottomSheetFragment: AddTodoBottomSheetFragment
    private lateinit var settingsManager: SettingsManager
    private var isDarkMode = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(applicationContext)
        //Observe Ui Preferences (Set theme)
        //observeUiPreferences()
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        bottomBar = findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bottomBar)
        setStartingFragment()
        //CLICK LISTENERS
        binding.fab.setOnClickListener {
            addTodoBottomSheetFragment = AddTodoBottomSheetFragment.newInstance()
            addTodoBottomSheetFragment.show(supportFragmentManager, addTodoBottomSheetFragment.tag)
        }
    }

    private fun setStartingFragment() {
        settingsManager.userName.asLiveData().observeForever {
            /*val myJob = Job()
            val uiScope = CoroutineScope(Dispatchers.Main + myJob)
            uiScope.launch {
              //  delay(3000)
            }*/
            if (!it.isNullOrBlank()) {
                findNavController(R.id.navHostFragment).navigate(R.id.userHomeFragment)
            }
            else{
                findNavController(R.id.navHostFragment).navigate(R.id.newUserLandingFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun observeUiPreferences() {
        settingsManager.uiModeFlow.asLiveData().observe(this) { uiMode ->
            when (uiMode) {
                UiMode.LIGHT -> onLightMode()
                UiMode.DARK -> onDarkMode()
            }
        }
    }

    private fun onLightMode() {
        isDarkMode = false
        setTheme(R.style.MyAppThemeLight)

        // Actually turn on Light mode using AppCompatDelegate.setDefaultNightMode() here
    }

    private fun onDarkMode() {
        isDarkMode = true
        setTheme(R.style.MyAppThemeDark)

        // Actually turn on Dark mode using AppCompatDelegate.setDefaultNightMode() here
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment?.show(supportFragmentManager, bottomNavDrawerFragment?.tag)
            }
        }
        return true
    }

    override fun onTaskSaved() {
        addTodoBottomSheetFragment.dismiss()

        val snackbar =
            window.decorView.rootView?.let {
                Snackbar.make(
                    it,
                    getString(R.string.snackbar_task_save_msg),
                    Snackbar.LENGTH_LONG
                )
            }
        snackbar?.apply {
            setTextColor(Colors.BLACK)
            setAction(getString(R.string.snackbar_dismiss_btn_text)) { this.dismiss() }
            setBackgroundTint(Colors.GREEN_LIGHT)
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            show()
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?,
        pref: Preference?
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref?.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref!!.fragment
        )
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        findNavController(R.id.navHostFragment).navigate(R.id.notificationSettingsFragment)
        return true
    }
}