package com.mobiverse.MobiVerse;

import java.util.ArrayList;
import java.util.List;

public class MobiverseVPN {

    private List<String> servers;
    private String selectedServer;
    private boolean isConnected;

    public MobiverseVPN() {
        this.servers = new ArrayList<>();
        // Add some default servers
        this.servers.add("us-east-1");
        this.servers.add("us-west-1");
        this.servers.add("eu-central-1");
        this.selectedServer = this.servers.get(0);
        this.isConnected = false;
    }

    public List<String> getServers() {
        return servers;
    }

    public void selectServer(String server) {
        if (servers.contains(server)) {
            this.selectedServer = server;
        }
    }

    public String getSelectedServer() {
        return selectedServer;
    }

    public void connect() {
        // Simulate connecting to the VPN
        System.out.println("Connecting to " + selectedServer);
        this.isConnected = true;
        System.out.println("Connected to " + selectedServer);
    }

    public void disconnect() {
        // Simulate disconnecting from the VPN
        System.out.println("Disconnecting from " + selectedServer);
        this.isConnected = false;
        System.out.println("Disconnected from " + selectedServer);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
