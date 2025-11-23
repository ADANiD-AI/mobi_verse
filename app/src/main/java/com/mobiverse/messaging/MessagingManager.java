package com.mobiverse.messaging;

import android.content.Context;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * MessagingManager آف لائن میسجنگ کی تمام فعالیت کو سنبھالتا ہے۔
 * یہ اب صارف کی Mobiverse ID (Firebase Display Name) کا استعمال کرتا ہے۔
 */
public class MessagingManager {

    // ... (existing code)

    private final FirebaseAuth auth;

    // ... (existing code)

    private MessagingManager(Context context) {
        this.context = context.getApplicationContext();
        this.connectionsClient = Nearby.getConnectionsClient(context);
        this.auth = FirebaseAuth.getInstance(); // Firebase Auth کا انسٹنس حاصل کریں
    }

    // ... (existing code)

    private String getUserNickname() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            return user.getDisplayName(); // صارف کا ڈسپلے نام استعمال کریں
        } else if (user != null) {
            return user.getUid(); // فال بیک کے طور پر UID استعمال کریں
        }
        return "AnonymousUser"; // اگر صارف لاگ ان نہیں ہے
    }

    public void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        connectionsClient.startAdvertising(getUserNickname(), serviceId, connectionLifecycleCallback, advertisingOptions);
    }

    public void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        connectionsClient.startDiscovery(serviceId, endpointDiscoveryCallback, discoveryOptions);
    }

    // ... (rest of the existing code)
}
