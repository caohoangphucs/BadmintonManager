package com.example.winfinal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;
}
