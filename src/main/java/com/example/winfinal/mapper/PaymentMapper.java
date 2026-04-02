package com.example.winfinal.mapper;

import com.example.winfinal.dto.PaymentDTO;
import com.example.winfinal.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "booking.bookingId", target = "bookingId")
    PaymentDTO toDTO(Payment payment);
}
