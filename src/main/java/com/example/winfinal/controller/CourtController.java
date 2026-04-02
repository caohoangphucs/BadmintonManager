package com.example.winfinal.controller;

import com.example.winfinal.dto.CourtDTO;
import com.example.winfinal.service.CourtService;

import java.util.List;

public class CourtController {
    private final CourtService courtService;

    public CourtController() {
        this.courtService = new CourtService();
    }

    public List<CourtDTO> getAllCourts() {
        return courtService.getAllCourts();
    }

    public CourtDTO getCourt(Integer id) {
        return courtService.getCourtById(id);
    }

    public void addCourt(CourtDTO courtDTO) {
        courtService.addCourt(courtDTO);
    }

    public void updateCourt(CourtDTO courtDTO) {
        courtService.updateCourt(courtDTO);
    }

    public void deleteCourt(Integer id) {
        courtService.deleteCourt(id);
    }
}
