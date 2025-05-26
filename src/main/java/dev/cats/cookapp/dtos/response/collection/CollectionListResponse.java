package dev.cats.cookapp.dtos.response.collection;

import lombok.Data;

import java.util.List;

@Data
public class CollectionListResponse {
    List<CollectionResponse> collections;
}
