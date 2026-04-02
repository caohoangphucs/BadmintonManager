package com.example.winfinal.mapper;

import com.example.winfinal.dto.EquipmentDTO;
import com.example.winfinal.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {
    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    EquipmentDTO toDTO(Equipment equipment);
    Equipment toEntity(EquipmentDTO equipmentDTO);
}
