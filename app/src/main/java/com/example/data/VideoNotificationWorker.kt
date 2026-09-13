package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.data.database.AppDatabase

class VideoNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("VideoNotificationWorker", "Background check for new updates initiated...")
        
        val context = applicationContext
        val sharedPrefs = context.getSharedPreferences("ponpes_notifications_pref", Context.MODE_PRIVATE)

        val notificationsEnabled = sharedPrefs.getBoolean("notifications_enabled", true)
        val newVideoNotify = sharedPrefs.getBoolean("notify_new_video", true)
        if (!notificationsEnabled || !newVideoNotify) {
            Log.d("VideoNotificationWorker", "Notifications disabled by user in preferences. Skipping.")
            return Result.success()
        }

        try {
            val db = AppDatabase.getDatabase(context)
            val repository = YouTubeRepository(db.favoriteVideoDao())
            
            // Fetch recent videos from channel UCpQJTZz_O9JAOWGCyWEDOdA
            val videos = repository.getRandomOrRecentVideos()
            if (videos.isNotEmpty()) {
                val latestVideo = videos.first()
                val lastSavedId = sharedPrefs.getString("last_known_video_id", null)

                Log.d("VideoNotificationWorker", "Latest uploaded video ID from API is: ${latestVideo.id}. Last known saved is: $lastSavedId")

                if (lastSavedId != null && lastSavedId != latestVideo.id) {
                    // Trigger a system notification indicating a new upload is ready
                    showNotification(context, latestVideo.title, latestVideo.description)
                }

                // Update last known ID to prevent multiple triggers
                sharedPrefs.edit().putString("last_known_video_id", latestVideo.id).apply()
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e("VideoNotificationWorker", "Error checking for new videos in background", e)
            return Result.retry()
        }
    }

    private fun showNotification(context: Context, title: String, content: String) {
        val channelId = "ponpes_updates_channel"
        val notificationId = 1001

        val sharedPrefs = context.getSharedPreferences("ponpes_notifications_pref", Context.MODE_PRIVATE)
        val vibrationEnabled = sharedPrefs.getBoolean("notify_vibration", true)
        val soundEnabled = sharedPrefs.getBoolean("notify_sound", true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Setup notification channel for Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Update Kajian Ponpes Jaya Baru",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Saluran notifikasi untuk update video kajian terbaru dari Ponpes Jaya Baru"
                enableLights(true)
                enableVibration(vibrationEnabled)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Action when notification is tapped
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
            .setContentTitle("Kajian Baru Ponpes Jaya Baru")
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title\n\n$content"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (!soundEnabled) {
            builder.setSilent(true)
        }

        if (vibrationEnabled) {
            builder.setVibrate(longArrayOf(0, 300, 150, 300))
        } else {
            builder.setVibrate(longArrayOf(0))
        }

        notificationManager.notify(notificationId, builder.build())
    }
}
