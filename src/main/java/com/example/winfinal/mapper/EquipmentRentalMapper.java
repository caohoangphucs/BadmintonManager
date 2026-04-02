package com.example.winfinal.mapper;

import com.example.winfinal.dto.EquipmentRentalDTO;
import com.example.winfinal.entity.EquipmentRental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentRentalMapper {
    EquipmentRentalMapper INSTANCE = Mappers.getMapper(EquipmentRentalMapper.class);

    @Mapping(source = "equipment.equipmentId", target = "equipmentId")
    @Mapping(source = "equipment.equipmentName", target = "equipmentName")
    @Mapping(source = "booking.bookingId", target = "bookingId")
    EquipmentRentalDTO toDTO(EquipmentRental equipmentRental);
}
