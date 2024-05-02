package dev.cats.cookapp.dto.response;

import lombok.Value;

@Value
public class ProductResponse {
    Long id;
    String aisle;
    String image;
    String name;

}
