package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.util.List;

@Value
public class UserStatisticsResponse {
    private List<DataRow> data;

    @Value
    public static class DataRow {
        private String date;
        private int count;
    }
}
