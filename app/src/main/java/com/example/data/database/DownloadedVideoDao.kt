package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadedVideoDao {
    @Query("SELECT * FROM downloaded_videos ORDER BY downloadedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadedVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadedVideoEntity)

    @Query("DELETE FROM downloaded_videos WHERE id = :videoId")
    suspend fun deleteDownloadById(videoId: String)

    @Query("SELECT * FROM downloaded_videos WHERE id = :videoId")
    suspend fun getDownloadById(videoId: String): DownloadedVideoEntity?

    @Query("SELECT * FROM downloaded_videos WHERE id = :videoId")
    fun observeDownload(videoId: String): Flow<DownloadedVideoEntity?>

    @Query("UPDATE downloaded_videos SET downloadProgress = :progress, status = :status WHERE id = :videoId")
    suspend fun updateProgress(videoId: String, progress: Int, status: String)
}
