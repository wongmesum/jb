package com.example.ui

import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.data.*
import com.example.ui.theme.ArtisanGreen
import com.example.ui.theme.ArtisanLightGreen
import com.example.ui.theme.ArtisanLimeHighlight
import com.example.ui.theme.ArtisanDarkContrast
import com.example.ui.theme.ArtisanMutedText
import com.example.ui.theme.ArtisanDividerColor
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun AppSplashScreen(onTimeout: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
        label = "Splash Alpha"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1.05f else 0.85f,
        animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
        label = "Splash Scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E200C),
                        ArtisanGreen,
                        Color(0xFF131505)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Elegant Mandala / Islamic Pattern drawn dynamically via Canvas
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .shadow(16.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF152114)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ponpes_logo),
                    contentDescription = "Logo Ponpes Jaya Baru",
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Subtitle
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                color = ArtisanLimeHighlight,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App Main Title
            Text(
                text = "PONPES JAYA BARU",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim)
            )

            // Topic description
            Text(
                text = "Bimbingan Ilmu Tasawuf",
                color = ArtisanLimeHighlight.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Credits text as requested: "Ponpes Jayabaru by PBM 2105"
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .alpha(alphaAnim)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Ponpes Jayabaru by PBM 2105",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// Gorgeous Custom Islamic Geometric Emblem
@Composable
fun IslamicMandalaLogo(modifier: Modifier = Modifier) {
    val goldColor = ArtisanLimeHighlight
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radiusOuter = size.width * 0.45f
        val radiusInner = size.width * 0.25f

        // Draw Outer circle
        drawCircle(
            color = goldColor,
            radius = radiusOuter,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw Inner circle
        drawCircle(
            color = goldColor,
            radius = radiusInner,
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Draw 8-point geometric Islamic star (Octagram)
        val path = Path()
        val numPoints = 8
        for (i in 0 until numPoints * 2) {
            val radius = if (i % 2 == 0) radiusOuter else radiusInner
            val angle = i * Math.PI / numPoints
            val x = (centerX + Math.cos(angle) * radius).toFloat()
            val y = (centerY + Math.sin(angle) * radius).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        drawPath(
            path = path,
            color = goldColor,
            style = Stroke(width = 2.dp.toPx())
        )

        // Additional internal geometric cross
        for (i in 0 until 4) {
            val angle = i * Math.PI / 2
            val x1 = (centerX + Math.cos(angle) * radiusOuter).toFloat()
            val y1 = (centerY + Math.sin(angle) * radiusOuter).toFloat()
            val x2 = (centerX + Math.cos(angle + Math.PI) * radiusOuter).toFloat()
            val y2 = (centerY + Math.sin(angle + Math.PI) * radiusOuter).toFloat()
            drawLine(
                color = goldColor.copy(alpha = 0.4f),
                start = androidx.compose.ui.geometry.Offset(x1, y1),
                end = androidx.compose.ui.geometry.Offset(x2, y2),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppFrame(viewModel: YouTubeViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isSplashLoading by viewModel.isSplashLoading.collectAsState()

    var activePlaybackVideo by remember { mutableStateOf<VideoModel?>(null) }

    if (isSplashLoading) {
        AppSplashScreen(onTimeout = {})
    } else {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_ponpes_logo),
                                    contentDescription = "Logo Ponpes",
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, ArtisanLimeHighlight, CircleShape)
                                )
                                Column {
                                    Text(
                                        "Ponpes Jabaru",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Text(
                                        "Bimbingan Ilmu Tauhid",
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                        fontFamily = FontFamily.Serif,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        actions = {
                            IconButton(onClick = { viewModel.refreshAll() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Muat Ulang Data",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), thickness = 0.5.dp)
                }
            },
            bottomBar = {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), thickness = 0.5.dp)
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp
                    ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontWeight = FontWeight.SemiBold) },
                        selected = currentTab == CurrentTab.HOME,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArtisanGreen,
                            selectedTextColor = ArtisanGreen,
                            indicatorColor = ArtisanLimeHighlight
                        ),
                        onClick = { viewModel.setTab(CurrentTab.HOME) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Category, contentDescription = "Kategori") },
                        label = { Text("Kategori", fontWeight = FontWeight.SemiBold) },
                        selected = currentTab == CurrentTab.KATEGORI,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArtisanGreen,
                            selectedTextColor = ArtisanGreen,
                            indicatorColor = ArtisanLimeHighlight
                        ),
                        onClick = { viewModel.setTab(CurrentTab.KATEGORI) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Chat, contentDescription = "Tanya AI") },
                        label = { Text("Tanya AI", fontWeight = FontWeight.SemiBold) },
                        selected = currentTab == CurrentTab.CHAT_AI,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArtisanGreen,
                            selectedTextColor = ArtisanGreen,
                            indicatorColor = ArtisanLimeHighlight
                        ),
                        onClick = { viewModel.setTab(CurrentTab.CHAT_AI) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorit") },
                        label = { Text("Favorit", fontWeight = FontWeight.SemiBold) },
                        selected = currentTab == CurrentTab.FAVORIT,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArtisanGreen,
                            selectedTextColor = ArtisanGreen,
                            indicatorColor = ArtisanLimeHighlight
                        ),
                        onClick = { viewModel.setTab(CurrentTab.FAVORIT) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
                        label = { Text("Pengaturan", fontWeight = FontWeight.SemiBold) },
                        selected = currentTab == CurrentTab.PENGATURAN,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArtisanGreen,
                            selectedTextColor = ArtisanGreen,
                            indicatorColor = ArtisanLimeHighlight
                        ),
                        onClick = { viewModel.setTab(CurrentTab.PENGATURAN) }
                    )
                }
            }
        }
    ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (currentTab) {
                    CurrentTab.HOME -> HomeScreen(viewModel, onVideoClick = { activePlaybackVideo = it })
                    CurrentTab.KATEGORI -> KategoriScreen(viewModel, onVideoClick = { activePlaybackVideo = it })
                    CurrentTab.CHAT_AI -> ChatAiScreen(viewModel)
                    CurrentTab.FAVORIT -> FavoritScreen(viewModel, onVideoClick = { activePlaybackVideo = it })
                    CurrentTab.PENGATURAN -> PengaturanScreen(viewModel)
                }
            }
        }

        // Active video player floating card overlay dialog
        activePlaybackVideo?.let { video ->
            VideoPlayerDialog(
                video = video,
                viewModel = viewModel,
                onDismiss = { activePlaybackVideo = null }
            )
        }
    }
}

