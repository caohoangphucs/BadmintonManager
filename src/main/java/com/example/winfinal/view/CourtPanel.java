package com.example.winfinal.view;

import com.example.winfinal.controller.CourtController;
import com.example.winfinal.dto.CourtDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourtPanel extends JPanel {
    private final CourtController courtController;
    private JTable table;
    private DefaultTableModel tableModel;

    public CourtPanel() {
        this.courtController = new CourtController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Header and Actions
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel header = new JLabel("Quản lý sân");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(header, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm sân", new Color(46, 204, 113));
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

        // Table
        String[] cols = {"ID", "Tên sân", "Loại thảm", "Trạng thái", "Giá/Giờ"};
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
        List<CourtDTO> courts = courtController.getAllCourts();
        for (CourtDTO c : courts) {
            tableModel.addRow(new Object[]{c.getCourtId(), c.getCourtName(), c.getCourtType(), c.getStatus(), c.getPricePerHour()});
        }
    }

    private void showAddDialog() {
        JTextField name = new JTextField();
        JTextField type = new JTextField();
        JTextField status = new JTextField("Available");
        JTextField price = new JTextField();

        Object[] message = { "Tên sân:", name, "Loại thảm:", type, "Trạng thái:", status, "Giá/Giờ:", price };
        int option = JOptionPane.showConfirmDialog(this, message, "Thêm sân mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            courtController.addCourt(CourtDTO.builder()
                .courtName(name.getText())
                .courtType(type.getText())
                .status(status.getText())
                .pricePerHour(new java.math.BigDecimal(price.getText()))
                .build());
            loadData();
        }
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Integer id = (Integer) table.getValueAt(row, 0);
        CourtDTO current = courtController.getCourt(id);

        JTextField name = new JTextField(current.getCourtName());
        JTextField type = new JTextField(current.getCourtType());
        JTextField status = new JTextField(current.getStatus());
        JTextField price = new JTextField(current.getPricePerHour().toString());

        Object[] message = { "Tên sân:", name, "Loại thảm:", type, "Trạng thái:", status, "Giá/Giờ:", price };
        int option = JOptionPane.showConfirmDialog(this, message, "Cập nhật thông tin sân", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            current.setCourtName(name.getText());
            current.setCourtType(type.getText());
            current.setStatus(status.getText());
            current.setPricePerHour(new java.math.BigDecimal(price.getText()));
            courtController.updateCourt(current);
            loadData();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sân này?") == JOptionPane.YES_OPTION) {
            courtController.deleteCourt((Integer) table.getValueAt(row, 0));
            loadData();
        }
    }
}
