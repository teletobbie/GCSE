package com.teama.server.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Heater {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Heater(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public String connect(String secret) {
        try {
            socket = new Socket(serverAddress, serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("$CV-CONNECT-$-"+secret);
            return reader.readLine();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void setCVLevel(int pumpLevel, int burnerLevel) {
        try {
            String requestBody = String.format("$CV-ACT-$%d$%d", pumpLevel, burnerLevel);
            String response = request(requestBody);
            // System.out.println(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getStatus() {
        try {
            return request("$CV-STAT?");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private String request(String requestBody) throws IOException {
        writer.println(requestBody);
        return reader.readLine();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}