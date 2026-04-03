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
        // ── Header wrapper (title + subtitle + separator) ─────────────────────
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(Color.WHITE);

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Color.WHITE);
        north.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title block: title + subtitle stacked
        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Color.WHITE);

        JLabel header = new JLabel("Quản lý khách hàng");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(31, 41, 55));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(header);

        JLabel subtitle = new JLabel("Theo dõi thông tin khách hàng và hạng thành viên");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        north.add(titleBlock, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = createStyledButton("Thêm khách hàng", new Color(16, 185, 129));
        JButton editBtn = createStyledButton("Sửa", new Color(245, 158, 11));
        JButton delBtn = createStyledButton("Xóa", new Color(239, 68, 68));
        JButton refreshBtn = createStyledButton("Làm mới", new Color(59, 130, 246));

        addBtn.addActionListener(e -> showForm(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa.");
                return;
            }
            showForm(customerController.getById((Integer) table.getValueAt(row, 0)));
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

        headerWrapper.add(north);
        headerWrapper.add(Box.createVerticalStrut(10));

        // Subtle separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 222, 226));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        headerWrapper.add(sep);
        headerWrapper.add(Box.createVerticalStrut(10));

        add(headerWrapper, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Họ tên", "Số điện thoại", "Email", "Hạng TV"}, 0);
        table = new com.example.winfinal.view.components.ModernTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        customerController.getAll().forEach(c -> {
            String tier = c.getMembershipType();
            String tierHTML = tier;
            if (tier != null) {
                if (tier.equalsIgnoreCase("VIP") || tier.equalsIgnoreCase("Gold") || tier.contains("Gold")) {
                    tierHTML = "<html><font color='#d4ac0d'><b>  " + tier + "  </b></font></html>"; 
                } else if (tier.equalsIgnoreCase("Thân thiết") || tier.equalsIgnoreCase("Silver") || tier.contains("Silver")) {
                    tierHTML = "<html><font color='#7f8c8d'><b>  " + tier + "  </b></font></html>"; 
                } else if (tier.equalsIgnoreCase("Thường") || tier.equalsIgnoreCase("Regular") || tier.contains("Regular")) {
                    tierHTML = "<html><font color='#2980b9'><b>  " + tier + "  </b></font></html>"; 
                } else {
                    tierHTML = "<html><font color='#34495e'><b>  " + tier + "  </b></font></html>";
                }
            }
            tableModel.addRow(new Object[]{c.getCustomerId(), c.getFullName(), c.getPhoneNumber(), c.getEmail(), tierHTML});
        });
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
        return new com.example.winfinal.view.components.ModernButton(t, c);
    }
}
