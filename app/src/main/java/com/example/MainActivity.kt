package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.example.data.TextScale
import com.example.data.ThemeMode
import com.example.data.VideoNotificationWorker
import com.example.ui.MainAppFrame
import com.example.ui.YouTubeViewModel
import com.example.ui.theme.MyApplicationTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Request notification permission for modern Android versions (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
      }
    }

    // Schedule background YouTube checks for Ponpes Channel (UCpQJTZz_O9JAOWGCyWEDOdA)
    try {
      val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val notificationWorkRequest = PeriodicWorkRequestBuilder<VideoNotificationWorker>(
        1, TimeUnit.HOURS
      )
        .setConstraints(constraints)
        .build()

      WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
        "PonpesVideoNotificationCheck",
        ExistingPeriodicWorkPolicy.KEEP,
        notificationWorkRequest
      )
    } catch (e: Exception) {
      e.printStackTrace()
    }

    enableEdgeToEdge()
    setContent {
      val viewModel: YouTubeViewModel = viewModel()
      val displaySettings by viewModel.displaySettings.collectAsState()

      val isDark = when (displaySettings.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
      }

      val fontScaleMultiplier = when (displaySettings.textScale) {
        TextScale.NORMAL -> 1.0f
        TextScale.MEDIUM -> 1.12f
        TextScale.LARGE -> 1.25f
      }

      val currentDensity = LocalDensity.current
      val adjustedDensity = remember(currentDensity, fontScaleMultiplier) {
        Density(
          density = currentDensity.density,
          fontScale = currentDensity.fontScale * fontScaleMultiplier
        )
      }

      CompositionLocalProvider(LocalDensity provides adjustedDensity) {
        MyApplicationTheme(darkTheme = isDark) {
          MainAppFrame(viewModel = viewModel)
        }
      }
    }
  }
}
