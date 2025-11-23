package com.mobiverse.MobiVerse;

import android.content.Context;
import android.telephony.TelephonyManager;

public class CallManager {

    private Context context;
    private TelephonyManager telephonyManager;

    public CallManager(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void answerCall() {
        // Logic to answer an incoming call (requires specific permissions and implementation)
        System.out.println("Answering call...");
    }

    public void endCall() {
        // Logic to end the current call
        System.out.println("Ending call...");
    }

    public boolean isCallActive() {
        // Check if a call is currently active
        return telephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE;
    }
}
