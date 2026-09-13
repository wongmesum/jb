package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVideoDao {
    @Query("SELECT * FROM favorite_videos ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(video: FavoriteVideoEntity)

    @Query("DELETE FROM favorite_videos WHERE id = :videoId")
    suspend fun deleteFavoriteById(videoId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_videos WHERE id = :videoId)")
    suspend fun isFavorite(videoId: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_videos WHERE id = :videoId)")
    fun observeIsFavorite(videoId: String): Flow<Boolean>
}
