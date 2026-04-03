package com.example.winfinal.view;

import com.example.winfinal.controller.PromotionController;
import com.example.winfinal.dto.PromotionDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PromotionPanel extends JPanel {
    private final PromotionController promotionController;
    private JTable table;
    private DefaultTableModel tableModel;

    public PromotionPanel() {
        this.promotionController = new PromotionController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel header = new JLabel("Quản lý chương trình khuyến mãi");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(header, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm khuyến mãi", new Color(16, 185, 129));
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

        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Tên chương trình", "% Giảm giá", "Ngày bắt đầu", "Ngày kết thúc"};
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
        List<PromotionDTO> list = promotionController.getAll();
        for (PromotionDTO p : list) {
            tableModel.addRow(new Object[]{
                p.getPromoId(), 
                p.getPromoName(), 
                com.example.winfinal.utils.FormatUtils.formatPercentage(p.getDiscountPercentage()), 
                p.getStartDate(), 
                p.getEndDate()
            });
        }
    }

    private void showAddDialog() {
        JTextField name = new JTextField();
        JTextField discount = new JTextField();
        JTextField start = new JTextField(LocalDate.now().toString());
        JTextField end = new JTextField(LocalDate.now().plusMonths(1).toString());

        Object[] message = { 
            "Tên khuyến mãi:", name, 
            "Giảm giá (%):", discount, 
            "Ngày bắt đầu (yyyy-mm-dd):", start, 
            "Ngày kết thúc (yyyy-mm-dd):", end 
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Thêm khuyến mãi mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                promotionController.add(PromotionDTO.builder()
                    .promoName(name.getText())
                    .discountPercentage(new BigDecimal(discount.getText()))
                    .startDate(LocalDate.parse(start.getText()))
                    .endDate(LocalDate.parse(end.getText()))
                    .build());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng: " + ex.getMessage());
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
        PromotionDTO p = promotionController.getById(id);

        JTextField name = new JTextField(p.getPromoName());
        JTextField discount = new JTextField(p.getDiscountPercentage().toString());
        JTextField start = new JTextField(p.getStartDate().toString());
        JTextField end = new JTextField(p.getEndDate().toString());

        Object[] message = { 
            "Tên khuyến mãi:", name, 
            "Giảm giá (%):", discount, 
            "Ngày bắt đầu (yyyy-mm-dd):", start, 
            "Ngày kết thúc (yyyy-mm-dd):", end 
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Cập nhật khuyến mãi", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                p.setPromoName(name.getText());
                p.setDiscountPercentage(new BigDecimal(discount.getText()));
                p.setStartDate(LocalDate.parse(start.getText()));
                p.setEndDate(LocalDate.parse(end.getText()));
                promotionController.update(p);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng: " + ex.getMessage());
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khuyến mãi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                promotionController.delete((Integer) table.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
            } finally {
                loadData();
            }
        }
    }
}
