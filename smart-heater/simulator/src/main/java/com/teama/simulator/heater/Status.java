package com.teama.simulator.heater;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Status {
    public static DateTimeFormatter TijdFormatOpUtc;
    public int keteldruk;
    public double binnenTemperatuur;
    public double buitenTemperatuur;
    public boolean deurDicht;
    public Instant laatsteBeweging;
    public double gasverbruik;
    public Instant gemaaktOp;

    public Status() {
    }

    public String toString() {
        String result = "#STAT";
        result = result + "#";
        result = result + this.keteldruk;
        result = result + "#";
        result = result + String.format(Locale.ENGLISH, "%.2f", this.binnenTemperatuur);
        result = result + "#";
        result = result + String.format(Locale.ENGLISH, "%.2f", this.buitenTemperatuur);
        result = result + "#";
        result = result + (this.deurDicht ? "1" : "0");
        result = result + "#";
        result = result + this.laatsteBeweging.getEpochSecond();
        result = result + "#";
        result = result + String.format(Locale.ENGLISH, "%.2f", this.gasverbruik);
        result = result + "#";
        result = result + this.gemaaktOp.getEpochSecond();
        return result;
    }

    public static Status fromString(String input) {
        String[] parts = input.split("#");
        Status stat = new Status();
        stat.keteldruk = Integer.valueOf(parts[2]);
        stat.binnenTemperatuur = Double.valueOf(parts[3]);
        stat.buitenTemperatuur = Double.valueOf(parts[4]);
        stat.deurDicht = !parts[5].equals("0");
        stat.laatsteBeweging = Instant.ofEpochSecond(Long.valueOf(parts[6]));
        stat.gasverbruik = Double.valueOf(parts[7]);
        stat.gemaaktOp = Instant.ofEpochSecond(Long.valueOf(parts[8]));
        return stat;
    }

    public String readable() {
        String result = "Status ";
        result = result + TijdFormatOpUtc.format(this.gemaaktOp);
        result = result + "\t| ";
        result = result + String.format(Locale.ENGLISH, "Druk: %.2f bar", (double)this.keteldruk / 100.0);
        result = result + "\t| ";
        result = result + String.format(Locale.ENGLISH, "Binnen: %.2f C", this.binnenTemperatuur);
        result = result + "\t| ";
        result = result + String.format(Locale.ENGLISH, "Buiten: %.2f C", this.buitenTemperatuur);
        result = result + "\t| ";
        result = result + (this.deurDicht ? "Deur dicht" : "Deur open");
        result = result + "\t| ";
        result = result + "Beweging: " + TijdFormatOpUtc.format(this.laatsteBeweging);
        result = result + "\t| ";
        result = result + String.format(Locale.ENGLISH, "Verbruik: %.2f m3", this.gasverbruik);
        result = result + "\t| ";
        return result;
    }

    static {
        TijdFormatOpUtc = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    }
}
