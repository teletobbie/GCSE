package com.teama.server.dto.connection;

import java.time.LocalDateTime;

public class ConnectionResponse {
    private String message;
    private LocalDateTime simulatedDate;
    private LocalDateTime realDate;

    public ConnectionResponse(String message, LocalDateTime simulatedDate, LocalDateTime realDate) {
        this.message = message;
        this.simulatedDate = simulatedDate;
        this.realDate = realDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSimulatedDate() {
        return simulatedDate;
    }

    public void setSimulatedDate(LocalDateTime simulatedDate) {
        this.simulatedDate = simulatedDate;
    }

    public LocalDateTime getRealDate() {
        return realDate;
    }

    public void setRealDate(LocalDateTime realDate) {
        this.realDate = realDate;
    }
}
