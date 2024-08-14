package com.teama.server.dto.connection;

public class ConnectionRequest {
    private String serverAddress;
    private int serverPort;
    private String secret;

    public ConnectionRequest(String serverAddress, int serverPort, String secret) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.secret = secret;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
