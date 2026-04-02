package com.example.winfinal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Court")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Integer courtId;

    @Column(name = "court_name", length = 50)
    private String courtName;

    @Column(name = "court_type", length = 20)
    private String courtType;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "price_per_hour", precision = 10, scale = 2)
    private BigDecimal pricePerHour;
}
