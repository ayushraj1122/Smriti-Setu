package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner

@Composable
fun WelcomeScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakContext: () -> Unit,
    onPlayGames: () -> Unit,
    onPatientLogin: () -> Unit,
    onSignUp: () -> Unit,
    onCaregiverLogin: () -> Unit,
    onQuickDemoPatient: () -> Unit,
    onQuickDemoCaregiver: () -> Unit
) {
    val scrollState = rememberScrollState()
    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

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
                onSpeakCurrentContext = onSpeakContext,
                title = StringsProvider.get("app_name", currentLanguage)
            )
        },
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Logo & Title
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantBlueContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_smriti),
                    contentDescription = "Smriti App Icon",
                    modifier = Modifier
                        .size(74.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = StringsProvider.get("app_name", currentLanguage),
                fontSize = (28f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = StringsProvider.get("app_tagline", currentLanguage),
                fontSize = (15f * fontSizeScale.scale).sp,
                color = if (highContrast) Color(0xFFCBD5E1) else com.example.ui.theme.VibrantTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Big Action: Play Games
            AccessibleButton(
                text = StringsProvider.get("btn_play_games", currentLanguage),
                onClick = onPlayGames,
                icon = Icons.Default.PlayArrow,
                isPrimary = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_welcome_play_games"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Demo Buttons (Instant access for judges & evaluators)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBorder
                ),
                shadowElevation = if (highContrast) 0.dp else 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⚡",
                            fontSize = 13.sp
                        )
                        Text(
                            text = StringsProvider.get("demo_evaluator_title", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (12f * fontSizeScale.scale).sp,
                            color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Demo Patient
                        Surface(
                            onClick = onQuickDemoPatient,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_quick_demo_patient"),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            color = if (highContrast) Color(0xFF0F172A) else Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (highContrast) 2.dp else 1.5.dp,
                                color = if (highContrast) Color(0xFF38BDF8) else Color(0xFFBFDBFE)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (highContrast) Color(0xFF1E293B) else Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "👤", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = StringsProvider.get("demo_patient_title", currentLanguage),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (13f * fontSizeScale.scale).sp,
                                    color = if (highContrast) Color.White else Color(0xFF1E3A8A),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = StringsProvider.get("demo_patient_desc", currentLanguage),
                                    fontSize = (11f * fontSizeScale.scale).sp,
                                    color = if (highContrast) Color(0xFF94A3B8) else Color(0xFF2563EB),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }

                        // Demo Caregiver
                        Surface(
                            onClick = onQuickDemoCaregiver,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_quick_demo_caregiver"),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            color = if (highContrast) Color(0xFF0F172A) else Color(0xFFF0FDF4),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (highContrast) 2.dp else 1.5.dp,
                                color = if (highContrast) Color(0xFF4ADE80) else Color(0xFFBBF7D0)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (highContrast) Color(0xFF1E293B) else Color(0xFFDCFCE7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🩺", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = StringsProvider.get("demo_caregiver_title", currentLanguage),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (13f * fontSizeScale.scale).sp,
                                    color = if (highContrast) Color.White else Color(0xFF14532D),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = StringsProvider.get("demo_caregiver_desc", currentLanguage),
                                    fontSize = (11f * fontSizeScale.scale).sp,
                                    color = if (highContrast) Color(0xFF94A3B8) else Color(0xFF16A34A),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Patient Login & Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AccessibleButton(
                    text = StringsProvider.get("btn_patient_login", currentLanguage),
                    onClick = onPatientLogin,
                    icon = Icons.Default.Person,
                    isOutlined = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    modifier = Modifier.weight(1f),
                    testTag = "btn_welcome_patient_login"
                )

                AccessibleButton(
                    text = StringsProvider.get("btn_sign_up", currentLanguage),
                    onClick = onSignUp,
                    isOutlined = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    modifier = Modifier.weight(1f),
                    testTag = "btn_welcome_signup"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Caregiver Login
            AccessibleButton(
                text = StringsProvider.get("btn_caregiver_login", currentLanguage),
                onClick = onCaregiverLogin,
                icon = Icons.Default.SupervisorAccount,
                isPrimary = false,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_welcome_caregiver_login"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Medical Disclaimer
            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
