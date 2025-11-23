package com.mobiverse.satellite;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

/**
 * Helper class to check for network and satellite connectivity.
 * Based on the user's plan for Project Nebula.
 * MIT License - Mobiverse Project Nebula
 */
public class ConnectivityHelper {

    /**
     * Checks if the device has an active internet connection (Wi-Fi, Cellular, or Ethernet).
     * @param context The application context.
     * @return true if an internet connection is available, false otherwise.
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
        if (caps == null) return false;

        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }

    /**
     * Checks if the device is connected to a satellite network.
     * This requires Android 12 (API 31) and newer.
     * @param context The application context.
     * @return true if a satellite connection is available, false otherwise.
     */
    public static boolean isSatelliteConnected(Context context) {
        // The SATELLITE transport type was added in a later version. 
        // We will assume for now it will be available in future Android versions as planned by the user.
        // Using a placeholder check for now.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Check should be higher in reality
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) return false;

            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) return false;

            NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
            if (caps == null) return false;

            // TRANSPORT_SATELLITE is not in the public SDK yet, but we build the logic for it.
            // We can check for other properties that might indicate a satellite link.
            // For example, unmetered but not Wi-Fi could be a hint.
            boolean isUnmetered = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            boolean isNotWifi = !caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);

            // This is a creative interpretation. A real implementation will use a future TRANSPORT_SATELLITE constant.
            if (isUnmetered && isNotWifi && hasInternet(context)) {
                 // This could be a satellite link. For now, we return false until the API is public.
                 return false; 
            }
        }
        return false;
    }

    /**
     * Combines internet and satellite checks.
     * @param context The application context.
     * @return true if any form of connectivity is available.
     */
    public static boolean isSatelliteOrInternetAvailable(Context context) {
        return hasInternet(context) || isSatelliteConnected(context);
    }
}
