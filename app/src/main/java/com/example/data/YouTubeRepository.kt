package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.api.YouTubeApiService
import com.example.data.database.FavoriteVideoDao
import com.example.data.database.FavoriteVideoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class YouTubeRepository(private val favoriteVideoDao: FavoriteVideoDao) {

    private val keys = listOf(
        BuildConfig.YOUTUBE_API_KEY_1,
        BuildConfig.YOUTUBE_API_KEY_2,
        BuildConfig.YOUTUBE_API_KEY_3,
        BuildConfig.YOUTUBE_API_KEY_4,
        BuildConfig.YOUTUBE_API_KEY_5
    )

    private val channelId = BuildConfig.CHANNEL_ID

    // Tracks which API key is currently in use
    private var activeKeyIndex = 0

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/youtube/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService = retrofit.create(YouTubeApiService::class.java)

    fun getActiveApiKey(): String {
        if (keys.isEmpty()) return ""
        return keys[activeKeyIndex]
    }

    fun getActiveKeyIndex(): Int = activeKeyIndex + 1

    private fun rotateKey() {
        if (keys.isNotEmpty()) {
            activeKeyIndex = (activeKeyIndex + 1) % keys.size
            Log.d("YouTubeRepository", "Rotating YouTube API key to index: $activeKeyIndex")
        }
    }

    private suspend fun <T> executeWithRollingKeys(call: suspend (apiKey: String) -> T): T {
        var lastException: Exception? = null
        val attempts = if (keys.isNotEmpty()) keys.size else 1

        for (attempt in 0 until attempts) {
            val key = getActiveApiKey()
            try {
                return call(key)
            } catch (e: HttpException) {
                lastException = e
                Log.e("YouTubeRepository", "HTTP Error code: ${e.code()} on API key index $activeKeyIndex", e)
                // Switch to next key on limits/forbidden (403, 429) or other errors
                rotateKey()
            } catch (e: Exception) {
                lastException = e
                Log.e("YouTubeRepository", "General network error on API key index $activeKeyIndex", e)
                rotateKey()
            }
        }
        throw lastException ?: Exception("All 5 YouTube API keys failed to complete the request.")
    }

    suspend fun getPopularVideos(): List<VideoModel> {
        val response = executeWithRollingKeys { apiKey ->
            apiService.searchVideos(
                channelId = channelId,
                maxResults = 4,
                order = "viewCount",
                apiKey = apiKey
            )
        }
        return mapSearchResponseToVideos(response)
    }

    suspend fun getRandomOrRecentVideos(): List<VideoModel> {
        val response = executeWithRollingKeys { apiKey ->
            apiService.searchVideos(
                channelId = channelId,
                maxResults = 30,
                order = "date",
                apiKey = apiKey
            )
        }
        return mapSearchResponseToVideos(response)
    }

    suspend fun searchVideosByQuery(query: String): List<VideoModel> {
        val response = executeWithRollingKeys { apiKey ->
            apiService.searchVideos(
                channelId = channelId,
                maxResults = 30,
                order = "relevance",
                apiKey = apiKey,
                query = query
            )
        }
        return mapSearchResponseToVideos(response)
    }

    private fun mapSearchResponseToVideos(response: YouTubeSearchResponse): List<VideoModel> {
        val items = response.items ?: return emptyList()
        return items.mapNotNull { item ->
            val videoId = item.id?.videoId ?: return@mapNotNull null
            val snippet = item.snippet ?: return@mapNotNull null
            
            // Clean up html entities often found in YouTube titles
            val rawTitle = snippet.title ?: "Kajian Tasawuf"
            val title = decodeHtmlEntities(rawTitle)
            
            val rawDesc = snippet.description ?: ""
            val description = decodeHtmlEntities(rawDesc)

            VideoModel(
                id = videoId,
                title = title,
                description = description,
                thumbnailUrl = snippet.thumbnails?.high?.url 
                    ?: snippet.thumbnails?.medium?.url 
                    ?: "https://i.ytimg.com/vi/$videoId/hqdefault.jpg",
                publishedAt = snippet.publishedAt ?: ""
            )
        }
    }

    private fun decodeHtmlEntities(input: String): String {
        return input
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&rsquo;", "'")
            .replace("&ndash;", "-")
            .replace("&mdash;", "-")
    }

    // Room operations
    val allFavorites: Flow<List<VideoModel>> = favoriteVideoDao.getAllFavorites().map { entities ->
        entities.map { entity ->
            VideoModel(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                thumbnailUrl = entity.thumbnailUrl,
                publishedAt = entity.publishedAt,
                isFavorite = true
            )
        }
    }

    suspend fun addFavorite(video: VideoModel) {
        favoriteVideoDao.insertFavorite(
            FavoriteVideoEntity(
                id = video.id,
                title = video.title,
                description = video.description,
                thumbnailUrl = video.thumbnailUrl,
                publishedAt = video.publishedAt
            )
        )
    }

    suspend fun removeFavorite(videoId: String) {
        favoriteVideoDao.deleteFavoriteById(videoId)
    }

    suspend fun isFavoriteVideo(videoId: String): Boolean {
        return favoriteVideoDao.isFavorite(videoId)
    }
}
