package com.zai.choicescreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {
    
    private RadioGroup radioGroup;
    private Button btnConfirm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        
        radioGroup = findViewById(R.id.radioGroup);
        btnConfirm = findViewById(R.id.btnConfirm);
        
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String choice = selectedRadioButton.getText().toString();
                    Toast.makeText(ChoiceActivity.this, "Selected: " + choice, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChoiceActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}