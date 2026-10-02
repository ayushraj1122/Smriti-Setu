package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GameCategory
import com.example.engine.AdaptiveEngine
import com.example.engine.ScoringEngine
import com.example.i18n.StringsProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Smriti Setu", appName)
    }

    @Test
    fun `test scoring engine calculation`() {
        val result = ScoringEngine.calculate(
            totalQuestions = 4,
            correctAnswers = 4,
            responseTimesMs = listOf(2000L, 2500L, 3000L, 2200L)
        )
        assertEquals(100, result.accuracyPercent)
        assertTrue(result.calculatedScore >= 95)
    }

    @Test
    fun `test multilingual strings provider fallback`() {
        val assameseHome = StringsProvider.get("nav_home", "as")
        assertEquals("ঘৰ (Home)", assameseHome)
        val englishHome = StringsProvider.get("nav_home", "en")
        assertEquals("Home", englishHome)
    }

    @Test
    fun `test adaptive recommendation progression`() {
        val rec = AdaptiveEngine.getRecommendation(
            currentLevel = 1,
            accuracyPercent = 85,
            avgResponseTimeMs = 2500L
        )
        assertEquals(2, rec.recommendedLevel)
        assertTrue(rec.shouldAdvance)
    }

    @Test
    fun `test initial recommendation gives level 1 across unplayed games`() {
        // Empty history -> first game at level 1
        val rec1 = AdaptiveEngine.getRecommendedGame(emptyList())
        assertEquals("day_date", rec1.gameId)
        assertEquals(1, rec1.level)

        // After playing day_date -> next unplayed game at level 1
        val attempts = listOf(
            com.example.data.model.GameAttemptEntity(
                userId = 1L,
                gameId = "day_date",
                gameCategory = "orientation",
                level = 1,
                timestamp = System.currentTimeMillis(),
                totalQuestions = 3,
                correctAnswers = 3,
                incorrectAnswers = 0,
                accuracyPercent = 100,
                avgResponseTimeMs = 3000L,
                totalScore = 95,
                recommendationMessage = "Great"
            )
        )
        val rec2 = AdaptiveEngine.getRecommendedGame(attempts)
        assertEquals("memory_match", rec2.gameId)
        assertEquals(1, rec2.level)
    }

    @Test
    fun `test recommendation rotates and advances level when all games played`() {
        val now = System.currentTimeMillis()
        val allPlayedAttempts = AdaptiveEngine.standardSequence.mapIndexed { index, gameId ->
            com.example.data.model.GameAttemptEntity(
                userId = 1L,
                gameId = gameId,
                gameCategory = "general",
                level = 1,
                timestamp = now - ((10 - index) * 100000L), // oldest is day_date
                totalQuestions = 3,
                correctAnswers = 3,
                incorrectAnswers = 0,
                accuracyPercent = 90,
                avgResponseTimeMs = 3000L,
                totalScore = 90,
                recommendationMessage = "Good"
            )
        }

        // Since day_date was least recently played and had 90% accuracy, it rotates to day_date at Level 2!
        val nextRec = AdaptiveEngine.getRecommendedGame(allPlayedAttempts)
        assertEquals("day_date", nextRec.gameId)
        assertEquals(2, nextRec.level)
    }

    @Test
    fun `test new localized string keys exist`() {
        val heroTitle = StringsProvider.get("home_hero_title", "en")
        assertEquals("Today's Brain Activity", heroTitle)
        val recLabel = StringsProvider.get("label_recommended", "en")
        assertEquals("Recommended", recLabel)
        val playNext = StringsProvider.get("btn_play_next_recommended", "en")
        assertEquals("Play Next Activity", playNext)
    }
}

