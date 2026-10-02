package com.example.engine

import com.example.data.model.GameAttemptEntity
import com.example.data.model.GameCatalog
import com.example.data.model.GameCategory
import com.example.data.model.PerformanceTrend
import com.example.i18n.StringsProvider

object ScoringEngine {

    data class ScoreCalculationResult(
        val totalQuestions: Int,
        val correctAnswers: Int,
        val incorrectAnswers: Int,
        val accuracyPercent: Int,
        val avgResponseTimeMs: Long,
        val calculatedScore: Int
    )

    fun calculate(
        totalQuestions: Int,
        correctAnswers: Int,
        responseTimesMs: List<Long>
    ): ScoreCalculationResult {
        val safeTotal = if (totalQuestions <= 0) 1 else totalQuestions
        val correct = correctAnswers.coerceIn(0, safeTotal)
        val incorrect = safeTotal - correct
        val accuracyPercent = ((correct.toFloat() / safeTotal.toFloat()) * 100).toInt()

        val avgTime = if (responseTimesMs.isNotEmpty()) {
            responseTimesMs.average().toLong().coerceAtLeast(500L)
        } else {
            3000L
        }

        // Dementia-appropriate balanced scoring:
        // Heavily weights accuracy (80 points max)
        // Completion reward (15 points)
        // Gentle pacing bonus (up to 5 points without rushing the user)
        val accuracyPart = (accuracyPercent * 0.80f).toInt()
        val completionPart = 15
        val pacingBonus = if (avgTime in 1500..8000) 5 else 2
        val finalScore = (accuracyPart + completionPart + pacingBonus).coerceIn(10, 100)

        return ScoreCalculationResult(
            totalQuestions = safeTotal,
            correctAnswers = correct,
            incorrectAnswers = incorrect,
            accuracyPercent = accuracyPercent,
            avgResponseTimeMs = avgTime,
            calculatedScore = finalScore
        )
    }
}

object AdaptiveEngine {

    data class RecommendationResult(
        val recommendedLevel: Int,
        val recommendationText: String,
        val shouldAdvance: Boolean,
        val recommendationKey: String = ""
    )

    data class RecommendedGameInfo(
        val gameId: String,
        val level: Int,
        val gameTitleKey: String,
        val gameSubtitleKey: String,
        val category: GameCategory,
        val iconEmoji: String,
        val reasonText: String,
        val cycleIndex: Int = 1,
        val totalGamesInCycle: Int = 8
    )

    val standardSequence = listOf(
        "day_date",
        "memory_match",
        "find_target",
        "simple_pattern",
        "place_time",
        "remember_objects",
        "sequence_attention",
        "everyday_choice"
    )

