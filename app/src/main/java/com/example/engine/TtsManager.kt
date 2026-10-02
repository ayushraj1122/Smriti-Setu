package com.example.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isMuted = false
    private var speechRate = 0.9f
    private var pendingSpeech: Pair<String, String>? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("TtsManager", "Failed to initialize TTS engine", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setSpeechRate(speechRate)
            pendingSpeech?.let { (text, lang) ->
                pendingSpeech = null
                speak(text, lang)
            }
        }
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            stop()
        }
    }

    fun setSpeechRate(rate: Float) {
        speechRate = rate
        tts?.setSpeechRate(rate)
    }

    fun speak(text: String, langCode: String = "en") {
        if (isMuted || text.isBlank()) return
        if (!isInitialized) {
            pendingSpeech = Pair(text, langCode)
            return
        }

        try {
            val locale = resolveLocale(langCode)
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback for Indian regional languages
                val fallbackLocale = when (langCode.lowercase()) {
                    "as", "bn" -> Locale("bn", "IN")
                    "hi", "mni", "nag" -> Locale("hi", "IN")
                    else -> Locale.ENGLISH
                }
                tts?.setLanguage(fallbackLocale)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "smriti_tts_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.w("TtsManager", "TTS speech failed", e)
        }
    }

    private fun resolveLocale(langCode: String): Locale {
        return when (langCode.lowercase()) {
            "hi" -> Locale("hi", "IN")
            "bn" -> Locale("bn", "IN")
            "as" -> {
                val asLoc = Locale("as", "IN")
                val avail = tts?.isLanguageAvailable(asLoc) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (avail >= TextToSpeech.LANG_AVAILABLE) asLoc else Locale("bn", "IN")
            }
            "mni" -> {
                val mniLoc = Locale("mni", "IN")
                val avail = tts?.isLanguageAvailable(mniLoc) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (avail >= TextToSpeech.LANG_AVAILABLE) mniLoc else Locale("hi", "IN")
            }
            "kha", "lus" -> {
                val loc = Locale(langCode.lowercase(), "IN")
                val avail = tts?.isLanguageAvailable(loc) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (avail >= TextToSpeech.LANG_AVAILABLE) loc else Locale.ENGLISH
            }
            "nag" -> Locale("hi", "IN")
            else -> Locale.ENGLISH
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
