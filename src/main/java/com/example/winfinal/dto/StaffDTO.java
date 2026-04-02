package com.example.winfinal.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDTO {
    private Integer staffId;
    private String fullName;
    private String role;
    private String phoneNumber;
    private String email;
}
