package com.example.winfinal.service;

import com.example.winfinal.dao.EquipmentDAO;
import com.example.winfinal.dto.EquipmentDTO;
import com.example.winfinal.entity.Equipment;
import com.example.winfinal.mapper.EquipmentMapper;

import java.util.List;
import java.util.stream.Collectors;

public class EquipmentService {
    private final EquipmentDAO equipmentDAO;
    private final EquipmentMapper equipmentMapper = EquipmentMapper.INSTANCE;

    public EquipmentService() {
        this.equipmentDAO = new EquipmentDAO();
    }

    public List<EquipmentDTO> getAllEquipment() {
        return equipmentDAO.findAll().stream()
                .map(equipmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EquipmentDTO getEquipmentById(Integer id) {
        return equipmentDAO.findById(id)
                .map(equipmentMapper::toDTO)
                .orElse(null);
    }

    public void addEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipmentDAO.save(equipment);
    }

    public void updateEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipmentDAO.update(equipment);
    }

    public void deleteEquipment(Integer id) {
        equipmentDAO.deleteById(id);
    }
}
