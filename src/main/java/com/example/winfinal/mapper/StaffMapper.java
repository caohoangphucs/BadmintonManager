package com.example.winfinal.mapper;

import com.example.winfinal.dto.StaffDTO;
import com.example.winfinal.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StaffMapper {
    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    StaffDTO toDTO(Staff staff);
    Staff toEntity(StaffDTO staffDTO);
}
