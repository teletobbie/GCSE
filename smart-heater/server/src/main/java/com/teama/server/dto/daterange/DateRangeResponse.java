package com.teama.server.dto.daterange;

import java.time.LocalDate;

public class DateRangeResponse {
    private LocalDate date;
    private double gasUsageStd;
    private double gasUsageStdPlus;
    private double gasUsageDeluxe;
    private double gasUsageSuperDeluxe;

    public DateRangeResponse(LocalDate date, double gasUsageStd, double gasUsageStdPlus, double gasUsageDeluxe, double gasUsageSuperDeluxe) {
        this.date = date;
        this.gasUsageStd = gasUsageStd;
        this.gasUsageStdPlus = gasUsageStdPlus;
        this.gasUsageDeluxe = gasUsageDeluxe;
        this.gasUsageSuperDeluxe = gasUsageSuperDeluxe;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public double getGasUsageStd() {
        return gasUsageStd;
    }
    public void setGasUsageStd(double gasUsageStd) {
        this.gasUsageStd = gasUsageStd;
    }
    public double getGasUsageStdPlus() {
        return gasUsageStdPlus;
    }
    public void setGasUsageStdPlus(double gasUsageStdPlus) {
        this.gasUsageStdPlus = gasUsageStdPlus;
    }
    public double getGasUsageDeluxe() {
        return gasUsageDeluxe;
    }
    public void setGasUsageDeluxe(double gasUsageDeluxe) {
        this.gasUsageDeluxe = gasUsageDeluxe;
    }
    public double getGasUsageSuperDeluxe() {
        return gasUsageSuperDeluxe;
    }
    public void setGasUsageSuperDeluxe(double gasUsageSuperDeluxe) {
        this.gasUsageSuperDeluxe = gasUsageSuperDeluxe;
    }
}
