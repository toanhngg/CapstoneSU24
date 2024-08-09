package fpt.CapstoneSU24.dto.payload;

import jakarta.validation.constraints.*;

public class FilterByTimeStampRequest {
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The startDate is required")
    private long startDate;
    @Min(value = 0, message = "Must be greater than or equal to zero")
    @NotNull(message = "The endDate is required")
    private long endDate;

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public boolean isValidDates() {
        return startDate < endDate;
    }
}
