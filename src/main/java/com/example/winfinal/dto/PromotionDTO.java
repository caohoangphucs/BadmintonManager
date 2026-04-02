package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Integer promoId;
    private String promoName;
    private BigDecimal discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
}
