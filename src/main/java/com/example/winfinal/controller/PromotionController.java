package com.example.winfinal.controller;

import com.example.winfinal.dto.PromotionDTO;
import com.example.winfinal.service.PromotionService;

import java.util.List;

public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController() {
        this.promotionService = new PromotionService();
    }

    public List<PromotionDTO> getAll() {
        return promotionService.getAllPromotions();
    }

    public PromotionDTO getById(Integer id) {
        return promotionService.getPromotionById(id);
    }

    public void add(PromotionDTO dto) {
        promotionService.addPromotion(dto);
    }

    public void update(PromotionDTO dto) {
        promotionService.updatePromotion(dto);
    }

    public void delete(Integer id) {
        promotionService.deletePromotion(id);
    }
}
