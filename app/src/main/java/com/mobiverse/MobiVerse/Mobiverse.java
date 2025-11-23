package com.zai.mobiverse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MobiVerse extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Choose an option:");
    }
}
