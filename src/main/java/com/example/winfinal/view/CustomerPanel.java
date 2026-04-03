package com.example.winfinal.view;

import com.example.winfinal.controller.CustomerController;
import com.example.winfinal.dto.CustomerDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private final CustomerController customerController;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerPanel() {
        this.customerController = new CustomerController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Color.WHITE);
        JLabel header = new JLabel("Quản lý khách hàng");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        north.add(header, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = createStyledButton("Thêm khách hàng", new Color(46, 204, 113));
        JButton editBtn = createStyledButton("Sửa", new Color(241, 196, 15));
        JButton delBtn = createStyledButton("Xóa", new Color(231, 76, 60));
        JButton refreshBtn = createStyledButton("Làm mới", new Color(52, 152, 219));

        addBtn.addActionListener(e -> showForm(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showForm(customerController.getById((Integer) table.getValueAt(row, 0)));
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    customerController.delete((Integer) table.getValueAt(row, 0));
                    JOptionPane.showMessageDialog(this, "Xóa thành công.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
                } finally {
                    loadData();
                }
            }
        });
        refreshBtn.addActionListener(e -> loadData());

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);
        north.add(btnPanel, BorderLayout.EAST);
        add(north, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Họ tên", "Số điện thoại", "Email", "Hạng TV"}, 0);
        table = new com.example.winfinal.view.components.ModernTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        customerController.getAll().forEach(c -> 
            tableModel.addRow(new Object[]{c.getCustomerId(), c.getFullName(), c.getPhoneNumber(), c.getEmail(), c.getMembershipType()})
        );
    }

    private void showForm(CustomerDTO customer) {
        JTextField name = new JTextField(customer != null ? customer.getFullName() : "");
        JTextField phone = new JTextField(customer != null ? customer.getPhoneNumber() : "");
        JTextField email = new JTextField(customer != null ? customer.getEmail() : "");
        JComboBox<String> membership = new JComboBox<>(new String[]{"Thường", "Thân thiết", "VIP"});
        if (customer != null) membership.setSelectedItem(customer.getMembershipType());

        Object[] fields = {"Họ tên:", name, "Điện thoại:", phone, "Email:", email, "Hạng TV:", membership};
        if (JOptionPane.showConfirmDialog(this, fields, customer == null ? "Thêm mới" : "Chỉnh sửa", JOptionPane.OK_CANCEL_OPTION) == 0) {
            CustomerDTO dto = customer != null ? customer : new CustomerDTO();
            dto.setFullName(name.getText());
            dto.setPhoneNumber(phone.getText());
            dto.setEmail(email.getText());
            dto.setMembershipType((String) membership.getSelectedItem());
            if (customer == null) customerController.create(dto);
            else customerController.update(dto);
            loadData();
        }
    }

    private JButton createStyledButton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
