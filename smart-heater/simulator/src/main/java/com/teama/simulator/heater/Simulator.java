package com.teama.simulator.heater;


import com.teama.simulator.ApplicationEvents;
import com.teama.simulator.SharedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

public class Simulator {
    private static final Logger logger = LogManager.getLogger();
    private final SharedState state;
    private final TimeProvider time;
    private final ApplicationEvents eventListener;
    private Instant laatsteSensorUpdate;
    private int standKraanPct = 0;
    private int standBranderPct = 0;
    private double huidigeTemperatuurBinnen = 15.0;
    private double huidigeDruk = 1.5;
    private double verbruikM3 = 0.0;
    private int slijtage = 0;
    private final BuitenTemperatuurSensor buitenTempSensor;
    private final BewegingsSensor bewegingSensor;
    private final DeurSensor deurSensor;

    public Simulator(SharedState state, TimeProvider provider, ApplicationEvents eventListener) {
        this.state = state;
        this.time = provider;
        this.eventListener = eventListener;
        this.laatsteSensorUpdate = this.time.currentInstant();
        this.bewegingSensor = new BewegingsSensor(this.time.currentInstant());
        this.buitenTempSensor = new BuitenTemperatuurSensor();
        this.deurSensor = new DeurSensor();
        this.publishSharedState();
    }

    public void iterate() {
        try {
            this.iterateWithExceptions();
        } catch (Exception var2) {
            this.eventListener.problem();
            logger.error("MAJOR PROBLEM: " + var2.getMessage());
            throw var2;
        }
    }

    private void iterateWithExceptions() {
        logger.trace("Iterate");
        this.verwerkAction();
        if (this.SensorsNeedUpdate()) {
            this.updateDruk();
            this.updateTemperatuurBinnen();
            this.valideerDrukEnSlijtage();
            this.updateBeweging();
            this.updateDeurOpenDicht();
            this.updateVerbruik();
            this.updateTemperatuurBuiten();
            this.publishSharedState();
            this.laatsteSensorUpdate = this.time.currentInstant();
        }

    }

    private void verwerkAction() {
        Optional<Action> action = this.state.geefAction();
        if (action.isPresent()) {
            this.standKraanPct = ((Action)action.get()).getStandKraanPct();
            this.standBranderPct = ((Action)action.get()).getStandBranderPct();
            logger.info(action.get());
        }

    }

    private void updateTemperatuurBinnen() {
        double gradenKouderBuiten;
        double gradenPerMinuutOptimum;
        double warmteVerlies;
        if (this.standBranderPct > 0 && this.huidigeDruk >= 1.5 && this.standKraanPct > 0) {
            gradenKouderBuiten = 0.4166666666666667;
            gradenPerMinuutOptimum = (double)this.standBranderPct / 100.0;
            warmteVerlies = gradenKouderBuiten * gradenPerMinuutOptimum;
            this.huidigeTemperatuurBinnen += warmteVerlies;
            logger.debug(String.format(Locale.ENGLISH, "Verwarmt: %.2f", warmteVerlies));
        }

        gradenKouderBuiten = this.huidigeTemperatuurBinnen - this.buitenTempSensor.getTemperatuur();
        if (gradenKouderBuiten > 0.0) {
            gradenPerMinuutOptimum = 0.0016666666666666668;
            warmteVerlies = gradenKouderBuiten * gradenPerMinuutOptimum;
            if (!this.deurSensor.isDicht()) {
                warmteVerlies *= 12.0;
            }

            logger.debug(String.format(Locale.ENGLISH, "Warmte verlies: %.2f", warmteVerlies));
            this.huidigeTemperatuurBinnen -= warmteVerlies;
        }

    }

    private void updateDruk() {
        double drukVerschil = 0.0;
        if (this.standKraanPct == 0 && this.standBranderPct > 0) {
            drukVerschil += 0.2;
        }

        if (this.standBranderPct > 80) {
            drukVerschil += (double)(this.standBranderPct - 80) / 100.0 * 0.25;
        }

        if (this.standBranderPct > 30) {
            drukVerschil += (double)(this.standBranderPct - 30) / 100.0 * 0.05;
        }

        if (this.standBranderPct < 30 && this.standKraanPct > 0 && this.huidigeDruk > 1.55) {
            drukVerschil = -0.05;
        }

        this.huidigeDruk += drukVerschil;
        if (drukVerschil > 0.01 || drukVerschil < -0.01) {
            logger.debug(String.format(Locale.ENGLISH, "Druk update: %.2f", drukVerschil));
        }

    }

    private void valideerDrukEnSlijtage() {
        if (this.huidigeDruk < 1.0) {
            ++this.slijtage;
        }

        if (this.huidigeDruk > 2.0) {
            ++this.slijtage;
        }

        if (this.huidigeDruk > 3.0) {
            throw new KetelDefect("Te hoge druk");
        } else if (this.slijtage > 100) {
            throw new KetelDefect("Ketel versleten, let op de druk");
        }
    }

    private void publishSharedState() {
        Status status = new Status();
        status.keteldruk = (int)(this.huidigeDruk * 100.0);
        status.binnenTemperatuur = this.huidigeTemperatuurBinnen;
        status.buitenTemperatuur = this.buitenTempSensor.getTemperatuur();
        status.deurDicht = this.deurSensor.isDicht();
        status.laatsteBeweging = this.bewegingSensor.getLaatsteBewegingGedetecteerd();
        status.gasverbruik = this.verbruikM3;
        status.gemaaktOp = this.time.currentInstant();
        this.state.updateStatus(status);
        String interneData = " Brander:" + this.standBranderPct + " % | Pomp: " + this.standKraanPct + " % | Slijtage: " + this.slijtage;
        logger.info(status.readable() + interneData);
    }

    private void updateVerbruik() {
        double maxVerbruikPerMinuut = 0.016666666666666666;
        double verbruik = (double)this.standBranderPct * maxVerbruikPerMinuut / 100.0;
        this.verbruikM3 += verbruik;
    }

    private void updateTemperatuurBuiten() {
        this.buitenTempSensor.iterate(this.time.currentInstant());
    }

    private boolean SensorsNeedUpdate() {
        return this.time.currentInstant().isAfter(this.laatsteSensorUpdate.plusSeconds(60L));
    }

    private void updateDeurOpenDicht() {
        this.deurSensor.iterate(this.time.currentInstant());
    }

    private void updateBeweging() {
        this.bewegingSensor.iterate(this.time.currentInstant());
    }

    public void setBuitenTemperatuur(double tempC) {
        this.buitenTempSensor.zetVastOp(tempC);
        this.publishSharedState();
    }

    public void setBinnenTemperatuur(double tempC) {
        this.huidigeTemperatuurBinnen = tempC;
        this.publishSharedState();
    }

    public void setDeurDicht() {
        this.deurSensor.zetVastOpDicht();
        this.publishSharedState();
    }

    public void setDeurOpen() {
        this.deurSensor.zetVastOpOpen();
        this.publishSharedState();
    }
}

