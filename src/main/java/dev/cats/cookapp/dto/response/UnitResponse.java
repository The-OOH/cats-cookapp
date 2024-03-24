package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.cats.cookapp.models.Unit}
 */
@Value
public class UnitResponse implements Serializable {
    Long id;
    String name;
    Boolean isMetric;
}