    fun getRecommendedGame(
        attempts: List<GameAttemptEntity>,
        excludeGameId: String? = null
    ): RecommendedGameInfo {
        val attemptsByGame = attempts.groupBy { it.gameId }

        // Helper to check if a game has been passed at or above a given level
        fun hasCompletedLevel(gameId: String, lvl: Int): Boolean {
            val list = attemptsByGame[gameId] ?: return false
            return list.any { it.level >= lvl && it.accuracyPercent >= 50 }
        }

        // 1. Determine active progression phase:
        // Level 1: until all 8 games have been completed at Level 1
        // Level 2: after all 8 completed Level 1, until all 8 completed Level 2
        // Level 3: mastery / advanced
        val allL1Completed = standardSequence.all { hasCompletedLevel(it, 1) }
        val allL2Completed = allL1Completed && standardSequence.all { hasCompletedLevel(it, 2) }

        val activePhaseLevel = when {
            !allL1Completed -> 1
            !allL2Completed -> 2
            else -> 3
        }

        // 2. Identify the most recent attempt across all games to advance sequence continuously
        val latestAttempt = attempts.maxByOrNull { it.timestamp }
        val lastPlayedIndex = if (latestAttempt != null) {
            standardSequence.indexOf(latestAttempt.gameId).takeIf { it >= 0 } ?: -1
        } else {
            -1
        }

        // Start search after the last played game in circular order
        val startIndex = if (lastPlayedIndex >= 0) (lastPlayedIndex + 1) % standardSequence.size else 0
        val circularOrder = (0 until standardSequence.size).map { offset ->
            standardSequence[(startIndex + offset) % standardSequence.size]
        }

        // Filter out excludeGameId (used when finishing a game to guarantee a different next game)
        val availableGames = if (excludeGameId != null && circularOrder.size > 1) {
            circularOrder.filter { it != excludeGameId }
        } else {
            circularOrder
        }

        // 3. In the active phase, pick the first game in circular sequence that hasn't completed this phase level
        val chosenGameId = availableGames.firstOrNull { gameId ->
            !hasCompletedLevel(gameId, activePhaseLevel)
        } ?: availableGames.first()

        // 4. Calculate gradual level for chosen game
        val chosenHistory = attemptsByGame[chosenGameId] ?: emptyList()
        val latestForGame = chosenHistory.maxByOrNull { it.timestamp }

        val targetLevel = when {
            activePhaseLevel == 1 -> 1
            activePhaseLevel == 2 -> {
                if (latestForGame != null && latestForGame.level == 1 && latestForGame.accuracyPercent < 50) 1 else 2
            }
            else -> {
                if (latestForGame != null && latestForGame.level < 3 && latestForGame.accuracyPercent < 60) {
                    latestForGame.level.coerceAtLeast(1)
                } else {
                    3
                }
            }
        }

        val gameDef = GameCatalog.getById(chosenGameId) ?: GameCatalog.games.first()
        val cycleIndex = standardSequence.indexOf(chosenGameId) + 1

        val reason = when (activePhaseLevel) {
            1 -> "Exploring Level 1 ($cycleIndex of 8 games in initial foundation cycle)."
            2 -> "Advancing to Level 2 ($cycleIndex of 8 games in cognitive challenge cycle)."
            else -> "Mastery Level 3 ($cycleIndex of 8 games for sustained sharp focus)."
        }

        return RecommendedGameInfo(
            gameId = gameDef.id,
            level = targetLevel,
            gameTitleKey = gameDef.titleKey,
            gameSubtitleKey = gameDef.subtitleKey,
            category = gameDef.category,
            iconEmoji = gameDef.iconEmoji,
            reasonText = reason,
            cycleIndex = cycleIndex,
            totalGamesInCycle = standardSequence.size
        )
    }

    fun calculateGradualLevel(attemptsForGame: List<GameAttemptEntity>): Int {
        if (attemptsForGame.isEmpty()) return 1
        val latest = attemptsForGame.maxByOrNull { it.timestamp } ?: return 1
        return when {
            latest.accuracyPercent >= 80 -> {
                (latest.level + 1).coerceAtMost(3)
            }
            latest.accuracyPercent in 50..79 -> {
                latest.level.coerceIn(1, 3)
            }
            else -> {
                (latest.level - 1).coerceAtLeast(1)
            }
        }
    }

    fun getRecommendation(
        currentLevel: Int,
        accuracyPercent: Int,
        avgResponseTimeMs: Long
    ): RecommendationResult {
        return when {
            accuracyPercent >= 80 -> {
                if (currentLevel < 3) {
                    val nextLevel = currentLevel + 1
                    RecommendationResult(
                        recommendedLevel = nextLevel,
                        recommendationText = "You're doing very well! Ready for Level $nextLevel.",
                        shouldAdvance = true,
                        recommendationKey = "rec_ready_next_level"
                    )
                } else {
                    RecommendationResult(
                        recommendedLevel = 3,
                        recommendationText = "Wonderful mastery! You have completed all levels.",
                        shouldAdvance = false,
                        recommendationKey = "rec_mastery_complete"
                    )
                }
            }
            accuracyPercent in 50..79 -> {
                RecommendationResult(
                    recommendedLevel = currentLevel,
                    recommendationText = "Good progress! Let's practice Level $currentLevel again to build confidence.",
                    shouldAdvance = false,
                    recommendationKey = "rec_practice_current"
                )
            }
            else -> {
                val gentleLevel = (currentLevel - 1).coerceAtLeast(1)
                RecommendationResult(
                    recommendedLevel = gentleLevel,
                    recommendationText = "That's completely okay. Let's take our time and practice with simpler steps.",
                    shouldAdvance = false,
                    recommendationKey = "rec_gentle_steps"
                )
            }
        }
    }

