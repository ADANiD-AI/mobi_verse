package com.zai.choicescreen;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class CryptoTracker {

    private Context context;
    private FirebaseFirestore db;

    public CryptoTracker(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    public void getLivePrices() {
        db.collection("livePrices").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w("CryptoTracker", "Listen failed.", e);
                return;
            }
            // Update UI with live prices
        });
    }
}
