package com.example.winfinal.mapper;

import com.example.winfinal.dto.CourtDTO;
import com.example.winfinal.entity.Court;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourtMapper {
    CourtMapper INSTANCE = Mappers.getMapper(CourtMapper.class);

    CourtDTO toDTO(Court court);
    Court toEntity(CourtDTO courtDTO);
}
