package com.example.winfinal.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Integer customerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String membershipType;
}
