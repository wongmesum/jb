package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_videos")
data class DownloadedVideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val publishedAt: String,
    val localFilePath: String,
    val downloadProgress: Int, // 0 to 100
    val status: String,        // "PENDING", "DOWNLOADING", "COMPLETED", "FAILED"
    val downloadedAt: Long = System.currentTimeMillis()
)
