package com.kavyakanaja.app.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Manages Android TextToSpeech engine lifecycle and playback.
 *
 * Features:
 * - Speaks poem text or AI explanations
 * - Switches between English and Kannada voices
 * - Handles initialization, errors, and lifecycle cleanup
 */
class TTSManager(context: Context) {

    private val TAG = "TTSManager"

    // TTS engine state
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    // Callback for UI to react to TTS events
    var onInitialized: ((Boolean) -> Unit)? = null
    var onSpeakingStarted: (() -> Unit)? = null
    var onSpeakingFinished: (() -> Unit)? = null

    init {
        // Initialize TTS engine on creation
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                Log.d(TAG, "TTS initialized successfully")
                onInitialized?.invoke(true)
            } else {
                isInitialized = false
                Log.e(TAG, "TTS initialization failed with status: $status")
                onInitialized?.invoke(false)
            }
        }
    }

    /**
     * Speaks the given text using the language setting.
     * @param text Text to speak
     * @param language "en" or "kn"
     */
    fun speak(text: String, language: String) {
        if (!isInitialized || tts == null) {
            Log.w(TAG, "TTS not initialized yet")
            return
        }

        // Select locale based on language preference
        val locale = when (language) {
            Constants.LANG_KANNADA -> Locale("kn", "IN")  // Kannada (India)
            else -> Locale.ENGLISH
        }

        // Set the language and check for support
        val result = tts!!.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to English if Kannada TTS is not supported on device
            Log.w(TAG, "Language not supported: $language. Falling back to English.")
            tts!!.setLanguage(Locale.ENGLISH)
        }

        // Set speech rate and pitch for natural poetry reading
        tts!!.setSpeechRate(0.85f)  // Slightly slower for clear pronunciation
        tts!!.setPitch(1.0f)

        // Set utterance progress listener for UI callbacks
        tts!!.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                onSpeakingStarted?.invoke()
            }
            override fun onDone(utteranceId: String?) {
                onSpeakingFinished?.invoke()
            }
            override fun onError(utteranceId: String?) {
                Log.e(TAG, "TTS error for utterance: $utteranceId")
                onSpeakingFinished?.invoke()
            }
        })

        // Speak — QUEUE_FLUSH stops any current speech and starts new
        tts!!.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "kavya_kanaja_utterance"
        )
    }

    /** Returns true if TTS is currently speaking */
    fun isSpeaking(): Boolean = tts?.isSpeaking ?: false

    /** Stops any ongoing speech */
    fun stop() {
        tts?.stop()
        onSpeakingFinished?.invoke()
    }

    /**
     * IMPORTANT: Call this in ViewModel's onCleared() or Activity's onDestroy()
     * to properly release the TTS engine and avoid memory leaks.
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        Log.d(TAG, "TTS shut down")
    }
}