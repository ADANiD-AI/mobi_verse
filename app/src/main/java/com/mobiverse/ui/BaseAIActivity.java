package com.mobiverse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiverse.ai.MobiverseAIHelper;
import java.util.ArrayList;

// یہ وہ کلاس ہے جو ہر ماڈیول کا "باپ" (Parent) ہوگی
public abstract class BaseAIActivity extends AppCompatActivity {

    protected MobiverseAIHelper aiHelper;
    private static final int VOICE_REQUEST_CODE = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aiHelper = MobiverseAIHelper.getInstance(this);
    }

    // ہم `setContentView` کو اوور رائیڈ کر رہے ہیں تاکہ ہر اسکرین پر بٹن خود بخود لگ جائے
    @Override
    public void setContentView(View view) {
        // 1. ایک فریم بنائیں جو پوری اسکرین کو گھیرے
        FrameLayout rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // 2. اصل ایپ کا مواد (اسٹاک، ہوم وغیرہ) شامل کریں
        rootLayout.addView(view);

        // 3. AI Agent بٹن (Floating Action Button) تیار کریں
        FloatingActionButton aiButton = new FloatingActionButton(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        
        // بٹن کی پوزیشن: نیچے دائیں جانب (Bottom Right)
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(32, 32, 32, 200); // تھوڑا اوپر تاکہ نیویگیشن بار کے اوپر رہے
        aiButton.setLayoutParams(params);
        
        // مائک کا آئیکن یا روبوٹ کا آئیکن
        aiButton.setImageResource(android.R.drawable.ic_btn_speak_now);
        
        // 4. کلک کرنے پر سننے کا عمل
        aiButton.setOnClickListener(v -> startListening());

        // 5. بٹن کو اسکرین پر شامل کریں
        rootLayout.addView(aiButton);

        // فائنل ویو سیٹ کریں
        super.setContentView(rootLayout);
    }

    private void startListening() {
        // گوگل کا ڈیفالٹ اسپیچ ریکوگنائزر (یہاں ہم اپنا کسٹم UI بھی بنا سکتے ہیں)
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Mobiverse AI سن رہا ہے...");
        startActivityForResult(intent, VOICE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                // یوزر کی آواز ٹیکسٹ میں مل گئی، اب دماغ کو بھیجیں
                String spokenText = result.get(0);
                aiHelper.processVoiceCommand(spokenText);
            }
        }
    }
}
