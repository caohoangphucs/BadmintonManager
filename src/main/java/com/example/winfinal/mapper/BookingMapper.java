package com.example.winfinal.mapper;

import com.example.winfinal.dto.BookingDTO;
import com.example.winfinal.entity.Booking;
import com.example.winfinal.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerFullName")
    @Mapping(source = "court.courtId", target = "courtId")
    @Mapping(source = "court.courtName", target = "courtName")
    @Mapping(source = "promotions", target = "promoIds", qualifiedByName = "promotionsToIds")
    BookingDTO toDTO(Booking booking);

    @Named("promotionsToIds")
    default Set<Integer> promotionsToIds(Set<Promotion> promotions) {
        if (promotions == null) return null;
        return promotions.stream().map(Promotion::getPromoId).collect(Collectors.toSet());
    }
}
