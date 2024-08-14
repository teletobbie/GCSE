package com.teama.server.services;

import com.teama.server.exceptions.EntityNotFoundException;
import com.teama.server.exceptions.HeaterConnectionException;
import com.teama.server.exceptions.NoHeaterException;
import com.teama.server.logic.Heater;
import com.teama.server.logic.HeaterManager;
import com.teama.server.logic.HeaterTranscriber;
import com.teama.server.dto.CV;
import com.teama.server.dto.connection.ConnectionResponse;
import com.teama.server.models.HeaterData;
import com.teama.server.dto.connection.ConnectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HeaterService {
    private Heater heater;
    private HeaterData lastResponse;
    private final HeaterTranscriber heaterTranscriber = new HeaterTranscriber();
    @Autowired
    private HeaterManager heaterManager;

    public ConnectionResponse connectToHeater(ConnectionRequest connectionRequest) throws HeaterConnectionException {
        heater = new Heater(connectionRequest.getServerAddress(), connectionRequest.getServerPort());
        String connection = heater.connect(connectionRequest.getSecret());
        if (connection == null) throw new HeaterConnectionException();

        ConnectionResponse response = heaterTranscriber.transcribeConnectionResponse(connection);
        if (response == null) throw new HeaterConnectionException();

        heaterManager.addHeater(heater);
        // A bad connection should also be shut down here!

        return response;
    }

    public HeaterData getHeaterData(int id) throws EntityNotFoundException {
        return heaterManager.getHeaterData(id);
    }

    public List<HeaterData> getHeaterDatas() {
        return heaterManager.getHeaterDatas();
    }

    public void setCVLevel(CV cv) throws NoHeaterException {
        if (heater == null) throw new NoHeaterException();

        heater.setCVLevel(cv.getPumpLevel(), cv.getBurnerLevel());
    }
}