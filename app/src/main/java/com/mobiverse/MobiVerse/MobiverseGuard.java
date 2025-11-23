package com.mobiverse.MobiVerse;

import java.util.ArrayList;
import java.util.List;

public class MobiverseGuard {

    private List<String> scanResults;

    public MobiverseGuard() {
        this.scanResults = new ArrayList<>();
    }

    public void scanForMalware() {
        // Simulate scanning for malware
        System.out.println("Scanning for malware...");
        // Add some dummy results
        scanResults.add("No malware found.");
    }

    public void scanForPhishing() {
        // Simulate scanning for phishing
        System.out.println("Scanning for phishing...");
        // Add some dummy results
        scanResults.add("No phishing attempts found.");
    }

    public void scanForOtherThreats() {
        // Simulate scanning for other threats
        System.out.println("Scanning for other threats...");
        // Add some dummy results
        scanResults.add("No other threats found.");
    }

    public List<String> getScanResults() {
        return scanResults;
    }

    public void clearScanResults() {
        scanResults.clear();
    }
}
