package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceInvoiceDTO {
    private Integer serviceId;
    private Integer bookingId;
    private String serviceName;
    private BigDecimal price;
}
