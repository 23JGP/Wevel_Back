package com.wevel.wevel_server.tripInfo.dto;

public class SpentPercentageResponse {
    private Double spentPercentage;

    public SpentPercentageResponse(Double spentPercentage) {
        this.spentPercentage = spentPercentage;
    }

    public Double getSpentPercentage() {
        return spentPercentage;
    }
}
