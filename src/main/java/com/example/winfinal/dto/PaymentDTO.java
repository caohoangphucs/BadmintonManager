package com.example.winfinal.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Integer paymentId;
    private Integer bookingId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
}
