package com.example.mistercalculator.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechRecognizerManager(
    private val context: Context,
) {
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(
        context
    )
    var isListening = false
        private set

    private val recognizerIntent = Intent(
        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
    ).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "en-US"
        )

        putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            true
        )
    }

    private var onResult: ((String) -> Unit)? = null

    fun setOnResultListener(
        listener: (String) -> Unit
    ) {
        onResult = listener
    }

    fun startListening() {
        isListening = true
        speechRecognizer.startListening(
            recognizerIntent
        )
    }

    fun stopListening() {
        isListening = false
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }

    init {
        speechRecognizer.setRecognitionListener(
            object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                }

                override fun onBeginningOfSpeech() {
                }

                override fun onRmsChanged(rmsdB: Float) {
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                }

                override fun onEndOfSpeech() {
                }

                override fun onError(error: Int) {
                }

                override fun onPartialResults(partialResults: Bundle?) {
                }

                override fun onEvent(eventType: Int, params: Bundle?) {
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )

                    val spokenText = matches?.getOrNull(0)

                    if (spokenText != null) {
                        onResult?.invoke(
                            spokenText
                        )
                        isListening = false
                    }
                }
            }
        )
    }
}

