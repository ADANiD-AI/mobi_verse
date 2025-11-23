package com.mobiverse.aikeyboard;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AITextProcessor {
    private static final String TAG = "AITextProcessor";
    private Context context;
    
    // Option 1: Use local Ollama server (self-hosted)
    private static final String LOCAL_API = "http://localhost:11434/api/generate";
    
    // Option 2: Use Hugging Face Inference API (Free tier)
    private static final String HF_API = "https://api-inference.huggingface.co/models/";
    private static final String HF_MODEL = "facebook/opt-350m"; // Free small model
    
    public AITextProcessor(Context context) {
        this.context = context;
    }

    /**
     * Correct grammar/spelling mistakes in text
     */
    public String correctText(String rawText) {
        try {
            // Simple rule-based corrections for common Urdu/English mix issues
            String corrected = rawText
                .replaceAll("\s+", " ") // Multiple spaces
                .trim();
            
            // Check if device is online and use AI
            if (isNetworkAvailable()) {
                corrected = callHuggingFaceAPI(
                    "Correct this text: " + rawText,
                    50
                );
            } else {
                // Offline: use local rules
                corrected = applyLocalCorrections(rawText);
            }
            
            return corrected;
            
        } catch (Exception e) {
            Log.e(TAG, "Error correcting text", e);
            return rawText;
        }
    }

    /**
     * Get smart suggestion for next word/sentence
     */
    public String getSuggestion(String context) {
        try {
            if (!isNetworkAvailable()) {
                return getLocalSuggestion(context);
            }
            
            String prompt = "Complete this sentence naturally: " + context;
            return callHuggingFaceAPI(prompt, 30);
            
        } catch (Exception e) {
            Log.e(TAG, "Error getting suggestion", e);
            return null;
        }
    }

    /**
     * Call Hugging Face API (Free, no API key needed for public models)
     */
    private String callHuggingFaceAPI(String prompt, int maxTokens) {
        try {
            URL url = new URL(HF_API + HF_MODEL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Create request body
            JSONObject json = new JSONObject();
            json.put("inputs", prompt);
            json.put("parameters", new JSONObject()
                .put("max_length", maxTokens)
                .put("temperature", 0.7)
            );
            
            // Send request
            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();
            
            // Read response
            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            
            // Parse response
            JSONObject result = new JSONObject(response.toString());
            if (result.has("generated_text")) {
                return result.getString("generated_text")
                    .replace(prompt, "").trim();
            }
            
            return null;
            
        } catch (Exception e) {
            Log.e(TAG, "HF API call failed", e);
            return null;
        }
    }

    /**
     * Local rule-based corrections (offline fallback)
     */
    private String applyLocalCorrections(String text) {
        // Common Urdu/Roman Urdu corrections
        return text
            .replaceAll("\bkr\b", "kar")
            .replaceAll("\bhy\b", "hai")
            .replaceAll("\bko\b", "ko")
            .replaceAll("\bmee\b", "main")
            .replaceAll("\bapp?\b", "aap")
            // Add more rules based on common mistakes
            .replaceAll("([.!?])([a-zA-Z])", "$1 $2") // Space after punctuation
            .replaceAll("\s+([.!?,])", "$1"); // No space before punctuation
    }

    /**
     * Local suggestion engine (simple n-gram based)
     */
    private String getLocalSuggestion(String context) {
        String[] words = context.toLowerCase().split("\s+");
        if (words.length == 0) return null;
        
        String lastWord = words[words.length - 1];
        
        // Simple prediction based on common patterns
        if (lastWord.equals("main")) return "ne";
        if (lastWord.equals("aap")) return "kaise";
        if (lastWord.equals("kya")) return "hai";
        if (lastWord.equals("mujhe")) return "chahiye";
        
        return null;
    }

    private boolean isNetworkAvailable() {
        try {
            android.net.ConnectivityManager cm = 
                (android.net.ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public void cleanup() {
        // Cleanup resources if needed
    }
}