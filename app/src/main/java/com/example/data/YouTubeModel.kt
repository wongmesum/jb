package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeSearchResponse(
    @Json(name = "items") val items: List<YouTubeSearchResult>? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchResult(
    @Json(name = "id") val id: YouTubeVideoId? = null,
    @Json(name = "snippet") val snippet: YouTubeVideoSnippet? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoId(
    @Json(name = "kind") val kind: String? = null,
    @Json(name = "videoId") val videoId: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoSnippet(
    @Json(name = "publishedAt") val publishedAt: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "thumbnails") val thumbnails: YouTubeThumbnails? = null,
    @Json(name = "channelTitle") val channelTitle: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnails(
    @Json(name = "default") val default: YouTubeThumbnailDetails? = null,
    @Json(name = "medium") val medium: YouTubeThumbnailDetails? = null,
    @Json(name = "high") val high: YouTubeThumbnailDetails? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnailDetails(
    @Json(name = "url") val url: String? = null,
    @Json(name = "width") val width: Int? = null,
    @Json(name = "height") val height: Int? = null
)

// UI-friendly domain model for a Video to be used across the screens
data class VideoModel(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val publishedAt: String,
    val isFavorite: Boolean = false
)
