package com.example.winfinal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer equipmentId;

    @Column(name = "equipment_name", length = 50)
    private String equipmentName;

    private Integer quantity;

    @Column(name = "`condition`", length = 20)
    private String condition;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}
