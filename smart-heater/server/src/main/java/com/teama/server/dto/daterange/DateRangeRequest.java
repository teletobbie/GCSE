package com.teama.server.dto.daterange;

import java.time.LocalDateTime;

public class DateRangeRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRangeRequest() {}
    public DateRangeRequest(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