    fun getLocalizedRecommendation(
        recommendationMsgOrKey: String,
        languageCode: String,
        targetLevel: Int = 1
    ): String {
        return when {
            recommendationMsgOrKey == "rec_ready_next_level" ||
            recommendationMsgOrKey.contains("Ready for Level", ignoreCase = true) ||
            recommendationMsgOrKey.contains("very well", ignoreCase = true) -> {
                val lvl = Regex("\\d+").find(recommendationMsgOrKey)?.value?.toIntOrNull() ?: targetLevel
                StringsProvider.get("rec_ready_next_level", languageCode, lvl)
            }
            recommendationMsgOrKey == "rec_mastery_complete" ||
            recommendationMsgOrKey.contains("mastery", ignoreCase = true) ||
            recommendationMsgOrKey.contains("completed all levels", ignoreCase = true) -> {
                StringsProvider.get("rec_mastery_complete", languageCode)
            }
            recommendationMsgOrKey == "rec_practice_current" ||
            recommendationMsgOrKey.contains("practice", ignoreCase = true) ||
            recommendationMsgOrKey.contains("confidence", ignoreCase = true) -> {
                val lvl = Regex("\\d+").find(recommendationMsgOrKey)?.value?.toIntOrNull() ?: targetLevel
                StringsProvider.get("rec_practice_current", languageCode, lvl)
            }
            recommendationMsgOrKey == "rec_gentle_steps" ||
            recommendationMsgOrKey.contains("completely okay", ignoreCase = true) ||
            recommendationMsgOrKey.contains("simpler steps", ignoreCase = true) -> {
                StringsProvider.get("rec_gentle_steps", languageCode)
            }
            else -> {
                val direct = StringsProvider.get(recommendationMsgOrKey, languageCode)
                if (direct != recommendationMsgOrKey) direct else recommendationMsgOrKey
            }
        }
    }

    data class CaregiverTrendSummary(
        val trend: PerformanceTrend,
        val trendTitle: String,
        val trendDescription: String,
        val baselineAccuracy: Int,
        val recentAccuracy: Int,
        val baselineAvgTimeSec: Float,
        val recentAvgTimeSec: Float
    )

    fun evaluatePerformanceTrend(attempts: List<GameAttemptEntity>): CaregiverTrendSummary {
        if (attempts.size < 2) {
            return CaregiverTrendSummary(
                trend = PerformanceTrend.STABLE,
                trendTitle = "Establishing Baseline",
                trendDescription = "More game sessions will establish a clearer baseline trend.",
                baselineAccuracy = if (attempts.isNotEmpty()) attempts[0].accuracyPercent else 75,
                recentAccuracy = if (attempts.isNotEmpty()) attempts[0].accuracyPercent else 75,
                baselineAvgTimeSec = 4.0f,
                recentAvgTimeSec = 4.0f
            )
        }

        // Recent attempts (up to 3) vs Earlier attempts
        val recent = attempts.take(3)
        val baseline = attempts.drop(3).ifEmpty { attempts.takeLast(attempts.size / 2 + 1) }

        val recentAcc = recent.map { it.accuracyPercent }.average().toInt()
        val baselineAcc = baseline.map { it.accuracyPercent }.average().toInt()

        val recentTime = (recent.map { it.avgResponseTimeMs }.average() / 1000.0).toFloat()
        val baselineTime = (baseline.map { it.avgResponseTimeMs }.average() / 1000.0).toFloat()

        val diff = recentAcc - baselineAcc

        return when {
            diff >= 8 -> CaregiverTrendSummary(
                trend = PerformanceTrend.IMPROVING,
                trendTitle = "Improving Trend",
                trendDescription = "Recent game accuracy is $diff% higher than earlier baseline. Patient shows engaged focus.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
            diff <= -15 -> CaregiverTrendSummary(
                trend = PerformanceTrend.NEEDS_ATTENTION,
                trendTitle = "Noticeable Variation",
                trendDescription = "Recent accuracy is lower than earlier baseline. Patient may be fatigued, distracted, or experiencing temporary fluctuations.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
            else -> CaregiverTrendSummary(
                trend = PerformanceTrend.STABLE,
                trendTitle = "Stable Engagement",
                trendDescription = "Activity accuracy and timing are consistent with established baseline records.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
        }
    }
}
