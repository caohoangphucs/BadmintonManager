package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDTO {
    private Integer equipmentId;
    private String equipmentName;
    private Integer quantity;
    private String condition;
    private BigDecimal price;
}
