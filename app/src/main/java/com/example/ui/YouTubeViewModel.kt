package com.example.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.database.AppDatabase
import com.example.data.database.DownloadedVideoEntity
import com.example.data.database.StudyNoteEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class GoogleUser(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)

class YouTubeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = YouTubeRepository(db.favoriteVideoDao())
    private val sharedPrefs = application.getSharedPreferences("ponpes_auth_prefs", Context.MODE_PRIVATE)

    private val appSettingsManager = AppSettingsManager(application)
    val displaySettings: StateFlow<DisplaySettings> = appSettingsManager.displaySettings
    val notificationSettings: StateFlow<NotificationSettings> = appSettingsManager.notificationSettings

    fun updateThemeMode(mode: ThemeMode) {
        appSettingsManager.updateThemeMode(mode)
    }

    fun updateTextScale(scale: TextScale) {
        appSettingsManager.updateTextScale(scale)
    }

    fun updateCardDensity(density: CardDensity) {
        appSettingsManager.updateCardDensity(density)
    }

    fun updateHdThumbnails(enabled: Boolean) {
        appSettingsManager.updateHdThumbnails(enabled)
    }

    fun updateAutoPlayPreview(enabled: Boolean) {
        appSettingsManager.updateAutoPlayPreview(enabled)
    }

    fun resetDisplaySettings() {
        appSettingsManager.resetDisplaySettings()
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        appSettingsManager.updateNotificationsEnabled(enabled)
    }

    fun updateNewVideoNotify(enabled: Boolean) {
        appSettingsManager.updateNewVideoNotify(enabled)
    }

    fun updateDailyReminderNotify(enabled: Boolean) {
        appSettingsManager.updateDailyReminderNotify(enabled)
    }

    fun updateVibrationEnabled(enabled: Boolean) {
        appSettingsManager.updateVibrationEnabled(enabled)
    }

    fun updateSoundEnabled(enabled: Boolean) {
        appSettingsManager.updateSoundEnabled(enabled)
    }

    fun updateNotificationFrequency(frequency: CheckFrequency) {
        appSettingsManager.updateFrequency(frequency)
    }

    fun resetNotificationSettings() {
        appSettingsManager.resetNotificationSettings()
    }

    fun sendTestNotification(): Boolean {
        return appSettingsManager.sendTestNotification()
    }

    private val _currentUser = MutableStateFlow<GoogleUser?>(null)
    val currentUser: StateFlow<GoogleUser?> = _currentUser.asStateFlow()

    // Study Notes collected reactively from Room DB Flow
    val studyNotes: StateFlow<List<StudyNoteEntity>> = db.studyNoteDao().getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getNotesForVideo(videoId: String): Flow<List<StudyNoteEntity>> {
        return db.studyNoteDao().getNotesForVideo(videoId)
    }

    fun addStudyNote(videoId: String, videoTitle: String, noteText: String) {
        viewModelScope.launch {
            val note = StudyNoteEntity(
                videoId = videoId,
                videoTitle = videoTitle,
                noteText = noteText
            )
            db.studyNoteDao().insertNote(note)
        }
    }

    fun updateStudyNote(note: StudyNoteEntity) {
        viewModelScope.launch {
            db.studyNoteDao().updateNote(note)
        }
    }

    fun deleteStudyNote(note: StudyNoteEntity) {
        viewModelScope.launch {
            db.studyNoteDao().deleteNote(note)
        }
    }

    fun deleteStudyNoteById(noteId: Int) {
        viewModelScope.launch {
            db.studyNoteDao().deleteNoteById(noteId)
        }
    }

    // Downloaded videos collected reactively from Room DB Flow
    val downloadedVideos: StateFlow<List<DownloadedVideoEntity>> = db.downloadedVideoDao().getAllDownloads()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Splash screen state (Loading logo first)
    private val _isSplashLoading = MutableStateFlow(true)
    val isSplashLoading: StateFlow<Boolean> = _isSplashLoading.asStateFlow()

    // Screen Navigation Tab State
    private val _currentTab = MutableStateFlow(CurrentTab.HOME)
    val currentTab: StateFlow<CurrentTab> = _currentTab.asStateFlow()

    // 4 Popular Videos UI state
    private val _popularVideosState = MutableStateFlow<UiState<List<VideoModel>>>(UiState.Loading)
    val popularVideosState: StateFlow<UiState<List<VideoModel>>> = _popularVideosState.asStateFlow()

    // Random/Recent Grid Videos UI state
    private val _recentVideosState = MutableStateFlow<UiState<List<VideoModel>>>(UiState.Loading)
    val recentVideosState: StateFlow<UiState<List<VideoModel>>> = _recentVideosState.asStateFlow()

    // Category Screen videos state
    private val _categoryVideosState = MutableStateFlow<UiState<List<VideoModel>>>(UiState.Success(emptyList()))
    val categoryVideosState: StateFlow<UiState<List<VideoModel>>> = _categoryVideosState.asStateFlow()

    // Active Category Filter
    private val _activeCategory = MutableStateFlow("Semua")
    val activeCategory: StateFlow<String> = _activeCategory.asStateFlow()

    // Favorites collected reactively from Room DB Flow
    val favoriteVideos: StateFlow<List<VideoModel>> = repository.allFavorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Keeps track of the API index we are currently on to show in "Pengaturan" 
    private val _currentApiKeyIndex = MutableStateFlow(1)
    val currentApiKeyIndex: StateFlow<Int> = _currentApiKeyIndex.asStateFlow()

    // Cache of all fetched videos so we can randomize/shuffle or search category locally as well
    private var allFetchedVideosCache = listOf<VideoModel>()

    init {
        // Load stored user if any
        val savedUserId = sharedPrefs.getString("user_id", null)
        if (savedUserId != null) {
            _currentUser.value = GoogleUser(
                id = savedUserId,
                displayName = sharedPrefs.getString("user_display_name", ""),
                email = sharedPrefs.getString("user_email", ""),
                photoUrl = sharedPrefs.getString("user_photo_url", "")
            )
        }

        // Start splash delay and load initial data
        viewModelScope.launch {
            // Initiate parallel loads
            val popularJob = launch { loadPopularVideos() }
            val recentJob = launch { loadRandomOrRecentVideos() }
            
            // Wait for data load and display splash for at least 2500ms
            delay(2500)
            _isSplashLoading.value = false
            
            // Sync API Key Index Tracker
            _currentApiKeyIndex.value = repository.getActiveKeyIndex()
        }
    }

    fun loginUser(user: GoogleUser) {
        _currentUser.value = user
        sharedPrefs.edit()
            .putString("user_id", user.id)
            .putString("user_display_name", user.displayName ?: "")
            .putString("user_email", user.email ?: "")
            .putString("user_photo_url", user.photoUrl ?: "")
            .apply()
    }

    fun logoutUser() {
        _currentUser.value = null
        sharedPrefs.edit().clear().apply()
    }

    fun setTab(tab: CurrentTab) {
        _currentTab.value = tab
        // If switched to Category and data hasn't loaded yet, run category filter
        if (tab == CurrentTab.KATEGORI && _activeCategory.value == "Semua") {
            setCategory("Semua")
        }
    }

    fun loadPopularVideos() {
        viewModelScope.launch {
            _popularVideosState.value = UiState.Loading
            try {
                val videos = repository.getPopularVideos()
                _popularVideosState.value = UiState.Success(videos)
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Error loading popular videos", e)
                _popularVideosState.value = UiState.Error(e.localizedMessage ?: "Gagal memuat video terpopuler")
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            }
        }
    }

    fun loadRandomOrRecentVideos() {
        viewModelScope.launch {
            _recentVideosState.value = UiState.Loading
            try {
                var videos = repository.getRandomOrRecentVideos()
                
                // Shuffle to deliver "data acak dari chanel" (random data from channel)
                if (videos.isNotEmpty()) {
                    allFetchedVideosCache = videos
                    videos = videos.shuffled()
                }
                
                _recentVideosState.value = UiState.Success(videos)
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Error loading recent videos", e)
                _recentVideosState.value = UiState.Error(e.localizedMessage ?: "Gagal memuat video acak")
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            }
        }
    }

    // Refresh everything
    fun refreshAll() {
        viewModelScope.launch {
            loadPopularVideos()
            loadRandomOrRecentVideos()
            setCategory(_activeCategory.value)
        }
    }

    // Key categories based on Ponpes Jaya Baru Tasawuf title patterns:
    // "Kategori menampilkan data berdasarkan judul dari chanel..."
    fun setCategory(categoryName: String) {
        _activeCategory.value = categoryName
        viewModelScope.launch {
            _categoryVideosState.value = UiState.Loading
            try {
                // If we have cached videos, we can filter locally or fetch from API
                // Let's do a smart hybrid: Fetch from YouTube API search query if a specific tag is clicked,
                // or fall back to high quality local regex filtering on titles for maximum speed!
                val searchQueryMap = mapOf(
                    "Ilmu Tasawuf" to "Tasawuf",
                    "Tauhid & Aqidah" to "Tauhid",
                    "Amalan & Sholawat" to "Amalan",
                    "Kajian Ruhani" to "Kajian",
                    "Zikir & Wirid" to "Zikir",
                    "Tanya Jawab" to "Tanya"
                )

                val query = searchQueryMap[categoryName]
                if (query != null) {
                    // Fetch directly from API using channel specific search query!
                    val apiResults = repository.searchVideosByQuery(query)
                    _categoryVideosState.value = UiState.Success(apiResults)
                } else {
                    // "Semua" or default -> fall back to cache or fetch recent
                    if (allFetchedVideosCache.isEmpty()) {
                        allFetchedVideosCache = repository.getRandomOrRecentVideos()
                    }
                    _categoryVideosState.value = UiState.Success(allFetchedVideosCache)
                }
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Error loading category $categoryName", e)
                // Local filter in cache if API fails during category switch as absolute robust fallback!
                val localFiltered = filterCacheByCategoryName(categoryName)
                _categoryVideosState.value = UiState.Success(localFiltered)
                _currentApiKeyIndex.value = repository.getActiveKeyIndex()
            }
        }
    }

    private fun filterCacheByCategoryName(categoryName: String): List<VideoModel> {
        val lowercaseCat = categoryName.lowercase()
        if (lowercaseCat == "semua" || allFetchedVideosCache.isEmpty()) {
            return allFetchedVideosCache
        }
        
        return allFetchedVideosCache.filter { video ->
            val title = video.title.lowercase()
            when (categoryName) {
                "Ilmu Tasawuf" -> title.contains("tasawuf") || title.contains("sufi") || title.contains("hakikat") || title.contains("makrifat") || title.contains("tarekat")
                "Tauhid & Aqidah" -> title.contains("tauhid") || title.contains("iman") || title.contains("aqidah") || title.contains("allah")
                "Amalan & Sholawat" -> title.contains("amalan") || title.contains("sholawat") || title.contains("doa") || title.contains("rotib")
                "Kajian Ruhani" -> title.contains("kajian") || title.contains("ruhani") || title.contains("qolbu") || title.contains("jiwa")
                "Zikir & Wirid" -> title.contains("zikir") || title.contains("wirid") || title.contains("ratib")
                "Tanya Jawab" -> title.contains("tanya") || title.contains("jawab") || title.contains("penjelasan")
                else -> true
            }
        }
    }

    // Toggle Favorite Action
    fun toggleFavorite(video: VideoModel) {
        viewModelScope.launch {
            val isFav = repository.isFavoriteVideo(video.id)
            if (isFav) {
                repository.removeFavorite(video.id)
            } else {
                repository.addFavorite(video)
            }
        }
    }

    // Force rotation of API key manually in Settings (Pengaturan)
    fun rotateApiKeyManually() {
        viewModelScope.launch {
            // Perform dummy task to rotate key, or call next
            _categoryVideosState.value = UiState.Loading
            try {
                // Just force load popular videos which triggers retry and rotate tracker
                // Or simply notify the repository to rotate
                val dummyClass = repository.javaClass.getDeclaredField("activeKeyIndex")
                dummyClass.isAccessible = true
                val currentIdx = dummyClass.get(repository) as Int
                val nextIdx = (currentIdx + 1) % 5
                dummyClass.set(repository, nextIdx)
                _currentApiKeyIndex.value = nextIdx + 1
                Log.d("YouTubeViewModel", "Manually rotated API key. New active index: ${nextIdx + 1}")
                // Reload and refresh lists with the new key!
                refreshAll()
            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Error in manual key rotation", e)
            }
        }
    }

    // Video Download operations
    fun startDownload(video: VideoModel) {
        viewModelScope.launch {
            val dao = db.downloadedVideoDao()
            val existing = dao.getDownloadById(video.id)
            if (existing != null && existing.status == "COMPLETED") {
                Log.d("YouTubeViewModel", "Video ${video.id} already exists offline.")
                return@launch
            }

            val downloadsDir = java.io.File(getApplication<Application>().filesDir, "downloads")
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val localFile = java.io.File(downloadsDir, "${video.id}.mp4")

            val entity = DownloadedVideoEntity(
                id = video.id,
                title = video.title,
                description = video.description,
                thumbnailUrl = video.thumbnailUrl,
                publishedAt = video.publishedAt,
                localFilePath = localFile.absolutePath,
                downloadProgress = 0,
                status = "DOWNLOADING"
            )
            dao.insertDownload(entity)

            // Increment progress in a separate coroutine flow
            viewModelScope.launch {
                try {
                    for (p in 10..100 step 20) {
                        delay(400)
                        dao.updateProgress(video.id, p, if (p == 100) "COMPLETED" else "DOWNLOADING")
                    }
                    // Write a dummy metadata file representing offline video stream
                    localFile.writeText("Bismillah. Media kajian offline Ponpes Jaya Baru: id=${video.id}, judul=${video.title}")
                    Log.d("YouTubeViewModel", "Downloaded video written locally: ${localFile.absolutePath}")
                } catch (e: Exception) {
                    Log.e("YouTubeViewModel", "Download system error for ID ${video.id}", e)
                    dao.updateProgress(video.id, 0, "FAILED")
                }
            }
        }
    }

    fun deleteDownload(videoId: String) {
        viewModelScope.launch {
            val dao = db.downloadedVideoDao()
            val existing = dao.getDownloadById(videoId)
            if (existing != null) {
                try {
                    val file = java.io.File(existing.localFilePath)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    Log.e("YouTubeViewModel", "Failed to delete offline media file", e)
                }
                dao.deleteDownloadById(videoId)
                Log.d("YouTubeViewModel", "Successfully deleted offline download: $videoId")
            }
        }
    }

    // --- Chat AI Feature Section ---

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Assalamualaikum Warahmatullahi Wabarakatuh.\n\nSelamat datang di pusat bimbingan rohani Ponpes Jaya Baru. Saya siap mendampingi Anda berdiskusi mengenai ilmu Tauhid, Tasawuf (kajian rohani), Tazkiyatun Nafs (pembersihan jiwa), atau menjawab berbagai pertanyaan keislaman lainnya.\n\nSilakan sampaikan pertanyaan atau keluh kesah spiritual Anda, insyaAllah saya bantu bimbing.",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun resetChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Assalamualaikum Warahmatullahi Wabarakatuh.\n\nSelamat datang di bimbingan rohani Ponpes Jaya Baru. Ada hal atau bimbingan ilmu yang ingin Anda tanyakan?",
                isUser = false
            )
        )
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        
        val userMsg = ChatMessage(text = text, isUser = true)
        val currentList = _chatMessages.value
        _chatMessages.value = currentList + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                
                val fullHistory = currentList + userMsg
                val contents = fullHistory.takeLast(12).map { msg ->
                    com.example.api.GeminiContent(
                        parts = listOf(com.example.api.GeminiPart(text = msg.text))
                    )
                }

                val systemInstruction = com.example.api.GeminiContent(
                    parts = listOf(
                        com.example.api.GeminiPart(
                            text = "You are a highly compassionate and polite virtual Islamic guide (Asisten Bimbingan Rohani / Ustadz AI) representing Pondok Pesantren Jaya Baru (Ponpes Jabaru). " +
                                   "You specialize in the deep spiritual and moral aspects of Islam: Tauhid (Pure Monotheism), Tasawuf (Sufi Spiritual Path), and Tazkiyatun Nafs (Purification of the Heart/Soul). " +
                                   "Always answer questions in respectful, gentle, and warm Indonesian (Bahasa Indonesia). " +
                                   "End answers occasionally with a gentle prayer (dua) or words of encouragement like \"InsyaAllah\" or \"Semoga Allah merahmati kita\". " +
                                   "If asked about Ponpes Jaya Baru or Ponpes Jabaru, tell them they are welcome to study and watch the live streams or offline downloads of the Ponpes Jabaru lectures."
                        )
                    )
                )

                val request = com.example.api.GeminiRequest(
                    contents = contents,
                    generationConfig = com.example.api.GeminiGenerationConfig(
                        temperature = 0.7f,
                        maxOutputTokens = 1500
                    ),
                    systemInstruction = systemInstruction
                )

                val response = com.example.api.GeminiRetrofitClient.service.generateContent(apiKey, request)
                val replyText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Maaf, saat ini saya tidak dapat merumuskan jawaban. Mari coba sampaikan pertanyaan Anda kembali."

                _chatMessages.value = _chatMessages.value + ChatMessage(text = replyText, isUser = false)
            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Gemini API error occurred", e)
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    text = "Mohon maaf, terjadi gangguan koneksi internet atau sistem bimbingan online kami sedang penuh. Silakan coba kirim ulang pertanyaan Anda sebentar lagi.\n\n(Hubungi Bimbingan Rohani Ponpes secara manual atau periksa internet Anda. Error: ${e.localizedMessage ?: "Koneksi terputus"})",
                    isUser = false
                )
            } finally {
                _isChatLoading.value = false
            }
        }
    }
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

enum class CurrentTab {
    HOME, KATEGORI, CHAT_AI, FAVORIT, PENGATURAN
}
