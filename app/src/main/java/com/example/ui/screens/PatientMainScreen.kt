package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.FontSizeScale
import com.example.data.model.GameAttemptEntity
import com.example.data.model.GameCatalog
import com.example.data.model.GameCategory
import com.example.data.model.GameDefinition
import com.example.data.model.RoutineItemEntity
import com.example.data.model.UserEntity
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccuracyTrendChart
import com.example.ui.components.CategoryBreakdownChart
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.ResponseTimeChart
import com.example.ui.components.StatCard
import com.example.ui.navigation.PatientTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PatientMainScreen(
    currentTab: PatientTab,
    onTabSelected: (PatientTab) -> Unit,
    user: UserEntity?,
    attempts: List<GameAttemptEntity>,
    routineItems: List<RoutineItemEntity>,
    onToggleRoutine: (Long, Boolean) -> Unit,
    selectedCategory: GameCategory,
    onSelectCategory: (GameCategory) -> Unit,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onSetFontSize: (FontSizeScale) -> Unit,
    onCycleFontSize: () -> Unit,
    largeButtons: Boolean,
    onToggleLargeButtons: (Boolean) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onSpeak: (String) -> Unit,
    onLaunchGame: (gameId: String, level: Int) -> Unit,
    onLogout: () -> Unit
) {
    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val navBg = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantNavBg
    val navSelectedColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantNavIndicatorText
    val navIndicatorColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantNavIndicator
    val navUnselectedColor = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantTextMuted

    val recommendedGame = remember(attempts) {
        com.example.engine.AdaptiveEngine.getRecommendedGame(attempts)
    }

    Scaffold(
        topBar = {
            AccessibilityHeader(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected,
                voiceEnabled = voiceEnabled,
                onToggleVoice = onToggleVoice,
                highContrast = highContrast,
                onToggleHighContrast = onToggleHighContrast,
                fontSizeScale = fontSizeScale,
                onCycleFontSize = onCycleFontSize,
                onSpeakCurrentContext = {
                    val tabName = when (currentTab) {
                        PatientTab.HOME -> "You are on the Home screen. Here are today's activities."
                        PatientTab.GAMES -> "You are on the Games library. Choose any cognitive game."
                        PatientTab.PROGRESS -> "You are on your Progress dashboard."
                        PatientTab.PROFILE -> "You are on your Profile and Settings."
                    }
                    onSpeak(tabName)
                },
                title = when (currentTab) {
                    PatientTab.HOME -> StringsProvider.get("nav_home", currentLanguage)
                    PatientTab.GAMES -> StringsProvider.get("nav_games", currentLanguage)
                    PatientTab.PROGRESS -> StringsProvider.get("nav_progress", currentLanguage)
                    PatientTab.PROFILE -> StringsProvider.get("nav_profile", currentLanguage)
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = navBg,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == PatientTab.HOME,
                    onClick = { onTabSelected(PatientTab.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = {
                        Text(
                            StringsProvider.get("nav_home", currentLanguage),
                            fontWeight = if (currentTab == PatientTab.HOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavIndicatorText,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_home")
                )
                NavigationBarItem(
                    selected = currentTab == PatientTab.GAMES,
                    onClick = { onTabSelected(PatientTab.GAMES) },
                    icon = { Icon(Icons.Default.Extension, contentDescription = "Games") },
                    label = {
                        Text(
                            StringsProvider.get("nav_games", currentLanguage),
                            fontWeight = if (currentTab == PatientTab.GAMES) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavIndicatorText,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_games")
                )
                NavigationBarItem(
                    selected = currentTab == PatientTab.PROGRESS,
                    onClick = { onTabSelected(PatientTab.PROGRESS) },
                    icon = { Icon(Icons.Default.ShowChart, contentDescription = "Progress") },
                    label = {
                        Text(
                            StringsProvider.get("nav_progress", currentLanguage),
                            fontWeight = if (currentTab == PatientTab.PROGRESS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavIndicatorText,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_progress")
                )
                NavigationBarItem(
                    selected = currentTab == PatientTab.PROFILE,
                    onClick = { onTabSelected(PatientTab.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = {
                        Text(
                            StringsProvider.get("nav_profile", currentLanguage),
                            fontWeight = if (currentTab == PatientTab.PROFILE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavIndicatorText,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_profile")
                )
            }
        },
        containerColor = bg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                PatientTab.HOME -> PatientHomeContent(
                    user = user,
                    routineItems = routineItems,
                    onToggleRoutine = onToggleRoutine,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    largeButtons = largeButtons,
                    onSpeak = onSpeak,
                    recommendedGame = recommendedGame,
                    onLaunchGame = onLaunchGame,
                    onSelectCategory = onSelectCategory,
                    onNavigateToTab = onTabSelected
                )
                PatientTab.GAMES -> PatientGamesContent(
                    selectedCategory = selectedCategory,
                    onSelectCategory = onSelectCategory,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    largeButtons = largeButtons,
                    onLaunchGame = onLaunchGame
                )
                PatientTab.PROGRESS -> PatientProgressContent(
                    attempts = attempts,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                PatientTab.PROFILE -> PatientProfileContent(
                    user = user,
                    currentLanguage = currentLanguage,
                    onLanguageSelected = onLanguageSelected,
                    highContrast = highContrast,
                    onToggleHighContrast = onToggleHighContrast,
                    fontSizeScale = fontSizeScale,
                    onSetFontSize = onSetFontSize,
                    largeButtons = largeButtons,
                    onToggleLargeButtons = onToggleLargeButtons,
                    voiceEnabled = voiceEnabled,
                    onToggleVoice = onToggleVoice,
                    onSpeak = onSpeak,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun PatientHomeContent(
    user: UserEntity?,
    routineItems: List<RoutineItemEntity>,
    onToggleRoutine: (Long, Boolean) -> Unit,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    largeButtons: Boolean,
    onSpeak: (String) -> Unit,
    recommendedGame: com.example.engine.AdaptiveEngine.RecommendedGameInfo,
    onLaunchGame: (gameId: String, level: Int) -> Unit,
    onSelectCategory: (GameCategory) -> Unit,
    onNavigateToTab: (PatientTab) -> Unit
) {
    val scrollState = rememberScrollState()
    val todayFormatted = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH).format(Date())
    val patientName = user?.fullName ?: "Arun"
    val initials = patientName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "AD" }
    val stateLocation = user?.locationState ?: "Assam, North East India"
    val recGameDef = com.example.data.model.GameCatalog.getById(recommendedGame.gameId)
    val recGameTitle = recGameDef?.let { StringsProvider.get(it.titleKey, currentLanguage) } ?: StringsProvider.get(recommendedGame.gameTitleKey, currentLanguage)
    val recGameDesc = recGameDef?.let { StringsProvider.get(it.subtitleKey, currentLanguage) } ?: StringsProvider.get(recommendedGame.gameSubtitleKey, currentLanguage)

    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val nextRoutine = routineItems.firstOrNull { !it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(18.dp)
    ) {
        // Patient Header with Avatar, Mode label & TTS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantOnBlueContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PATIENT MODE",
                        fontSize = (11f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantTextSecondary
                    )
                    Text(
                        text = "Namaskar, $patientName",
                        fontSize = (22f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                }
            }

            IconButton(
                onClick = { onSpeak("Namaskar $patientName. Welcome to your daily cognitive exercises. Today is $todayFormatted.") },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantActionBtnBg)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read home greeting aloud",
                    tint = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Vibrant Palette Hero Card: Today's Recommended Activity
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantBlueContainer
            ),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, com.example.ui.theme.VibrantBluePrimary.copy(alpha = 0.15f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (highContrast) Color(0xFF334155) else Color.White.copy(alpha = 0.85f)
                    ) {
                        Text(
                            text = "${StringsProvider.get("label_recommended", currentLanguage).uppercase()} • ACTIVITY ${recommendedGame.cycleIndex} OF ${recommendedGame.totalGamesInCycle}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp,
                            color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (highContrast) Color(0xFF1E3A8A) else com.example.ui.theme.VibrantBluePrimary
                    ) {
                        Text(
                            text = "${StringsProvider.get("label_level", currentLanguage).replace(":", "").trim()} ${recommendedGame.level}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "${recommendedGame.iconEmoji} $recGameTitle",
                    fontSize = (22f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (highContrast) Color.White else com.example.ui.theme.VibrantOnBlueContainer
                )

                Text(
                    text = recGameDesc,
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = if (highContrast) Color(0xFFCBD5E1) else com.example.ui.theme.VibrantTextSecondary
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (highContrast) Color(0xFF0F172A) else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡 ${recommendedGame.reasonText}",
                            fontSize = (12f * fontSizeScale.scale).sp,
                            fontWeight = FontWeight.Medium,
                            color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantTextSecondary
                        )
                    }
                }

                androidx.compose.material3.Button(
                    onClick = { onLaunchGame(recommendedGame.gameId, recommendedGame.level) },
                    shape = RoundedCornerShape(18.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                        contentColor = if (highContrast) Color.Black else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = if (largeButtons) 56.dp else 50.dp)
                        .testTag("btn_home_play_recommended")
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${StringsProvider.get("btn_play_game", currentLanguage)}: $recGameTitle",
                            fontWeight = FontWeight.Bold,
                            fontSize = (16f * fontSizeScale.scale).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "→",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // "Choose a Category" Section
        Text(
            text = StringsProvider.get("label_choose_category", currentLanguage),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 2x2 Category Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Orientation
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantOrientationBg,
                border = if (highContrast) BorderStroke(1.5.dp, Color.White) else BorderStroke(1.dp, Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onSelectCategory(GameCategory.ORIENTATION)
                        onNavigateToTab(PatientTab.GAMES)
                    }
                    .testTag("home_cat_orientation")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "📅", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = StringsProvider.get("cat_orientation", currentLanguage),
                        fontSize = (16f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.White else com.example.ui.theme.VibrantOrientationText
                    )
                    Text(
                        text = StringsProvider.get("cat_orientation_desc", currentLanguage),
                        fontSize = 11.sp,
                        color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantOrientationText.copy(alpha = 0.7f)
                    )
                }
            }

            // Memory
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantMemoryBg,
                border = if (highContrast) BorderStroke(1.5.dp, Color.White) else BorderStroke(1.dp, Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onSelectCategory(GameCategory.MEMORY)
                        onNavigateToTab(PatientTab.GAMES)
                    }
                    .testTag("home_cat_memory")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🧩", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = StringsProvider.get("cat_memory", currentLanguage),
                        fontSize = (16f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.White else com.example.ui.theme.VibrantMemoryText
                    )
                    Text(
                        text = StringsProvider.get("cat_memory_desc", currentLanguage),
                        fontSize = 11.sp,
                        color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantMemoryText.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Attention
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantAttentionBg,
                border = if (highContrast) BorderStroke(1.5.dp, Color.White) else BorderStroke(1.dp, Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onSelectCategory(GameCategory.ATTENTION)
                        onNavigateToTab(PatientTab.GAMES)
                    }
                    .testTag("home_cat_attention")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🔍", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = StringsProvider.get("cat_attention", currentLanguage),
                        fontSize = (16f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.White else com.example.ui.theme.VibrantAttentionText
                    )
                    Text(
                        text = StringsProvider.get("cat_attention_desc", currentLanguage),
                        fontSize = 11.sp,
                        color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantAttentionText.copy(alpha = 0.7f)
                    )
                }
            }

            // Reasoning
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantReasoningBg,
                border = if (highContrast) BorderStroke(1.5.dp, Color.White) else BorderStroke(1.dp, Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onSelectCategory(GameCategory.REASONING)
                        onNavigateToTab(PatientTab.GAMES)
                    }
                    .testTag("home_cat_reasoning")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "⚖️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = StringsProvider.get("cat_reasoning", currentLanguage),
                        fontSize = (16f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.White else com.example.ui.theme.VibrantReasoningText
                    )
                    Text(
                        text = StringsProvider.get("cat_reasoning_desc", currentLanguage),
                        fontSize = 11.sp,
                        color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantReasoningText.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Next Reminder Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantSurfaceVariant,
            border = BorderStroke(1.dp, if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantAlertContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💧", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = StringsProvider.get("label_next_reminder", currentLanguage),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantTextSecondary
                    )
                    Text(
                        text = nextRoutine?.let { "${it.title} • ${it.timeStr}" } ?: "Drink Water • 11:30 AM",
                        fontSize = (15f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                }
                nextRoutine?.let { item ->
                    IconButton(
                        onClick = { onToggleRoutine(item.id, true) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = StringsProvider.get("action_complete_task", currentLanguage),
                            tint = com.example.ui.theme.VibrantGreenPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Today's Daily Routine Checklist
        Text(
            text = StringsProvider.get("label_daily_routine", currentLanguage),
            fontSize = (18f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (routineItems.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantSurfaceVariant,
                border = BorderStroke(1.dp, if (highContrast) Color(0xFF475569) else com.example.ui.theme.VibrantBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = StringsProvider.get("msg_no_routine", currentLanguage),
                    color = textPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                routineItems.take(5).forEach { item ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantSurfaceVariant,
                        border = if (highContrast) BorderStroke(1.dp, Color(0xFF475569)) else BorderStroke(1.dp, com.example.ui.theme.VibrantBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onToggleRoutine(item.id, !item.isCompleted) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { onToggleRoutine(item.id, it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                                    checkmarkColor = if (highContrast) Color.Black else Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = (15f * fontSizeScale.scale).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (item.isCompleted) textPrimary.copy(alpha = 0.5f) else textPrimary
                                )
                                Text(
                                    text = "${item.timeStr} • ${item.description}",
                                    fontSize = (12f * fontSizeScale.scale).sp,
                                    color = textPrimary.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Medical Disclaimer
        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}

@Composable
private fun PatientGamesContent(
    selectedCategory: GameCategory,
    onSelectCategory: (GameCategory) -> Unit,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    largeButtons: Boolean,
    onLaunchGame: (gameId: String, level: Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    val filteredGames = remember(selectedCategory) {
        if (selectedCategory == GameCategory.ALL) {
            GameCatalog.games
        } else {
            GameCatalog.games.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Cognitive Games Library",
            fontSize = (22f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Text(
            text = "Carefully crafted for gentle engagement and North East culture.",
            fontSize = (13f * fontSizeScale.scale).sp,
            color = textPrimary.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(GameCategory.values()) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) {
                        if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                    } else {
                        if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantActionBtnBg
                    },
                    border = if (highContrast && !isSelected) BorderStroke(1.dp, Color(0xFF475569)) else if (!isSelected) BorderStroke(1.dp, com.example.ui.theme.VibrantBorder) else null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectCategory(category) }
                        .testTag("filter_${category.name}")
                ) {
                    Text(
                        text = when (category) {
                            GameCategory.ALL -> StringsProvider.get("cat_all", currentLanguage)
                            GameCategory.ORIENTATION -> StringsProvider.get("cat_orientation", currentLanguage)
                            GameCategory.MEMORY -> StringsProvider.get("cat_memory", currentLanguage)
                            GameCategory.ATTENTION -> StringsProvider.get("cat_attention", currentLanguage)
                            GameCategory.REASONING -> StringsProvider.get("cat_reasoning", currentLanguage)
                        },
                        color = if (isSelected) {
                            if (highContrast) Color.Black else Color.White
                        } else {
                            if (highContrast) Color.White else com.example.ui.theme.VibrantTextSecondary
                        },
                        fontSize = (13f * fontSizeScale.scale).sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game Cards List
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            filteredGames.forEach { game ->
                GameCardItem(
                    game = game,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    largeButtons = largeButtons,
                    onPlay = { level -> onLaunchGame(game.id, level) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}

@Composable
private fun GameCardItem(
    game: GameDefinition,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    largeButtons: Boolean,
    onPlay: (level: Int) -> Unit
) {
    var selectedLevel by remember { mutableIntStateOf(1) }
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, com.example.ui.theme.VibrantBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = game.iconEmoji, fontSize = 36.sp, modifier = Modifier.padding(end = 12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = StringsProvider.get(game.titleKey, currentLanguage),
                        fontSize = (18f * fontSizeScale.scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = StringsProvider.get(game.subtitleKey, currentLanguage),
                        fontSize = (12f * fontSizeScale.scale).sp,
                        color = com.example.ui.theme.VibrantTextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Level selector buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${StringsProvider.get("label_level", currentLanguage)}:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = com.example.ui.theme.VibrantTextSecondary
                )
                (1..3).forEach { lvl ->
                    val isLvlSelected = selectedLevel == lvl
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isLvlSelected) {
                            if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                        } else {
                            if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantActionBtnBg
                        },
                        border = if (!isLvlSelected && !highContrast) BorderStroke(1.dp, com.example.ui.theme.VibrantBorder) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedLevel = lvl }
                    ) {
                        Text(
                            text = "L$lvl",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLvlSelected) {
                                if (highContrast) Color.Black else Color.White
                            } else {
                                textPrimary
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Play Button
            AccessibleButton(
                text = "${StringsProvider.get("btn_play_game", currentLanguage)} - ${StringsProvider.get(game.titleKey, currentLanguage)}",
                onClick = { onPlay(selectedLevel) },
                icon = Icons.Default.PlayArrow,
                isPrimary = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtonMode = largeButtons,
                testTag = "btn_play_${game.id}"
            )
        }
    }
}

@Composable
private fun PatientProgressContent(
    attempts: List<GameAttemptEntity>,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale
) {
    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)

    val totalGames = attempts.size
    val avgAccuracy = if (attempts.isNotEmpty()) attempts.map { it.accuracyPercent }.average().toInt() else 0
    val totalScoreAvg = if (attempts.isNotEmpty()) attempts.map { it.totalScore }.average().toInt() else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = StringsProvider.get("progress_patient_title", currentLanguage),
            fontSize = (22f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Text(
            text = StringsProvider.get("progress_patient_subtitle", currentLanguage),
            fontSize = (13f * fontSizeScale.scale).sp,
            color = textPrimary.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Summary Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = StringsProvider.get("stat_total_sessions", currentLanguage),
                value = "$totalGames",
                modifier = Modifier.weight(1f),
                emoji = "🎮",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
            StatCard(
                title = StringsProvider.get("stat_avg_accuracy", currentLanguage),
                value = "$avgAccuracy%",
                modifier = Modifier.weight(1f),
                emoji = "🎯",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Accuracy Trend Chart
        AccuracyTrendChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale,
            currentLanguage = currentLanguage
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Response Time Chart
        ResponseTimeChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale,
            currentLanguage = currentLanguage
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Breakdown
        CategoryBreakdownChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale,
            currentLanguage = currentLanguage
        )

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}

@Composable
private fun PatientProfileContent(
    user: UserEntity?,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onSetFontSize: (FontSizeScale) -> Unit,
    largeButtons: Boolean,
    onToggleLargeButtons: (Boolean) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onSpeak: (String) -> Unit = {},
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(18.dp)
    ) {
        Text(
            text = StringsProvider.get("title_profile_accessibility", currentLanguage),
            fontSize = (22f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Patient Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, com.example.ui.theme.VibrantBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = user?.fullName ?: "Arun Sharma",
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = StringsProvider.get("label_age_years", currentLanguage, user?.age ?: 68, user?.locationState ?: "Assam"),
                    fontSize = (14f * fontSizeScale.scale).sp,
                    color = com.example.ui.theme.VibrantTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))

                val clipboardManager = LocalClipboardManager.current
                val context = LocalContext.current
                val haptic = LocalHapticFeedback.current
                val codeToCopy = user?.patientCode?.ifBlank { "NER-6842" } ?: "NER-6842"
                val copyCodeAction = {
                    clipboardManager.setText(AnnotatedString(codeToCopy))
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    Toast.makeText(context, "${StringsProvider.get("msg_patient_code_copied", currentLanguage)}: $codeToCopy", Toast.LENGTH_SHORT).show()
                    onSpeak("${StringsProvider.get("msg_patient_code_copied", currentLanguage)}: $codeToCopy")
                }

                @OptIn(ExperimentalFoundationApi::class)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBlueContainer,
                    border = BorderStroke(1.5.dp, if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .combinedClickable(
                            onClick = copyCodeAction,
                            onLongClick = copyCodeAction
                        )
                        .testTag("patient_code_copy_surface")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = StringsProvider.get("label_patient_code_caregiver", currentLanguage),
                                fontSize = 12.sp,
                                color = if (highContrast) Color.White else com.example.ui.theme.VibrantOnBlueContainer
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = codeToCopy,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${StringsProvider.get("label_copy_hint", currentLanguage)})",
                                    fontSize = 11.sp,
                                    color = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantTextSecondary
                                )
                            }
                        }
                        IconButton(
                            onClick = copyCodeAction,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = StringsProvider.get("label_copy_hint", currentLanguage),
                                tint = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Accessibility Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, com.example.ui.theme.VibrantBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = StringsProvider.get("label_accessibility_preferences", currentLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = (16f * fontSizeScale.scale).sp,
                    color = textPrimary
                )

                // High Contrast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsProvider.get("setting_high_contrast", currentLanguage),
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary
                    )
                    Switch(
                        checked = highContrast,
                        onCheckedChange = { onToggleHighContrast() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                            checkedTrackColor = if (highContrast) Color(0xFF475569) else com.example.ui.theme.VibrantNavIndicator
                        )
                    )
                }

                // Large Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsProvider.get("setting_large_buttons", currentLanguage),
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary
                    )
                    Switch(
                        checked = largeButtons,
                        onCheckedChange = { onToggleLargeButtons(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                            checkedTrackColor = if (highContrast) Color(0xFF475569) else com.example.ui.theme.VibrantNavIndicator
                        )
                    )
                }

                // Voice Read-Aloud
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsProvider.get("setting_voice", currentLanguage),
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary
                    )
                    Switch(
                        checked = voiceEnabled,
                        onCheckedChange = { onToggleVoice() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                            checkedTrackColor = if (highContrast) Color(0xFF475569) else com.example.ui.theme.VibrantNavIndicator
                        )
                    )
                }

                // Font Size Selector
                Column {
                    val scaleKey = when (fontSizeScale) {
                        FontSizeScale.STANDARD -> "font_standard"
                        FontSizeScale.LARGE -> "font_large"
                        FontSizeScale.EXTRA_LARGE -> "font_extra_large"
                    }
                    Text(
                        text = StringsProvider.get("setting_font_size_label", currentLanguage, StringsProvider.get(scaleKey, currentLanguage)),
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FontSizeScale.values().forEach { scale ->
                            val isSelected = scale == fontSizeScale
                            val itemScaleKey = when (scale) {
                                FontSizeScale.STANDARD -> "font_standard"
                                FontSizeScale.LARGE -> "font_large"
                                FontSizeScale.EXTRA_LARGE -> "font_extra_large"
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) {
                                    if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                                } else {
                                    if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantActionBtnBg
                                },
                                border = if (!isSelected && !highContrast) BorderStroke(1.dp, com.example.ui.theme.VibrantBorder) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSetFontSize(scale) }
                            ) {
                                Text(
                                    text = StringsProvider.get(itemScaleKey, currentLanguage),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) {
                                        if (highContrast) Color.Black else Color.White
                                    } else {
                                        textPrimary
                                    },
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout
        AccessibleButton(
            text = StringsProvider.get("btn_sign_out", currentLanguage),
            onClick = onLogout,
            icon = Icons.Default.ExitToApp,
            isOutlined = true,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale,
            testTag = "btn_patient_logout"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}
