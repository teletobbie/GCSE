package com.teama.simulator.heater;

public class Action {
    private final int standKraanPct;
    private final int standBranderPct;

    public Action(int standKraanPct, int standBranderPct) {
        this.standKraanPct = standKraanPct;
        this.standBranderPct = standBranderPct;
    }

    public Action(String kraanPct, String branderPct) {
        this(Integer.parseInt(kraanPct), Integer.parseInt(branderPct));
    }

    public int getStandKraanPct() {
        return this.standKraanPct;
    }

    public int getStandBranderPct() {
        return this.standBranderPct;
    }

    public String toString() {
        return "Action => kraan: " + this.standKraanPct + "% brander: " + this.standBranderPct + "%";
    }
}
