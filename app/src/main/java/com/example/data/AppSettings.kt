package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

enum class ThemeMode(val title: String, val description: String) {
    SYSTEM("Sistem", "Mengikuti tema bawaan perangkat"),
    LIGHT("Terang", "Nuansa Soft Ivory yang teduh dan bersih"),
    DARK("Gelap", "Nuansa Twilight Sage yang tenang dan nyaman di mata")
}

enum class TextScale(val title: String, val scaleMultiplier: Float) {
    NORMAL("Standar (100%)", 1.0f),
    MEDIUM("Sedang (112%)", 1.12f),
    LARGE("Besar (125%)", 1.25f)
}

enum class CardDensity(val title: String, val description: String) {
    COMFORTABLE("Nyaman & Detail", "Menampilkan deskripsi lengkap dan visual lapang"),
    COMPACT("Kompak & Hemat", "Tata letak ringkas dengan lebih banyak konten dalam satu layar")
}

enum class CheckFrequency(val hours: Long, val label: String) {
    ONE_HOUR(1, "Setiap 1 Jam"),
    THREE_HOURS(3, "Setiap 3 Jam"),
    SIX_HOURS(6, "Setiap 6 Jam"),
    DAILY(24, "Sekali Sehari")
}

data class DisplaySettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val textScale: TextScale = TextScale.NORMAL,
    val cardDensity: CardDensity = CardDensity.COMFORTABLE,
    val hdThumbnails: Boolean = true,
    val autoPlayPreview: Boolean = true
)

data class NotificationSettings(
    val enabled: Boolean = true,
    val newVideoNotify: Boolean = true,
    val dailyReminderNotify: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val frequency: CheckFrequency = CheckFrequency.ONE_HOUR
)

class AppSettingsManager(private val context: Context) {

    private val settingsPrefs = context.getSharedPreferences("ponpes_app_settings", Context.MODE_PRIVATE)
    private val notifyPrefs = context.getSharedPreferences("ponpes_notifications_pref", Context.MODE_PRIVATE)

    private val _displaySettings = MutableStateFlow(loadDisplaySettings())
    val displaySettings: StateFlow<DisplaySettings> = _displaySettings.asStateFlow()

    private val _notificationSettings = MutableStateFlow(loadNotificationSettings())
    val notificationSettings: StateFlow<NotificationSettings> = _notificationSettings.asStateFlow()

    private fun loadDisplaySettings(): DisplaySettings {
        val themeStr = settingsPrefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        val scaleStr = settingsPrefs.getString("text_scale", TextScale.NORMAL.name) ?: TextScale.NORMAL.name
        val densityStr = settingsPrefs.getString("card_density", CardDensity.COMFORTABLE.name) ?: CardDensity.COMFORTABLE.name
        val hd = settingsPrefs.getBoolean("hd_thumbnails", true)
        val autoPlay = settingsPrefs.getBoolean("autoplay_preview", true)

        return DisplaySettings(
            themeMode = runCatching { ThemeMode.valueOf(themeStr) }.getOrDefault(ThemeMode.SYSTEM),
            textScale = runCatching { TextScale.valueOf(scaleStr) }.getOrDefault(TextScale.NORMAL),
            cardDensity = runCatching { CardDensity.valueOf(densityStr) }.getOrDefault(CardDensity.COMFORTABLE),
            hdThumbnails = hd,
            autoPlayPreview = autoPlay
        )
    }

    private fun loadNotificationSettings(): NotificationSettings {
        val enabled = notifyPrefs.getBoolean("notifications_enabled", true)
        val newVideo = notifyPrefs.getBoolean("notify_new_video", true)
        val dailyReminder = notifyPrefs.getBoolean("notify_daily_reminder", true)
        val vibration = notifyPrefs.getBoolean("notify_vibration", true)
        val sound = notifyPrefs.getBoolean("notify_sound", true)
        val freqStr = notifyPrefs.getString("notify_frequency", CheckFrequency.ONE_HOUR.name) ?: CheckFrequency.ONE_HOUR.name

        return NotificationSettings(
            enabled = enabled,
            newVideoNotify = newVideo,
            dailyReminderNotify = dailyReminder,
            vibrationEnabled = vibration,
            soundEnabled = sound,
            frequency = runCatching { CheckFrequency.valueOf(freqStr) }.getOrDefault(CheckFrequency.ONE_HOUR)
        )
    }

    // Display updates
    fun updateThemeMode(mode: ThemeMode) {
        settingsPrefs.edit().putString("theme_mode", mode.name).apply()
        _displaySettings.value = _displaySettings.value.copy(themeMode = mode)
    }

    fun updateTextScale(scale: TextScale) {
        settingsPrefs.edit().putString("text_scale", scale.name).apply()
        _displaySettings.value = _displaySettings.value.copy(textScale = scale)
    }

    fun updateCardDensity(density: CardDensity) {
        settingsPrefs.edit().putString("card_density", density.name).apply()
        _displaySettings.value = _displaySettings.value.copy(cardDensity = density)
    }

    fun updateHdThumbnails(enabled: Boolean) {
        settingsPrefs.edit().putBoolean("hd_thumbnails", enabled).apply()
        _displaySettings.value = _displaySettings.value.copy(hdThumbnails = enabled)
    }

