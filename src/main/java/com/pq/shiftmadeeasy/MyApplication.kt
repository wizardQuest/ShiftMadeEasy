package com.pq.shiftmadeeasy

import android.app.Application
import androidx.lifecycle.asLiveData
import com.pq.shiftmadeeasy.settings.SettingsManager
import com.pq.shiftmadeeasy.settings.UiMode
import com.squareup.leakcanary.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    private lateinit var settingsManager: SettingsManager
    private var isDarkMode = true
    override fun onCreate() {
        super.onCreate()
        //Observe Ui Preferences (Set theme)
        settingsManager = SettingsManager(applicationContext)
        observeUiPreferences()
        // Obtain the FirebaseAnalytics instance.
        //Initializing Timber for logging in debug mode only
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun observeUiPreferences() {
        settingsManager.uiModeFlow.asLiveData().observeForever { uiMode ->
            when (uiMode) {
                UiMode.LIGHT -> onLightMode()
                UiMode.DARK -> onDarkMode()
            }
        }
        settingsManager.userProfessionFlow.asLiveData().observeForever {

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

    //https://codelabs.developers.google.com/codelabs/android-dagger/#6

    //https://developer.android.com/jetpack/docs/guide
}