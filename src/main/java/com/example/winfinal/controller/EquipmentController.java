package com.example.winfinal.controller;

import com.example.winfinal.dto.EquipmentDTO;
import com.example.winfinal.service.EquipmentService;

import java.util.List;

public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController() {
        this.equipmentService = new EquipmentService();
    }

    public List<EquipmentDTO> getAll() {
        return equipmentService.getAllEquipment();
    }

    public EquipmentDTO getById(Integer id) {
        return equipmentService.getEquipmentById(id);
    }

    public void add(EquipmentDTO dto) {
        equipmentService.addEquipment(dto);
    }

    public void update(EquipmentDTO dto) {
        equipmentService.updateEquipment(dto);
    }

    public void delete(Integer id) {
        equipmentService.deleteEquipment(id);
    }
}
