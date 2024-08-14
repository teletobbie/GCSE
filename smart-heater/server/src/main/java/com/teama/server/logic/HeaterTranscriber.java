package com.teama.server.logic;

import com.teama.server.dto.connection.ConnectionResponse;
import com.teama.server.models.HeaterData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class HeaterTranscriber {
    public static HeaterData transcribeToHeaterData(String status) {
        if (status == null) return null;

        HeaterData heaterData;
        try {
            String[] parts = splitResponse(status);
            double pressure = convertPressure(parts[2]);
            double insideTemperature = Double.parseDouble(parts[3]);
            double outsideTemperature = Double.parseDouble(parts[4]);
            boolean doorSensor = convertToBoolean(parts[5]);
            LocalDateTime movementSensor = convertEpochToDate(parts[6]);
            double gasUsage = Double.parseDouble(parts[7]);
            LocalDateTime dateGenerated = convertEpochToDate(parts[8]);
            heaterData = new HeaterData(pressure, outsideTemperature, insideTemperature, doorSensor, movementSensor, gasUsage, dateGenerated);
            return heaterData;
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ConnectionResponse transcribeConnectionResponse(String response) {
        ConnectionResponse connectionResponse;
        try {
            String[] parts = splitResponse(response);
            String message = parts[1];
            LocalDateTime simulatedDate = convertEpochToDate(parts[2]);
            LocalDateTime realDate = convertEpochToDate(parts[3]);
            connectionResponse = new ConnectionResponse(message, simulatedDate, realDate);
            return connectionResponse;
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private static String[] splitResponse(String response) {
        return response.split("#");
    }

    private static double convertPressure(String pressure) {
        return Double.parseDouble(pressure) / 100;
    }

    private static boolean convertToBoolean(String text) {
        if (text.equals("0")) return true;
        else if (text.equals("1")) return false;
        else {
            System.out.println(text + " is not a 0 or 1, I cannot convert it to a boolean value.");
            return false;
        }
    }

    private static LocalDateTime convertEpochToDate(String epochString) {
        // The epoch is in seconds
        long epoch = Long.parseLong(epochString);
        Instant instant = Instant.ofEpochSecond(epoch);
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }
}
