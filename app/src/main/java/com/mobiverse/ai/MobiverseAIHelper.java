package com.mobiverse.ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import java.util.Locale;

public class MobiverseAIHelper {
    private static MobiverseAIHelper instance;
    private TextToSpeech tts;
    private Context context;

    // Singleton Pattern: پوری ایپ کے لیے ایک ہی دماغ
    public static MobiverseAIHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MobiverseAIHelper(context);
        }
        return instance;
    }

    private MobiverseAIHelper(Context context) {
        this.context = context;
        initializeTTS();
    }

    // ٹیکسٹ ٹو اسپیچ (بولنے کی صلاحیت)
    private void initializeTTS() {
        tts = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.ENGLISH); // یا اردو سیٹ کریں
            }
        });
    }

    public void speak(String text) {
        if (tts != null) {
            // QUEUE_ADD کا مطلب ہے کہ پچھلی بات کاٹنے کے بجائے اگلی بات لائن میں لگے
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    // یہ ہے وہ "وائس اینالائزر" جو غلطیوں کو درست کرے گا
    public void processVoiceCommand(String rawText) {
        String command = rawText.toLowerCase();
        
        // 1. Voice Correction / Understanding Imperfect Speech
        // اگر یوزر "Kal" یا "Kol" بولے تو اسے "Call" سمجھو
        if (command.contains("kol") || command.contains("kal")) {
            command = command.replace("kol", "call").replace("kal", "call");
        }
        
        // 2. Command Execution
        if (command.startsWith("call")) {
            String name = command.replace("call", "").trim();
            speak("Calling " + name);
            makePhoneCall(name); // فنکشن نیچے ہے
            
        } else if (command.startsWith("open")) {
            String appName = command.replace("open", "").trim();
            speak("Opening " + appName);
            // openApp(appName); Logic here
            
        } else if (command.contains("read screen") || command.contains("read message")) {
            // اسکرین ریڈنگ کا فنکشن
            speak("Reading your last message: Hello, meeting is at 5 PM.");
            
        } else {
            // عام چیٹ (یہاں آپ OpenAI API لگا سکتے ہیں)
            speak("I heard: " + rawText);
        }
    }

    private void makePhoneCall(String name) {
        // یہ یوزر کو ڈائلر پر لے جائے گا
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:")); // اصل ایپ میں یہاں نام سے نمبر تلاش ہوگا
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    
    // اگر اسکرین آف ہو تب بھی بولنے کے لیے سروس درکار ہوگی (Foreground Service)
}
