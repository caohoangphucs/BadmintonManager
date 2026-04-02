package com.example.winfinal.view;

import com.example.winfinal.controller.EquipmentController;
import com.example.winfinal.dto.EquipmentDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EquipmentPanel extends JPanel {
    private final EquipmentController equipmentController;
    private JTable table;
    private DefaultTableModel tableModel;

    public EquipmentPanel() {
        this.equipmentController = new EquipmentController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel header = new JLabel("Quản lý trang thiết bị");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(header, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm thiết bị", new Color(46, 204, 113));
        JButton btnEdit = createButton("Sửa", new Color(241, 196, 15));
        JButton btnDelete = createButton("Xóa", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Làm mới", new Color(52, 152, 219));

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnRefresh);
        headerPanel.add(actions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Tên thiết bị", "Số lượng", "Tình trạng", "Giá thuê/bán"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<EquipmentDTO> items = equipmentController.getAll();
        for (EquipmentDTO item : items) {
            tableModel.addRow(new Object[]{
                item.getEquipmentId(), 
                item.getEquipmentName(), 
                item.getQuantity(), 
                item.getCondition(), 
                item.getPrice()
            });
        }
    }

    private void showAddDialog() {
        JTextField name = new JTextField();
        JTextField qty = new JTextField();
        JTextField cond = new JTextField("Good");
        JTextField price = new JTextField();

        Object[] message = { 
            "Tên thiết bị:", name, 
            "Số lượng:", qty, 
            "Tình trạng:", cond, 
            "Giá:", price 
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Thêm thiết bị mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                equipmentController.add(EquipmentDTO.builder()
                    .equipmentName(name.getText())
                    .quantity(Integer.parseInt(qty.getText()))
                    .condition(cond.getText())
                    .price(new BigDecimal(price.getText()))
                    .build());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        }
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Integer id = (Integer) table.getValueAt(row, 0);
        EquipmentDTO current = equipmentController.getById(id);

        JTextField name = new JTextField(current.getEquipmentName());
        JTextField qty = new JTextField(current.getQuantity().toString());
        JTextField cond = new JTextField(current.getCondition());
        JTextField price = new JTextField(current.getPrice().toString());

        Object[] message = { 
            "Tên thiết bị:", name, 
            "Số lượng:", qty, 
            "Tình trạng:", cond, 
            "Giá:", price 
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Cập nhật thiết bị", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                current.setEquipmentName(name.getText());
                current.setQuantity(Integer.parseInt(qty.getText()));
                current.setCondition(cond.getText());
                current.setPrice(new BigDecimal(price.getText()));
                equipmentController.update(current);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thiết bị này?") == JOptionPane.YES_OPTION) {
            equipmentController.delete((Integer) table.getValueAt(row, 0));
            loadData();
        }
    }
}
