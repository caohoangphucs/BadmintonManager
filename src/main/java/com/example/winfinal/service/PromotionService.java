package com.example.winfinal.service;

import com.example.winfinal.dao.BookingDAO;
import com.example.winfinal.dao.PromotionDAO;
import com.example.winfinal.dto.PromotionDTO;
import com.example.winfinal.entity.Promotion;
import com.example.winfinal.mapper.PromotionMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PromotionService {
    private final PromotionDAO promotionDAO;
    private final BookingDAO bookingDAO;
    private final PromotionMapper promotionMapper = PromotionMapper.INSTANCE;

    public PromotionService() {
        this.promotionDAO = new PromotionDAO();
        this.bookingDAO = new BookingDAO();
    }

    public List<PromotionDTO> getAllPromotions() {
        return promotionDAO.findAll().stream()
                .map(promotionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PromotionDTO getPromotionById(Integer id) {
        return promotionDAO.findById(id)
                .map(promotionMapper::toDTO)
                .orElse(null);
    }

    public void addPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        promotionDAO.save(promotion);
    }

    public void updatePromotion(PromotionDTO promotionDTO) {
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        promotionDAO.update(promotion);
    }

    public void deletePromotion(Integer id) {
        if (bookingDAO.existsByPromotionId(id)) {
            throw new RuntimeException("Chương trình khuyến mãi này không thể xóa vì đang được sử dụng trong các lượt đặt sân.");
        }
        promotionDAO.deleteById(id);
    }
}
