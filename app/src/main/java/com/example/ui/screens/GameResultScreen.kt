package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.data.model.GameCatalog
import com.example.engine.AdaptiveEngine
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.StatCard
import java.util.Locale

@Composable
fun GameResultScreen(
    gameId: String,
    level: Int,
    score: Int,
    accuracy: Int,
    avgTimeMs: Long,
    correct: Int,
    total: Int,
    recommendation: String,
    nextGameId: String = "memory_match",
    nextLevel: Int = 1,
    nextGameTitleKey: String = "game_memory_match",
    currentLanguage: String,
    fontSizeScale: FontSizeScale,
    highContrast: Boolean,
    largeButtons: Boolean,
    voiceEnabled: Boolean,
    onSpeak: (String) -> Unit,
    onPlayNextRecommended: (gameId: String, level: Int) -> Unit = { _, _ -> },
    onReplay: () -> Unit,
    onBackToGames: () -> Unit
) {
    val gameDef = GameCatalog.getById(gameId)
    val gameTitle = gameDef?.let { StringsProvider.get(it.titleKey, currentLanguage) } ?: "Game"
    val nextGameDef = GameCatalog.getById(nextGameId)
    val nextGameTitle = nextGameDef?.let { StringsProvider.get(it.titleKey, currentLanguage) } ?: StringsProvider.get(nextGameTitleKey, currentLanguage)
    val scrollState = rememberScrollState()

    val congratulation = if (accuracy >= 80) {
        StringsProvider.get("msg_good_job", currentLanguage)
    } else {
        StringsProvider.get("msg_improving", currentLanguage)
    }

    val localizedRecommendation = AdaptiveEngine.getLocalizedRecommendation(
        recommendationMsgOrKey = recommendation,
        languageCode = currentLanguage,
        targetLevel = (level + 1).coerceAtMost(3)
    )

    LaunchedEffect(currentLanguage) {
        val speechText = "$congratulation ${StringsProvider.get("label_accuracy", currentLanguage)}: $accuracy%. $localizedRecommendation"
        onSpeak(speechText)
    }

    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

    Scaffold(
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Celebration Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantAttentionBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (accuracy >= 80) "🌟" else "🌱",
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = congratulation,
                fontSize = (24f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = StringsProvider.get("level_complete_subtitle", currentLanguage, gameTitle, level),
                fontSize = (14f * fontSizeScale.scale).sp,
                color = com.example.ui.theme.VibrantTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stat Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = StringsProvider.get("label_score", currentLanguage),
                    value = "$score / 100",
                    modifier = Modifier.weight(1f),
                    emoji = "🏆",
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                StatCard(
                    title = StringsProvider.get("label_accuracy", currentLanguage),
                    value = "$accuracy%",
                    modifier = Modifier.weight(1f),
                    subtitle = StringsProvider.get("correct_of_total", currentLanguage, correct, total),
                    emoji = "🎯",
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val avgSec = String.format(Locale.US, "%.1fs", avgTimeMs / 1000.0)
            StatCard(
                title = StringsProvider.get("label_response_time", currentLanguage),
                value = avgSec,
                modifier = Modifier.fillMaxWidth(),
                subtitle = StringsProvider.get("pace_comfortable", currentLanguage),
                emoji = "⏱️",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Adaptive Recommendation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = StringsProvider.get("label_adaptive_recommendation", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp,
                            color = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary
                        )
                        IconButton(
                            onClick = { onSpeak(localizedRecommendation) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = StringsProvider.get("btn_listen", currentLanguage),
                                tint = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = localizedRecommendation,
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Actions
            // 1. Play Next Recommended Activity (Primary action!)
            Card(
                onClick = { onPlayNextRecommended(nextGameId, nextLevel) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_play_next_recommended")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = if (largeButtons) 18.dp else 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (highContrast) Color.Black else Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = nextGameDef?.iconEmoji ?: "▶",
                                    fontSize = 22.sp
                                )
                            }
                        }

                        Column {
                            Text(
                                text = StringsProvider.get("btn_play_next_recommended", currentLanguage),
                                fontSize = (16f * fontSizeScale.scale).sp,
                                fontWeight = FontWeight.Bold,
                                color = if (highContrast) Color.Black else Color.White
                            )
                            Text(
                                text = "$nextGameTitle • ${StringsProvider.get("label_level", currentLanguage).replace(":", "").trim()} $nextLevel",
                                fontSize = (13f * fontSizeScale.scale).sp,
                                color = if (highContrast) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Play Next",
                        tint = if (highContrast) Color.Black else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Play Again
            AccessibleButton(
                text = "${StringsProvider.get("btn_replay", currentLanguage)} (${StringsProvider.get("label_level", currentLanguage)} $level)",
                onClick = onReplay,
                icon = Icons.Default.Replay,
                isOutlined = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtonMode = largeButtons,
                testTag = "btn_game_replay"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Back to Home
            AccessibleButton(
                text = StringsProvider.get("btn_home", currentLanguage),
                onClick = onBackToGames,
                icon = Icons.Default.Home,
                isOutlined = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtonMode = largeButtons,
                testTag = "btn_game_finish_home"
            )

            Spacer(modifier = Modifier.height(20.dp))

            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