@Composable
fun HomeScreen(viewModel: YouTubeViewModel, onVideoClick: (VideoModel) -> Unit) {
    val popularState by viewModel.popularVideosState.collectAsState()
    val recentState by viewModel.recentVideosState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Spiritual Header Space Spacer (no long banner card)
        item {
            val currentUser by viewModel.currentUser.collectAsState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Async avatar with default fallback
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .border(1.dp, ArtisanLimeHighlight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser != null && !currentUser!!.photoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = currentUser!!.photoUrl,
                            contentDescription = "Avatar ${currentUser!!.displayName}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_ponpes_logo),
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(14.dp))
                
                Column {
                    val name = currentUser?.displayName ?: "Jama'ah Tarbiyah"
                    Text(
                        text = "Assalamu'alaikum,",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // 1. POPULAR VERTICAL VIDEO SLIDER
        item {
            Column(modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Popular",
                            tint = ArtisanGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kajian Terpopuler",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    Text(
                        text = "Video Utama",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArtisanGreen
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                when (val state = popularState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = ArtisanGreen)
                        }
                    }
                    is UiState.Success -> {
                        val items = state.data
                        if (items.isEmpty()) {
                            Text(
                                text = "Tidak ada video populer tersedia.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        } else {
                            val pagerState = rememberPagerState(pageCount = { items.size })
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(210.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    pageSpacing = 16.dp
                                ) { page ->
                                    val video = items[page]
                                    PopularVerticalVideoCard(
                                        video = video,
                                        onClick = { onVideoClick(video) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(items.size) { index ->
                                        HomeScreenIndicatorDot(isSelected = pagerState.currentPage == index)
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        ErrorStateView(
                            message = state.message,
                            onRetry = { viewModel.loadPopularVideos() }
                        )
                    }
                }
            }
        }

        // 2. RANDOM/RECENT VIDEO GRID - 2 Baris ke bawah
        item {
            Column(modifier = Modifier.padding(top = 28.dp, start = 16.dp, end = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Acak",
                        tint = ArtisanGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kajian Acak Ponpes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily.Serif
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // We convert the grid items to vertical scroll elements chunked by 2 columns for beautiful scrolling inside LazyColumn
        when (val state = recentState) {
            is UiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ArtisanGreen)
                    }
                }
            }
            is UiState.Success -> {
                val videos = state.data
                if (videos.isEmpty()) {
                    item {
                        Text(
                            text = "Tidak dapat memuat daftar video kajian.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                } else {
                    // Chunk list into pairs to simulate a 2-column grid in a parent LazyColumn
                    val chunks = videos.chunked(2)
                    items(chunks) { rowOfVideos ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            for (video in rowOfVideos) {
                                Box(modifier = Modifier.weight(1f)) {
                                    GridVideoCard(
                                        video = video,
                                        onClick = { onVideoClick(video) }
                                    )
                                }
                            }
                            if (rowOfVideos.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            is UiState.Error -> {
                item {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { viewModel.loadRandomOrRecentVideos() }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenIndicatorDot(isSelected: Boolean) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 18.dp else 6.dp,
        label = "indicator_width"
    )
    Box(
        modifier = Modifier
            .height(6.dp)
            .width(width)
            .clip(CircleShape)
            .background(
                if (isSelected) ArtisanGreen else ArtisanGreen.copy(alpha = 0.3f)
            )
    )
}

@Composable
fun PopularVerticalVideoCard(video: VideoModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .clickable(onClick = onClick)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Full height background thumbnail
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dynamic scrim gradient overlay to make text highly readable
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Play overlay icon floating in middle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(ArtisanLimeHighlight.copy(alpha = 0.9f), CircleShape)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Mainkan",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text info content on bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                // Video Label
                Box(
                    modifier = Modifier
                        .background(ArtisanGreen, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "POPULER",
                        color = ArtisanLimeHighlight,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = video.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@Composable
fun GridVideoCard(video: VideoModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Miniature play badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Main",
                        tint = ArtisanLimeHighlight,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = video.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ponpes Jaya Baru",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun KategoriScreen(viewModel: YouTubeViewModel, onVideoClick: (VideoModel) -> Unit) {
    val activeCategory by viewModel.activeCategory.collectAsState()
    val categoryState by viewModel.categoryVideosState.collectAsState()

    val categories = listOf(
        "Semua",
        "Ilmu Tasawuf",
        "Tauhid & Aqidah",
        "Amalan & Sholawat",
        "Kajian Ruhani",
        "Zikir & Wirid",
        "Tanya Jawab"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ArtisanGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Klasifikasi Bimbingan Ilmu",
                    color = ArtisanLimeHighlight,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Silakan pilih cabang kajian tasawuf di bawah untuk menyaring bimbingan dan ceramah Ponpes Jaya Baru.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.2.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Horizontal Category Scrolling Filter Raw Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = activeCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setCategory(category) },
                    label = { 
                        Text(
                            text = category, 
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        ) 
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ArtisanGreen,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.shadow(if (isSelected) 1.dp else 0.dp, RoundedCornerShape(24.dp))
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), thickness = 0.5.dp)

        // Content Area 
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = categoryState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ArtisanGreen)
                    }
                }
                is UiState.Success -> {
                    val list = state.data
                    if (list.isEmpty()) {
                        EmptyStateView(
                            title = "Tidak Ada Hasil",
                            subtitle = "Kajian video bertitle '$activeCategory' belum dapat dimuat saat ini. Hubungi admin atau coba kategori lainnya."
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(list) { video ->
                                GridVideoCard(video = video, onClick = { onVideoClick(video) })
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { viewModel.setCategory(activeCategory) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritScreen(viewModel: YouTubeViewModel, onVideoClick: (VideoModel) -> Unit) {
    val favorites by viewModel.favoriteVideos.collectAsState()
    val downloadedVideos by viewModel.downloadedVideos.collectAsState()
    val studyNotes by viewModel.studyNotes.collectAsState()
    var activeSubTab by remember { mutableStateOf(0) } // 0: Favorit Saya, 1: Hasil Unduhan, 2: Catatan Kajian

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ArtisanGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Perpustakaan Spiritual",
                    color = ArtisanLimeHighlight,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Akses video kajian tasawuf yang Anda sukai, hasil unduhan, dan ringkasan catatan penuntut ilmu.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.2.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Segmented Control/Sub-Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Tab 1: Favorit Saya
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (activeSubTab == 0) ArtisanGreen else Color.Transparent)
                    .clickable { activeSubTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Favorit (${favorites.size})",
                    color = if (activeSubTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Tab 2: Hasil Unduhan
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (activeSubTab == 1) ArtisanGreen else Color.Transparent)
                    .clickable { activeSubTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                val completedCount = downloadedVideos.count { status -> status.status == "COMPLETED" }
                val countText = if (completedCount > 0) " ($completedCount)" else ""
                Text(
                    text = "Unduhan$countText",
                    color = if (activeSubTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Tab 3: Catatan Kajian
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (activeSubTab == 2) ArtisanGreen else Color.Transparent)
                    .clickable { activeSubTab = 2 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                val notesCountText = if (studyNotes.isNotEmpty()) " (${studyNotes.size})" else ""
                Text(
                    text = "Catatan$notesCountText",
                    color = if (activeSubTab == 2) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (activeSubTab == 0) {
            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        title = "Koleksi Favorit Kosong",
                        subtitle = "Silakan tandai video kajian terbaik Ponpes Jaya Baru dengan menekan ikon bintang pada detail pemutar video."
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    items(favorites) { video ->
                        GridVideoCard(video = video, onClick = { onVideoClick(video) })
                    }
                }
            }
        } else if (activeSubTab == 1) {
            val completedDownloads = downloadedVideos.filter { it.status == "COMPLETED" }
            if (completedDownloads.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        title = "Belum Ada Unduhan",
                        subtitle = "Unduh kajian pilihan Anda dari Ponpes Jaya Baru dengan menekan ikon unduh pada detail pemutar video untuk menyimpannya di perangkat Anda."
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    items(completedDownloads) { download ->
                        val mappedVideo = VideoModel(
                            id = download.id,
                            title = download.title,
                            description = download.description,
                            thumbnailUrl = download.thumbnailUrl,
                            publishedAt = download.publishedAt,
                            isFavorite = false
                        )
                        GridVideoCard(video = mappedVideo, onClick = { onVideoClick(mappedVideo) })
                    }
                }
            }
        } else {
            // Catatan Kajian
            if (studyNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateView(
                        title = "Belum Ada Catatan Kajian",
                        subtitle = "Tahun ini mari giat menulis ilmu! Catat rangkuman kajian ketika Anda memutar video di pemutar utama."
                    )
                }
            } else {
                var editingNote by remember { mutableStateOf<com.example.data.database.StudyNoteEntity?>(null) }
                
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    items(studyNotes) { note ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(12.dp)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Book,
                                            contentDescription = "Judul Kajian",
                                            tint = ArtisanGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = note.videoTitle,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ArtisanGreen,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        IconButton(
                                            onClick = { editingNote = note },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Catatan",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.deleteStudyNote(note) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus Catatan",
                                                tint = Color.Red,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                Text(
                                    text = note.noteText,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                val format = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
                                val dateStr = format.format(java.util.Date(note.timestamp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateStr,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                    
                                    // play the associated video
                                    val dbVideo = remember { mutableStateOf<VideoModel?>(null) }
                                    LaunchedEffect(note.videoId) {
                                        val cacheMatch = viewModel.popularVideosState.value.let { 
                                            if (it is UiState.Success) it.data.find { v -> v.id == note.videoId } else null 
                                        } ?: viewModel.recentVideosState.value.let {
                                            if (it is UiState.Success) it.data.find { v -> v.id == note.videoId } else null
                                        } ?: downloadedVideos.find { v -> v.id == note.videoId }?.let {
                                            VideoModel(it.id, it.title, it.description, it.thumbnailUrl, it.publishedAt)
                                        }
                                        dbVideo.value = cacheMatch ?: VideoModel(
                                            id = note.videoId,
                                            title = note.videoTitle,
                                            description = "Kajian Ponpes Jaya Baru",
                                            thumbnailUrl = "",
                                            publishedAt = ""
                                        )
                                    }
                                    
                                    TextButton(
                                        onClick = { dbVideo.value?.let { onVideoClick(it) } },
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.height(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Putar",
                                            tint = ArtisanGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Tonton Kajian",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ArtisanGreen
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Edit Dialog
                editingNote?.let { note ->
                    var tempText by remember { mutableStateOf(note.noteText) }
                    AlertDialog(
                        onDismissRequest = { editingNote = null },
                        title = { Text("Edit Catatan Kajian", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                        text = {
                            OutlinedTextField(
                                value = tempText,
                                onValueChange = { tempText = it },
                                label = { Text("Isi Catatan") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.updateStudyNote(note.copy(noteText = tempText, timestamp = System.currentTimeMillis()))
                                    editingNote = null
                                }
                            ) {
                                Text("Simpan", color = ArtisanGreen, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { editingNote = null }) {
                                Text("Batal")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PengaturanScreen(viewModel: YouTubeViewModel) {
    val keyIndex by viewModel.currentApiKeyIndex.collectAsState()
    val displaySettings by viewModel.displaySettings.collectAsState()
    val notificationSettings by viewModel.notificationSettings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Sub-tab: 0 = Notifikasi, 1 = Tampilan, 2 = Akun & Sistem
    var activeSettingsTab by remember { mutableStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updateNotificationsEnabled(true)
            Toast.makeText(context, "Izin notifikasi berhasil diaktifkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Izin notifikasi tidak diberikan oleh sistem", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Identity Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ArtisanGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF152114))
                            .border(1.5.dp, ArtisanLimeHighlight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ponpes_logo),
                            contentDescription = "Logo Ponpes Jaya Baru",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pengaturan & Preferensi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = ArtisanLimeHighlight,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Kelola preferensi notifikasi kajian dan kenyamanan tampilan aplikasi Ponpes Jaya Baru.",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Sub-Tab Switcher (Notifikasi / Tampilan / Akun & Info)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Tab 0: Notifikasi
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeSettingsTab == 0) ArtisanGreen else Color.Transparent)
                        .clickable { activeSettingsTab = 0 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (notificationSettings.enabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = if (activeSettingsTab == 0) ArtisanLimeHighlight else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Notifikasi",
                            color = if (activeSettingsTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tab 1: Tampilan
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeSettingsTab == 1) ArtisanGreen else Color.Transparent)
                        .clickable { activeSettingsTab = 1 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = if (activeSettingsTab == 1) ArtisanLimeHighlight else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tampilan",
                            color = if (activeSettingsTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Tab 2: Akun & Info
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeSettingsTab == 2) ArtisanGreen else Color.Transparent)
                        .clickable { activeSettingsTab = 2 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = if (activeSettingsTab == 2) ArtisanLimeHighlight else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Akun & Info",
                            color = if (activeSettingsTab == 2) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ================= TAB 0: NOTIFIKASI =================
        if (activeSettingsTab == 0) {
            // Master Notification Switch
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (notificationSettings.enabled) ArtisanGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (notificationSettings.enabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                contentDescription = null,
                                tint = if (notificationSettings.enabled) ArtisanGreen else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Notifikasi Bimbingan",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (notificationSettings.enabled) ArtisanGreen else Color.Gray.copy(alpha = 0.3f),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (notificationSettings.enabled) "AKTIF" else "NONAKTIF",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (notificationSettings.enabled) ArtisanLimeHighlight else Color.White
                                    )
                                }
                            }
                            Text(
                                text = "Pemberitahuan pembaruan kajian tasawuf dan pengingat rohani",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = notificationSettings.enabled,
                            onCheckedChange = { isEnabled ->
                                if (isEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        return@Switch
                                    }
                                }
                                viewModel.updateNotificationsEnabled(isEnabled)
                                val msg = if (isEnabled) "Notifikasi bimbingan diaktifkan" else "Notifikasi bimbingan dinonaktifkan"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ArtisanLimeHighlight,
                                checkedTrackColor = ArtisanGreen
                            )
                        )
                    }
                }
            }

            // Notification Categories & Feedback Options (Visible when enabled)
            if (notificationSettings.enabled) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(14.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Kategori Pemberitahuan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArtisanGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Switch: Video & Kajian Baru
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OndemandVideo,
                                    contentDescription = null,
                                    tint = ArtisanGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Kajian & Video Baru",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Pemberitahuan saat video ceramah baru diunggah ke YouTube Ponpes",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                        lineHeight = 14.sp
                                    )
                                }
                                Switch(
                                    checked = notificationSettings.newVideoNotify,
                                    onCheckedChange = { viewModel.updateNewVideoNotify(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ArtisanLimeHighlight,
                                        checkedTrackColor = ArtisanGreen
                                    )
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            )

                            // Switch: Pengingat Rohani & Zikir Harian
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ArtisanLimeHighlight,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Pengingat Zikir & Rohani Harian",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Mutiara hikmah tasawuf harian untuk penenang kalbu",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                        lineHeight = 14.sp
                                    )
                                }
                                Switch(
                                    checked = notificationSettings.dailyReminderNotify,
                                    onCheckedChange = { viewModel.updateDailyReminderNotify(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ArtisanLimeHighlight,
                                        checkedTrackColor = ArtisanGreen
                                    )
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            )

                            // Switch: Vibration
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = null,
                                    tint = ArtisanGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Getaran Notifikasi",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Ponsel bergetar saat notifikasi tiba",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                    )
                                }
                                Switch(
                                    checked = notificationSettings.vibrationEnabled,
                                    onCheckedChange = { viewModel.updateVibrationEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ArtisanLimeHighlight,
                                        checkedTrackColor = ArtisanGreen
                                    )
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            )

                            // Switch: Sound
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = ArtisanGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Suara Notifikasi",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Bunyikan nada pemberitahuan standar perangkat",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                    )
                                }
                                Switch(
                                    checked = notificationSettings.soundEnabled,
                                    onCheckedChange = { viewModel.updateSoundEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ArtisanLimeHighlight,
                                        checkedTrackColor = ArtisanGreen
                                    )
                                )
                            }
                        }
                    }
                }

                // Background Check Frequency
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(14.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = ArtisanGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Frekuensi Pengecekan Video Baru",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ArtisanGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Seberapa rutin sistem memeriksa unggahan kajian baru di latar belakang:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CheckFrequency.values().forEach { freq ->
                                    val isSelected = notificationSettings.frequency == freq
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) ArtisanGreen else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                            )
                                            .border(
                                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                                color = if (isSelected) ArtisanLimeHighlight else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { viewModel.updateNotificationFrequency(freq) }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = freq.label.replace("Setiap ", "").replace(" (24 Jam)", ""),
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Test Notification & Reset
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(14.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Pengujian & Tindakan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArtisanGreen
                            )
                            Text(
                                text = "Kirim notifikasi contoh untuk memastikan suara, getaran, dan saluran pemberitahuan bekerja sempurna.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                lineHeight = 14.sp
                            )

                            Button(
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        val sent = viewModel.sendTestNotification()
                                        if (sent) {
                                            Toast.makeText(context, "Uji notifikasi dikirim! Periksa bilah atas layar ponsel Anda.", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, "Gagal mengirimkan notifikasi uji coba.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = "Test Notification",
                                    tint = ArtisanLimeHighlight
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kirim Uji Coba Notifikasi", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.resetNotificationSettings()
                                    Toast.makeText(context, "Preferensi notifikasi dikembalikan ke default", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = "Reset", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kembalikan Pengaturan Notifikasi Default", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // ================= TAB 1: TAMPILAN =================
        if (activeSettingsTab == 1) {
            // Theme Mode Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = ArtisanGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tema Tampilan Aplikasi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArtisanGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pilih nuansa warna yang paling sejuk dan nyaman di mata:",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ThemeMode.values().forEach { mode ->
                                val isSelected = displaySettings.themeMode == mode
                                val icon = when (mode) {
                                    ThemeMode.LIGHT -> Icons.Default.LightMode
                                    ThemeMode.DARK -> Icons.Default.DarkMode
                                    ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) ArtisanGreen else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 0.5.dp,
                                            color = if (isSelected) ArtisanLimeHighlight else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.updateThemeMode(mode) }
                                        .padding(vertical = 12.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = if (isSelected) ArtisanLimeHighlight else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = mode.title,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Dipilih",
                                                tint = ArtisanLimeHighlight,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = displaySettings.themeMode.description,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Text Scaling Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = null,
                                tint = ArtisanGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Skala Ukuran Teks",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArtisanGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Menyesuaikan keterbacaan judul kitab, ceramah, dan catatan:",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextScale.values().forEach { scale ->
                                val isSelected = displaySettings.textScale == scale
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) ArtisanGreen else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        )
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) ArtisanLimeHighlight else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.updateTextScale(scale) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = scale.title,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Live Preview Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ArtisanGreen.copy(alpha = 0.07f), RoundedCornerShape(10.dp))
                                .border(0.5.dp, ArtisanGreen.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArtisanGreen,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Contoh: Bimbingan Tasawuf & Penjernihan Hati (Tazkiyatun Nafs)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Pratinjau langsung ukuran huruf aplikasi sesuai skala yang dipilih.",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }
                }
            }

            // Card Density & Layout
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DashboardCustomize,
                                contentDescription = null,
                                tint = ArtisanGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kerapatan Tata Letak Kartu",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArtisanGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        CardDensity.values().forEachIndexed { idx, density ->
                            val isSelected = displaySettings.cardDensity == density
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) ArtisanGreen.copy(alpha = 0.08f) else Color.Transparent
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) ArtisanGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.updateCardDensity(density) }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.updateCardDensity(density) },
                                    colors = RadioButtonDefaults.colors(selectedColor = ArtisanGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = density.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = density.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                    )
                                }
                            }
                            if (idx < CardDensity.values().size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Media & Player Settings
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Preferensi Media & Pemutaran",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ArtisanGreen
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // HD Thumbnails
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoSizeSelectActual,
                                contentDescription = null,
                                tint = ArtisanGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Thumbnail Resolusi Tinggi (HD)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Tampilkan cover video resolusi tinggi (matikan untuk hemat kuota)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    lineHeight = 14.sp
                                )
                            }
                            Switch(
                                checked = displaySettings.hdThumbnails,
                                onCheckedChange = { viewModel.updateHdThumbnails(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisanLimeHighlight,
                                    checkedTrackColor = ArtisanGreen
                                )
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        // Autoplay
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                tint = ArtisanGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Putar Otomatis Pemutar Video",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Memulai pemutaran video langsung saat dialog player dibuka",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    lineHeight = 14.sp
                                )
                            }
                            Switch(
                                checked = displaySettings.autoPlayPreview,
                                onCheckedChange = { viewModel.updateAutoPlayPreview(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisanLimeHighlight,
                                    checkedTrackColor = ArtisanGreen
                                )
                            )
                        }
                    }
                }
            }

            // Reset Display Settings
            item {
                OutlinedButton(
                    onClick = {
                        viewModel.resetDisplaySettings()
                        Toast.makeText(context, "Pengaturan tampilan dikembalikan ke awal", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = "Reset Tampilan")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kembalikan Pengaturan Tampilan Default", fontSize = 12.sp)
                }
            }
        }

        // ================= TAB 2: AKUN & SISTEM =================
        if (activeSettingsTab == 2) {
            // OAuth Google Sign In Card
            item {
                val gso = remember {
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestProfile()
                        .build()
                }
                val googleSignInClient = remember {
                    GoogleSignIn.getClient(context, gso)
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            if (account != null) {
                                val googleUser = GoogleUser(
                                    id = account.id ?: "",
                                    displayName = account.displayName,
                                    email = account.email,
                                    photoUrl = account.photoUrl?.toString()
                                )
                                viewModel.loginUser(googleUser)
                            }
                        } catch (e: Exception) {
                            Log.e("OAuthLogin", "Sign in failed: ${e.message}")
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Profil Jama'ah (Google OAuth)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ArtisanGreen,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (currentUser == null) {
                            Text(
                                text = "Hubungkan akun Google Anda untuk personalisasi bimbingan rohani dan sinkronisasi catatan kajian secara online.",
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val signInIntent = googleSignInClient.signInIntent
                                    launcher.launch(signInIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Google Icon",
                                    tint = ArtisanLimeHighlight
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Masuk dengan Google",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ArtisanGreen.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(ArtisanGreen.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (!currentUser!!.photoUrl.isNullOrEmpty()) {
                                        AsyncImage(
                                            model = currentUser!!.photoUrl,
                                            contentDescription = "Avatar ${currentUser!!.displayName}",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Default Avatar",
                                            tint = ArtisanGreen,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentUser!!.displayName ?: "Penuntut Ilmu",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = currentUser!!.email ?: "tiada-email@google.com",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(ArtisanGreen.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Jama'ah Tarbiyah",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = ArtisanGreen
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedButton(
                                onClick = {
                                    googleSignInClient.signOut().addOnCompleteListener {
                                        viewModel.logoutUser()
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = Color.Red)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Keluar Akun", color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Rolling API Key Details Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Sistem Rollover API Key",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ArtisanGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Untuk menjaga kestabilan akses video tanpa terkena limit kuota harian dari YouTube, aplikasi ini memakai 5 API Key yang otomatis rolling teratur saat kuota hampir tercapai.",
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ArtisanGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Kunci Aktif Saat Ini",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArtisanGreen
                                )
                                Text(
                                    text = "YouTube API Key #$keyIndex",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(ArtisanLimeHighlight, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                Text(
                                    text = "ROLLING AKTIF",
                                    color = ArtisanDarkContrast,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.rotateApiKeyManually() },
                            colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Cached, contentDescription = "Rotasi")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Rotasi Manual Ke Kunci Berikutnya")
                        }
                    }
                }
            }

            // Contact / Social media channel information
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tautan Sosial Ponpes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ArtisanGreen
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@ponpesjayabaru2022?si=3aXAkaa36MCus3CQ"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "YouTube",
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Kunjungi YouTube Ponpes", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // Credits / Branding Box
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aplikasi Pendukung Dakwah Tasawuf",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "Ponpes Jayabaru by PBM 2105",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = ArtisanGreen,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Versi 1.0.0 (Build 2026)",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

// Dialog to Play Video with internal WebView and backup options
@Composable
fun VideoPlayerDialog(
    video: VideoModel,
    viewModel: YouTubeViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val favorites by viewModel.favoriteVideos.collectAsState()
    val isFavorite = favorites.any { it.id == video.id }
    
    val downloadedVideos by viewModel.downloadedVideos.collectAsState()
    val downloadState = downloadedVideos.find { it.id == video.id }
    
    var playOffline by remember { mutableStateOf(false) }
    val videoNotes by viewModel.getNotesForVideo(video.id).collectAsState(initial = emptyList())
    var newNoteText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header of video detail Dialog
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ArtisanGreen)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pemutar Video Tasawuf",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White
                        )
                    }
                }

                // EMBED YOUTUBE VIDEO PLAYER WEBVIEW OR CUSTOM OFFLINE PLAYER CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black)
                ) {
                    if (playOffline && downloadState?.status == "COMPLETED") {
                        // Beautiful spiritual waveform animated offline media playback view!
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            ArtisanGreen, 
                                            Color(0xFF2E3114)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "Offline Playback",
                                    tint = ArtisanLimeHighlight,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Mode Offline Aktif (Simulasi Media)",
                                    color = ArtisanLimeHighlight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Beautiful animated waveform bars!
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
                                    
                                    repeat(10) { index ->
                                        val heightMultiplier by infiniteTransition.animateFloat(
                                            initialValue = 0.2f,
                                            targetValue = 1f,
                                            animationSpec = infiniteRepeatable(
                                                animation = tween(
                                                    durationMillis = 400 + (index * 70),
                                                    easing = LinearEasing
                                                ),
                                                repeatMode = RepeatMode.Reverse
                                            ),
                                            label = "bar_$index"
                                        )
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height((30 * heightMultiplier).dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(ArtisanLimeHighlight)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Memutar file audio/video offline hasil unduhan...",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    webViewClient = WebViewClient()
                                    webChromeClient = WebChromeClient()
                                    settings.apply {
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        mediaPlaybackRequiresUserGesture = false
                                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    }
                                    val autoPlay = if (viewModel.displaySettings.value.autoPlayPreview) 1 else 0
                                    loadUrl("https://www.youtube.com/embed/${video.id}?autoplay=$autoPlay&fs=1&rel=0")
                                }
                            },
                            update = { _ -> },
                            onRelease = { webView ->
                                try {
                                    webView.stopLoading()
                                    webView.clearHistory()
                                    webView.removeAllViews()
                                    webView.destroy()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Video Content Info Details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = video.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Hablun Minallah - Ponpes Jaya Baru",
                            fontSize = 11.sp,
                            color = ArtisanGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        // Favorite and share buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { viewModel.toggleFavorite(video) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorit",
                                    tint = if (isFavorite) ArtisanLimeHighlight else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, video.title)
                                        putExtra(Intent.EXTRA_TEXT, "${video.title}\nTonton kajian ini di YouTube: https://www.youtube.com/watch?v=${video.id}")
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Bagikan Kajian"))
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Bagikan",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Divider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Scrollable Description Text & Study Notes
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            Text(
                                text = "Deskripsi Kajian:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (video.description.isNotBlank()) video.description else "Mari saksikan pembahasan mendalam oleh Guru pembimbing di Ponpes Jaya Baru mengenai tazkiyatun nafs. InsyaAllah berkah.",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                                lineHeight = 16.sp
                            )
                            
                            // Delete local copy option
                            if (downloadState?.status == "COMPLETED") {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            playOffline = false
                                            viewModel.deleteDownload(video.id)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.Red.copy(alpha = 0.7f)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Hapus Hasil Unduhan Offline",
                                            color = Color.Red.copy(alpha = 0.7f),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Tulis Catatan Kajian (Tasawuf)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ArtisanGreen
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newNoteText,
                                    onValueChange = { newNoteText = it },
                                    placeholder = { Text("Tulis sari pati ilmu kajian ini...", fontSize = 11.sp) },
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.5.sp),
                                    modifier = Modifier.weight(1f),
                                    maxLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ArtisanGreen,
                                        cursorColor = ArtisanGreen
                                    )
                                )
                                Button(
                                    onClick = {
                                        if (newNoteText.isNotBlank()) {
                                            viewModel.addStudyNote(video.id, video.title, newNoteText)
                                            newNoteText = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp),
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Save, 
                                        contentDescription = "Simpan",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Simpan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        if (videoNotes.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Catatan Anda (${videoNotes.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                            items(videoNotes) { note ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = note.noteText,
                                                fontSize = 11.sp,
                                                lineHeight = 15.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            val format = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
                                            Text(
                                                text = format.format(java.util.Date(note.timestamp)),
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.deleteStudyNote(note) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Hapus Catatan",
                                                tint = Color.Red.copy(alpha = 0.7f),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Dynamic Action Buttons at the bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when {
                        downloadState == null -> {
                            Button(
                                onClick = { viewModel.startDownload(video) },
                                colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = "Unduh")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Unduh Kajian", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                        downloadState.status == "DOWNLOADING" -> {
                            Button(
                                onClick = {},
                                enabled = false,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = downloadState.downloadProgress / 100f,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = ArtisanGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Unduh: ${downloadState.downloadProgress}%", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = ArtisanGreen)
                            }
                        }
                        downloadState.status == "COMPLETED" -> {
                            Button(
                                onClick = { playOffline = !playOffline },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (playOffline) ArtisanLimeHighlight else ArtisanGreen
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Icon(
                                    imageVector = if (playOffline) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = "Putar",
                                    tint = if (playOffline) ArtisanDarkContrast else Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (playOffline) "Matikan Offline" else "Putar Offline",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (playOffline) ArtisanDarkContrast else Color.White
                                )
                            }
                        }
                        else -> {
                            Button(
                                onClick = { viewModel.startDownload(video) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Retry")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Gagal (Coba Lagi)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = "Eksternal")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Buka YouTube", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorStateView(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = "Eror",
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            color = Color.Gray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRetry) {
            Text("Coba Lagi", color = ArtisanGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyStateView(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = "Kosong",
            tint = ArtisanGreen.copy(alpha = 0.4f),
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAiScreen(viewModel: YouTubeViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isChatLoading.collectAsState()
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to the latest message whenever message list updates
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Chat Header with reset button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ArtisanDarkContrast.copy(alpha = 0.05f))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isLoading) ArtisanLimeHighlight else ArtisanGreen)
                )
                Text(
                    text = "Bimbingan Rohani Ustadz AI",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanGreen,
                    fontFamily = FontFamily.Serif
                )
            }
            TextButton(
                onClick = { viewModel.resetChat() },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red.copy(alpha = 0.7f)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Bersihkan Chat",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Message List Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { message ->
                    ChatBubbleItem(message = message)
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 40.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_ponpes_logo),
                                contentDescription = "Ustadz AI",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                            )
                            Card(
                                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = ArtisanGreen.copy(alpha = 0.04f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        color = ArtisanGreen,
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Text(
                                        text = "Ustadz AI sedang menjawab...",
                                        fontSize = 12.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = ArtisanMutedText,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Suggestions and Input Bar Panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(6.dp))
            
            // Suggestion chips (only show when no message typing wait is active)
            if (!isLoading) {
                val suggestions = listOf(
                    "Apa itu Tazkiyatun Nafs?",
                    "Bagaimana melatih ikhlas?",
                    "Keutamaan ilmu Tauhid",
                    "Profil Ponpes Jabaru"
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(suggestions) { item ->
                        SuggestionChip(
                            onClick = {
                                viewModel.sendChatMessage(item)
                            },
                            label = { Text(item, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                labelColor = ArtisanGreen,
                                containerColor = ArtisanGreen.copy(alpha = 0.06f)
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                borderColor = ArtisanGreen.copy(alpha = 0.15f),
                                enabled = true
                            )
                        )
                    }
                }
            }

            // Text Input Box Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Tanyakan bimbingan rohani di sini...", fontSize = 13.sp, fontFamily = FontFamily.Serif) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ArtisanGreen.copy(alpha = 0.04f),
                        unfocusedContainerColor = ArtisanDarkContrast.copy(alpha = 0.02f),
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = ArtisanGreen,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 4,
                    trailingIcon = {
                        if (textInput.isNotEmpty()) {
                            IconButton(onClick = { textInput = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Hapus teks",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                )

                FloatingActionButton(
                    onClick = {
                        if (textInput.isNotBlank() && !isLoading) {
                            viewModel.sendChatMessage(textInput.trim())
                            textInput = ""
                        }
                    },
                    containerColor = ArtisanGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Kirim",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(id = R.drawable.ic_ponpes_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(end = 6.dp, top = 2.dp)
                    .size(24.dp)
                    .clip(CircleShape)
            )
        }

        val bubbleShape = if (isUser) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp)
        } else {
            RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
        }

        val containerColor = if (isUser) {
            ArtisanGreen
        } else {
            ArtisanGreen.copy(alpha = 0.06f)
        }

        val contentColor = if (isUser) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Card(
            shape = bubbleShape,
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 13.5.sp,
                    lineHeight = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

