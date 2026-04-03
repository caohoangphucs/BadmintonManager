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
        // ── Header wrapper (title + subtitle + separator) ─────────────────────
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title block: title + subtitle stacked
        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Color.WHITE);

        JLabel header = new JLabel("Quản lý trang thiết bị");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(31, 41, 55));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(header);

        JLabel subtitle = new JLabel("Quản lý thiết bị, số lượng và tình trạng sử dụng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        headerPanel.add(titleBlock, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm thiết bị", new Color(16, 185, 129));
        JButton btnEdit = createButton("Sửa", new Color(245, 158, 11));
        JButton btnDelete = createButton("Xóa", new Color(239, 68, 68));
        JButton btnRefresh = createButton("Làm mới", new Color(59, 130, 246));

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnRefresh);
        headerPanel.add(actions, BorderLayout.EAST);

        headerWrapper.add(headerPanel);
        headerWrapper.add(Box.createVerticalStrut(10));

        // Subtle separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 222, 226));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        headerWrapper.add(sep);
        headerWrapper.add(Box.createVerticalStrut(10));

        add(headerWrapper, BorderLayout.NORTH);

        String[] cols = {"ID", "Tên thiết bị", "Số lượng", "Tình trạng", "Giá thuê/bán (VND)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new com.example.winfinal.view.components.ModernTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color color) {
        return new com.example.winfinal.view.components.ModernButton(text, color);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<EquipmentDTO> items = equipmentController.getAll();
        for (EquipmentDTO item : items) {
            String condHTML = item.getCondition();
            if ("New".equalsIgnoreCase(condHTML)) {
                condHTML = "<html><font color='#27ae60'><b>Mới</b></font></html>";
            } else if ("Excellent".equalsIgnoreCase(condHTML)) {
                condHTML = "<html><font color='#2980b9'><b>Rất tốt</b></font></html>";
            } else if ("Good".equalsIgnoreCase(condHTML)) {
                condHTML = "<html><font color='#e67e22'><b>Tốt</b></font></html>";
            }
            tableModel.addRow(new Object[]{
                item.getEquipmentId(), 
                item.getEquipmentName(), 
                item.getQuantity(), 
                condHTML, 
                com.example.winfinal.utils.FormatUtils.formatCurrency(item.getPrice())
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
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa.");
            return;
        }
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
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thiết bị này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                equipmentController.delete((Integer) table.getValueAt(row, 0));
                loadData();
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