    fun updateAutoPlayPreview(enabled: Boolean) {
        settingsPrefs.edit().putBoolean("autoplay_preview", enabled).apply()
        _displaySettings.value = _displaySettings.value.copy(autoPlayPreview = enabled)
    }

    fun resetDisplaySettings() {
        val defaultDisplay = DisplaySettings()
        settingsPrefs.edit()
            .putString("theme_mode", defaultDisplay.themeMode.name)
            .putString("text_scale", defaultDisplay.textScale.name)
            .putString("card_density", defaultDisplay.cardDensity.name)
            .putBoolean("hd_thumbnails", defaultDisplay.hdThumbnails)
            .putBoolean("autoplay_preview", defaultDisplay.autoPlayPreview)
            .apply()
        _displaySettings.value = defaultDisplay
    }

    // Notification updates
    fun updateNotificationsEnabled(enabled: Boolean) {
        notifyPrefs.edit().putBoolean("notifications_enabled", enabled).apply()
        _notificationSettings.value = _notificationSettings.value.copy(enabled = enabled)
        applyWorkSchedule()
    }

    fun updateNewVideoNotify(enabled: Boolean) {
        notifyPrefs.edit().putBoolean("notify_new_video", enabled).apply()
        _notificationSettings.value = _notificationSettings.value.copy(newVideoNotify = enabled)
    }

    fun updateDailyReminderNotify(enabled: Boolean) {
        notifyPrefs.edit().putBoolean("notify_daily_reminder", enabled).apply()
        _notificationSettings.value = _notificationSettings.value.copy(dailyReminderNotify = enabled)
    }

    fun updateVibrationEnabled(enabled: Boolean) {
        notifyPrefs.edit().putBoolean("notify_vibration", enabled).apply()
        _notificationSettings.value = _notificationSettings.value.copy(vibrationEnabled = enabled)
    }

    fun updateSoundEnabled(enabled: Boolean) {
        notifyPrefs.edit().putBoolean("notify_sound", enabled).apply()
        _notificationSettings.value = _notificationSettings.value.copy(soundEnabled = enabled)
    }

    fun updateFrequency(frequency: CheckFrequency) {
        notifyPrefs.edit().putString("notify_frequency", frequency.name).apply()
        _notificationSettings.value = _notificationSettings.value.copy(frequency = frequency)
        applyWorkSchedule()
    }

    fun resetNotificationSettings() {
        val defaultNotify = NotificationSettings()
        notifyPrefs.edit()
            .putBoolean("notifications_enabled", defaultNotify.enabled)
            .putBoolean("notify_new_video", defaultNotify.newVideoNotify)
            .putBoolean("notify_daily_reminder", defaultNotify.dailyReminderNotify)
            .putBoolean("notify_vibration", defaultNotify.vibrationEnabled)
            .putBoolean("notify_sound", defaultNotify.soundEnabled)
            .putString("notify_frequency", defaultNotify.frequency.name)
            .apply()
        _notificationSettings.value = defaultNotify
        applyWorkSchedule()
    }

    fun applyWorkSchedule() {
        try {
            val workManager = WorkManager.getInstance(context)
            val current = _notificationSettings.value

            if (!current.enabled) {
                workManager.cancelUniqueWork("PonpesVideoNotificationCheck")
                Log.d("AppSettingsManager", "Notification work cancelled: Notifications disabled.")
                return
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<VideoNotificationWorker>(
                current.frequency.hours, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "PonpesVideoNotificationCheck",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
            Log.d("AppSettingsManager", "Scheduled periodic check: every ${current.frequency.hours} hours.")
        } catch (e: Exception) {
            Log.e("AppSettingsManager", "Failed to update WorkManager schedule", e)
        }
    }

    fun sendTestNotification(): Boolean {
        return try {
            val channelId = "ponpes_updates_channel"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val current = _notificationSettings.value

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Update Kajian Ponpes Jaya Baru",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Saluran notifikasi bimbingan ilmu tasawuf dan kajian Ponpes Jaya Baru"
                    enableLights(true)
                    enableVibration(current.vibrationEnabled)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Uji Notifikasi Ponpes Jaya Baru")
                .setContentText("Alhamdulillah, notifikasi kajian rohani Anda berhasil diatur dan aktif.")
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "Assalamu'alaikum Warahmatullahi Wabarakatuh.\n\nNotifikasi bimbingan ilmu tasawuf dan kajian Ponpes Jaya Baru telah aktif pada perangkat Anda. Semoga memberi manfaat bagi pembersihan jiwa dan ketenangan hati."
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            if (current.soundEnabled) {
                val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                builder.setSound(soundUri)
            } else {
                builder.setSilent(true)
            }

            if (current.vibrationEnabled) {
                builder.setVibrate(longArrayOf(0, 250, 150, 250))
            } else {
                builder.setVibrate(longArrayOf(0))
            }

            notificationManager.notify(9999, builder.build())
            true
        } catch (e: Exception) {
            Log.e("AppSettingsManager", "Failed to send test notification", e)
            false
        }
    }
}
