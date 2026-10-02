package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.i18n.StringsProvider
import com.example.ui.navigation.PatientTab
import com.example.ui.navigation.Screen
import com.example.ui.screens.CaregiverAuthScreen
import com.example.ui.screens.CaregiverMainScreen
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.GameResultScreen
import com.example.ui.screens.PatientAuthScreen
import com.example.ui.screens.PatientMainScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = viewModel()
            SmritiAppRoot(appViewModel)
        }
    }
}

@Composable
fun SmritiAppRoot(viewModel: AppViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val linkedPatient by viewModel.linkedPatient.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val highContrast by viewModel.highContrast.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val largeButtons by viewModel.largeButtons.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val voiceEnabled by viewModel.voiceEnabled.collectAsState()
    val patientTab by viewModel.patientTab.collectAsState()
    val caregiverTab by viewModel.caregiverTab.collectAsState()
    val selectedCategory by viewModel.selectedGameCategory.collectAsState()

    val attempts by viewModel.gameAttempts.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val dailyRoutine by viewModel.dailyRoutine.collectAsState()
    val allPatients by viewModel.allPatients.collectAsState()
    val linkedPatients by viewModel.linkedPatients.collectAsState()

    MyApplicationTheme(darkTheme = isDarkMode || highContrast) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                is Screen.Splash -> {
                    SplashScreen(
                        currentLanguage = currentLanguage,
                        fontSizeScale = fontSizeScale,
                        highContrast = highContrast,
                        onContinue = { viewModel.navigateTo(Screen.Welcome) }
                    )
                }

                is Screen.Welcome -> {
                    WelcomeScreen(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        voiceEnabled = voiceEnabled,
                        onToggleVoice = { viewModel.toggleVoiceEnabled(!voiceEnabled) },
                        highContrast = highContrast,
                        onToggleHighContrast = { viewModel.toggleHighContrast(!highContrast) },
                        fontSizeScale = fontSizeScale,
                        onCycleFontSize = {
                            val nextScale = when (fontSizeScale) {
                                com.example.data.model.FontSizeScale.STANDARD -> com.example.data.model.FontSizeScale.LARGE
                                com.example.data.model.FontSizeScale.LARGE -> com.example.data.model.FontSizeScale.EXTRA_LARGE
                                com.example.data.model.FontSizeScale.EXTRA_LARGE -> com.example.data.model.FontSizeScale.STANDARD
                            }
                            viewModel.setFontSize(nextScale)
                        },
                        onSpeakContext = {
                            viewModel.speakText(StringsProvider.get("welcome_spoken_intro", currentLanguage))
                        },
                        onPlayGames = { viewModel.quickLoginDemoPatient() },
                        onPatientLogin = { viewModel.navigateTo(Screen.PatientLogin) },
                        onSignUp = { viewModel.navigateTo(Screen.PatientSignUp) },
                        onCaregiverLogin = { viewModel.navigateTo(Screen.CaregiverLogin) },
                        onQuickDemoPatient = { viewModel.quickLoginDemoPatient() },
                        onQuickDemoCaregiver = { viewModel.quickLoginDemoCaregiver() }
                    )
                }

                is Screen.PatientLogin, is Screen.PatientSignUp -> {
                    val isSignUp = screen is Screen.PatientSignUp
                    PatientAuthScreen(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        voiceEnabled = voiceEnabled,
                        onToggleVoice = { viewModel.toggleVoiceEnabled(!voiceEnabled) },
                        highContrast = highContrast,
                        onToggleHighContrast = { viewModel.toggleHighContrast(!highContrast) },
                        fontSizeScale = fontSizeScale,
                        onCycleFontSize = {
                            val nextScale = when (fontSizeScale) {
                                com.example.data.model.FontSizeScale.STANDARD -> com.example.data.model.FontSizeScale.LARGE
                                com.example.data.model.FontSizeScale.LARGE -> com.example.data.model.FontSizeScale.EXTRA_LARGE
                                com.example.data.model.FontSizeScale.EXTRA_LARGE -> com.example.data.model.FontSizeScale.STANDARD
                            }
                            viewModel.setFontSize(nextScale)
                        },
                        onSpeakContext = {
                            viewModel.speakText(
                                if (isSignUp) StringsProvider.get("speak_signup_page", currentLanguage)
                                else StringsProvider.get("speak_login_page", currentLanguage)
                            )
                        },
                        initialTabIsSignUp = isSignUp,
                        onLogin = { email, pass, onError ->
                            viewModel.loginPatient(email, pass, onError)
                        },
                        onSignUp = { name, age, gender, language, state, email, pass, onSuccess, onError ->
                            viewModel.signupPatient(name, age, gender, language, state, email, pass, onSuccess, onError)
                        },
                        onQuickDemo = { viewModel.quickLoginDemoPatient() },
                        onNavigateBack = { viewModel.navigateTo(Screen.Welcome) },
                        onSwitchToCaregiver = { viewModel.navigateTo(Screen.CaregiverLogin) }
                    )
                }

                is Screen.CaregiverLogin -> {
                    CaregiverAuthScreen(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        voiceEnabled = voiceEnabled,
                        onToggleVoice = { viewModel.toggleVoiceEnabled(!voiceEnabled) },
                        highContrast = highContrast,
                        onToggleHighContrast = { viewModel.toggleHighContrast(!highContrast) },
                        fontSizeScale = fontSizeScale,
                        onCycleFontSize = {
                            val nextScale = when (fontSizeScale) {
                                com.example.data.model.FontSizeScale.STANDARD -> com.example.data.model.FontSizeScale.LARGE
                                com.example.data.model.FontSizeScale.LARGE -> com.example.data.model.FontSizeScale.EXTRA_LARGE
                                com.example.data.model.FontSizeScale.EXTRA_LARGE -> com.example.data.model.FontSizeScale.STANDARD
                            }
                            viewModel.setFontSize(nextScale)
                        },
                        onSpeakContext = {
                            viewModel.speakText(StringsProvider.get("caregiver_signin_desc", currentLanguage))
                        },
                        onLogin = { email, pass, onError ->
                            viewModel.loginCaregiver(email, pass, onError)
                        },
                        onSignUp = { name, email, pass, state, patientCode, relationship, onSuccess, onError ->
                            viewModel.signUpCaregiver(name, email, pass, state, patientCode, relationship, onSuccess, onError)
                        },
                        onQuickDemo = { viewModel.quickLoginDemoCaregiver() },
                        onNavigateBack = { viewModel.navigateTo(Screen.Welcome) },
                        onSwitchToPatient = { viewModel.navigateTo(Screen.PatientLogin) }
                    )
                }

                is Screen.PatientMain -> {
                    PatientMainScreen(
                        currentTab = patientTab,
                        onTabSelected = { viewModel.setPatientTab(it) },
                        user = currentUser,
                        attempts = attempts,
                        routineItems = dailyRoutine,
                        onToggleRoutine = { id, done -> viewModel.toggleRoutineItem(id, done) },
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.setGameCategory(it) },
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        highContrast = highContrast,
                        onToggleHighContrast = { viewModel.toggleHighContrast(!highContrast) },
                        fontSizeScale = fontSizeScale,
                        onSetFontSize = { viewModel.setFontSize(it) },
                        onCycleFontSize = {
                            val nextScale = when (fontSizeScale) {
                                com.example.data.model.FontSizeScale.STANDARD -> com.example.data.model.FontSizeScale.LARGE
                                com.example.data.model.FontSizeScale.LARGE -> com.example.data.model.FontSizeScale.EXTRA_LARGE
                                com.example.data.model.FontSizeScale.EXTRA_LARGE -> com.example.data.model.FontSizeScale.STANDARD
                            }
                            viewModel.setFontSize(nextScale)
                        },
                        largeButtons = largeButtons,
                        onToggleLargeButtons = { viewModel.toggleLargeButtons(it) },
                        voiceEnabled = voiceEnabled,
                        onToggleVoice = { viewModel.toggleVoiceEnabled(!voiceEnabled) },
                        onSpeak = { viewModel.speakText(it) },
                        onLaunchGame = { gameId, level ->
                            viewModel.navigateTo(Screen.GamePlay(gameId, level))
                        },
                        onLogout = { viewModel.logout() }
                    )
                }

                is Screen.CaregiverMain -> {
                    CaregiverMainScreen(
                        currentTab = caregiverTab,
                        onTabSelected = { viewModel.setCaregiverTab(it) },
                        caregiverUser = currentUser,
                        linkedPatient = linkedPatient,
                        linkedPatients = linkedPatients,
                        allPatients = allPatients,
                        onSelectPatient = { viewModel.selectMonitoredPatient(it) },
                        attempts = attempts,
                        reminders = reminders,
                        onToggleReminder = { id, done -> viewModel.toggleReminder(id, done) },
                        onAddReminder = { title, desc, cat, time -> viewModel.addReminder(title, desc, cat, time) },
                        onDeleteReminder = { viewModel.deleteReminder(it) },
                        routineItems = dailyRoutine,
                        onToggleRoutine = { id, done -> viewModel.toggleRoutineItem(id, done) },
                        onAddRoutine = { period, title, desc, time -> viewModel.addRoutineItem(period, title, desc, time) },
                        onDeleteRoutine = { viewModel.deleteRoutineItem(it) },
                        onLinkPatientByCode = { code, cb -> viewModel.linkPatientByCode(code, cb) },
                        onUnlinkPatient = { patientId, cb -> viewModel.unlinkPatient(patientId, cb) },
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        highContrast = highContrast,
                        onToggleHighContrast = { viewModel.toggleHighContrast(!highContrast) },
                        fontSizeScale = fontSizeScale,
                        onCycleFontSize = {
                            val nextScale = when (fontSizeScale) {
                                com.example.data.model.FontSizeScale.STANDARD -> com.example.data.model.FontSizeScale.LARGE
                                com.example.data.model.FontSizeScale.LARGE -> com.example.data.model.FontSizeScale.EXTRA_LARGE
                                com.example.data.model.FontSizeScale.EXTRA_LARGE -> com.example.data.model.FontSizeScale.STANDARD
                            }
                            viewModel.setFontSize(nextScale)
                        },
                        voiceEnabled = voiceEnabled,
                        onToggleVoice = { viewModel.toggleVoiceEnabled(!voiceEnabled) },
                        onSpeak = { viewModel.speakText(it) },
                        onLogout = { viewModel.logout() }
                    )
                }

                is Screen.GamePlay -> {
                    GamePlayScreen(
                        gameId = screen.gameId,
                        level = screen.level,
                        currentLanguage = currentLanguage,
                        fontSizeScale = fontSizeScale,
                        highContrast = highContrast,
                        largeButtons = largeButtons,
                        voiceEnabled = voiceEnabled,
                        onSpeak = { viewModel.speakText(it) },
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        onBackToGames = {
                            viewModel.setPatientTab(PatientTab.GAMES)
                            viewModel.navigateTo(Screen.PatientMain())
                        },
                        onCompleteGame = { total, correct, times ->
                            viewModel.recordGameAttempt(
                                gameId = screen.gameId,
                                category = com.example.data.model.GameCatalog.getById(screen.gameId)?.category?.name?.lowercase() ?: "general",
                                level = screen.level,
                                totalQuestions = total,
                                correctAnswers = correct,
                                responseTimesMs = times
                            ) { attempt ->
                                val nextRec = com.example.engine.AdaptiveEngine.getRecommendedGame(
                                    attempts = attempts + attempt,
                                    excludeGameId = screen.gameId
                                )
                                viewModel.navigateTo(
                                    Screen.GameResult(
                                        gameId = screen.gameId,
                                        level = screen.level,
                                        score = attempt.totalScore,
                                        accuracy = attempt.accuracyPercent,
                                        avgTimeMs = attempt.avgResponseTimeMs,
                                        correct = attempt.correctAnswers,
                                        total = attempt.totalQuestions,
                                        recommendation = attempt.recommendationMessage,
                                        nextGameId = nextRec.gameId,
                                        nextLevel = nextRec.level,
                                        nextGameTitleKey = nextRec.gameTitleKey
                                    )
                                )
                            }
                        }
                    )
                }

                is Screen.GameResult -> {
                    GameResultScreen(
                        gameId = screen.gameId,
                        level = screen.level,
                        score = screen.score,
                        accuracy = screen.accuracy,
                        avgTimeMs = screen.avgTimeMs,
                        correct = screen.correct,
                        total = screen.total,
                        recommendation = screen.recommendation,
                        nextGameId = screen.nextGameId,
                        nextLevel = screen.nextLevel,
                        nextGameTitleKey = screen.nextGameTitleKey,
                        currentLanguage = currentLanguage,
                        fontSizeScale = fontSizeScale,
                        highContrast = highContrast,
                        largeButtons = largeButtons,
                        voiceEnabled = voiceEnabled,
                        onSpeak = { viewModel.speakText(it) },
                        onPlayNextRecommended = { nextId, nextLvl ->
                            viewModel.navigateTo(Screen.GamePlay(nextId, nextLvl))
                        },
                        onReplay = {
                            viewModel.navigateTo(Screen.GamePlay(screen.gameId, screen.level))
                        },
                        onBackToGames = {
                            viewModel.setPatientTab(PatientTab.HOME)
                            viewModel.navigateTo(Screen.PatientMain())
                        }
                    )
                }
            }
        }
    }
}

