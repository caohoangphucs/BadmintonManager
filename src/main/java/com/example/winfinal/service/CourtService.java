package com.example.winfinal.service;

import com.example.winfinal.dao.CourtDAO;
import com.example.winfinal.dto.CourtDTO;
import com.example.winfinal.entity.Court;
import com.example.winfinal.mapper.CourtMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CourtService {
    private final CourtDAO courtDAO;
    private final CourtMapper courtMapper = CourtMapper.INSTANCE;

    public CourtService() {
        this.courtDAO = new CourtDAO();
    }

    public List<CourtDTO> getAllCourts() {
        return courtDAO.findAll().stream()
                .map(courtMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourtDTO getCourtById(Integer id) {
        return courtDAO.findById(id)
                .map(courtMapper::toDTO)
                .orElse(null);
    }

    public void addCourt(CourtDTO courtDTO) {
        Court court = courtMapper.toEntity(courtDTO);
        courtDAO.save(court);
    }

    public void updateCourt(CourtDTO courtDTO) {
        Court court = courtMapper.toEntity(courtDTO);
        courtDAO.update(court);
    }

    public void deleteCourt(Integer id) {
        courtDAO.deleteById(id);
    }
}
