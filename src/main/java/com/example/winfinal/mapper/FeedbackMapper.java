package com.example.winfinal.mapper;

import com.example.winfinal.dto.FeedbackDTO;
import com.example.winfinal.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerFullName")
    @Mapping(source = "booking.bookingId", target = "bookingId")
    FeedbackDTO toDTO(Feedback feedback);
}
