package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRentalDTO {
    private Integer rentalId;
    private Integer equipmentId;
    private String equipmentName;
    private Integer bookingId;
    private Integer quantity;
    private BigDecimal rentalPrice;
}
