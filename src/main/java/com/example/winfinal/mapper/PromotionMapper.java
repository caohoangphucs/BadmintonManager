package com.example.winfinal.mapper;

import com.example.winfinal.dto.PromotionDTO;
import com.example.winfinal.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

    PromotionDTO toDTO(Promotion promotion);
    Promotion toEntity(PromotionDTO promotionDTO);
}
