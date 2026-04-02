package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtDTO {
    private Integer courtId;
    private String courtName;
    private String courtType;
    private String status;
    private BigDecimal pricePerHour;
}
