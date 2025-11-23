package com.mobiverse.aikeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

public class AIKeyboardService extends InputMethodService 
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard englishKeyboard;
    private Keyboard urduKeyboard;
    private boolean isUrduLayout = false;

    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;
    private AITextProcessor aiProcessor;

    // Custom key codes
    private static final int KEYCODE_VOICE = -10;
    private static final int KEYCODE_AI = -11;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(
            R.layout.keyboard_view, null);
        
        // Load both keyboards
        englishKeyboard = new Keyboard(this, R.xml.keys_layout);
        urduKeyboard = new Keyboard(this, R.xml.keys_layout_urdu);
        
        // Set initial keyboard
        keyboardView.setKeyboard(englishKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        
        // Initialize AI processor
        aiProcessor = new AITextProcessor(this);
        
        // Initialize speech recognizer
        initializeSpeechRecognizer();
        
        return keyboardView;
    }

    private void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String rawText = matches.get(0);
                    processVoiceInput(rawText);
                }
                isListening = false;
            }

            @Override
            public void onError(int error) {
                Toast.makeText(AIKeyboardService.this, 
                    "Voice recognition error", Toast.LENGTH_SHORT).show();
                isListening = false;
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startVoiceRecognition() {
        if (isListening) return;
        
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur-PK"); // Urdu
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        
        speechRecognizer.startListening(intent);
        isListening = true;
        Toast.makeText(this, "Listening... Speak now", Toast.LENGTH_SHORT).show();
    }

    private void processVoiceInput(String rawText) {
        // Show raw text first
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.commitText(rawText, 1);
        }
        
        // Process with AI in background
        new Thread(() -> {
            String correctedText = aiProcessor.correctText(rawText);
            runOnUiThread(() -> {
                if (ic != null && !correctedText.equals(rawText)) {
                    // Replace with corrected text
                    ic.deleteSurroundingText(rawText.length(), 0);
                    ic.commitText(correctedText, 1);
                    Toast.makeText(this, "✨ AI Corrected", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void runOnUiThread(Runnable action) {
        keyboardView.post(action);
    }

    private void triggerAIAssist() {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        
        // Get current text
        CharSequence textBefore = ic.getTextBeforeCursor(100, 0);
        if (textBefore == null || textBefore.length() == 0) {
            Toast.makeText(this, "Type something first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String currentText = textBefore.toString();
        Toast.makeText(this, "🤖 AI is thinking...", Toast.LENGTH_SHORT).show();
        
        new Thread(() -> {
            String suggestion = aiProcessor.getSuggestion(currentText);
            runOnUiThread(() -> {
                if (ic != null && suggestion != null) {
                    ic.commitText(" " + suggestion, 1);
                }
            });
        }).start();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN, 
                    android.view.KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if (isUrduLayout) {
                    keyboardView.setKeyboard(englishKeyboard);
                } else {
                    keyboardView.setKeyboard(urduKeyboard);
                }
                isUrduLayout = !isUrduLayout;
                break;
            case KEYCODE_VOICE:
                startVoiceRecognition();
                break;
            case KEYCODE_AI:
                triggerAIAssist();
                break;
            default:
                char code = (char) primaryCode;
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override public void onPress(int primaryCode) {
        // Haptic feedback
        keyboardView.performHapticFeedback(
            android.view.HapticFeedbackConstants.KEYBOARD_TAP);
    }

    @Override public void onRelease(int primaryCode) {}
    @Override public void onText(CharSequence text) {}
    @Override public void swipeLeft() {}
    @Override public void swipeRight() {}
    @Override public void swipeDown() {}
    @Override public void swipeUp() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (aiProcessor != null) {
            aiProcessor.cleanup();
        }
    }
}