package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.util.List;

@Value
public class AdminStatisticsResponse {
    private List<String> labels;
    private List<Integer> data;
    private boolean isMonthly;
}
