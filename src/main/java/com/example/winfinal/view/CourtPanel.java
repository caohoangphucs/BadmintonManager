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
        // ── Header wrapper (title + subtitle + separator) ─────────────────────
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(Color.WHITE);

        // Title row (title left + action buttons right)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title block: title + subtitle stacked
        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(Color.WHITE);

        JLabel header = new JLabel("Quản lý sân");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(31, 41, 55));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(header);

        JLabel subtitle = new JLabel("Theo dõi và quản lý thông tin các sân cầu lông");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(subtitle);

        headerPanel.add(titleBlock, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm sân", new Color(16, 185, 129));
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

        // Table
        String[] cols = { "ID", "Tên sân", "Loại thảm", "Trạng thái", "Giá/Giờ (VND)" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
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
        List<CourtDTO> courts = courtController.getAllCourts();
        for (CourtDTO c : courts) {
            String statusHTML = c.getStatus();
            if ("Available".equalsIgnoreCase(statusHTML)) {
                statusHTML = "<html><font color='#27ae60'><b>Đang hoạt động</b></font></html>";
            } else if ("Maintenance".equalsIgnoreCase(statusHTML)) {
                statusHTML = "<html><font color='#e67e22'><b>Bảo trì</b></font></html>";
            }
            tableModel.addRow(new Object[] { c.getCourtId(), c.getCourtName(), c.getCourtType(), statusHTML,
                    com.example.winfinal.utils.FormatUtils.formatCurrency(c.getPricePerHour()) });
        }
    }

    // ── Court-type options ────────────────────────────────────────────────────
    private static final String[] COURT_TYPES = {
            "Thảm Hải Yến",
            "Thảm Yonex",
            "Sân Gỗ",
            "Thảm Đặc Biệt"
    };

    // ── Status helpers: DB value ↔ Vietnamese display label ───────────────────
    private static String statusToViet(String dbValue) {
        if ("Available".equalsIgnoreCase(dbValue))
            return "Đang hoạt động";
        if ("Maintenance".equalsIgnoreCase(dbValue))
            return "Bảo trì";
        return "Đang hoạt động"; // default
    }

    private static String statusToDb(String vietLabel) {
        if ("Bảo trì".equals(vietLabel))
            return "Maintenance";
        return "Available"; // default covers "Đang hoạt động"
    }

    private void showAddDialog() {
        JTextField name = new JTextField();

        JComboBox<String> typeCombo = new JComboBox<>(COURT_TYPES);
        typeCombo.setPreferredSize(new Dimension(200, 30));

        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Đang hoạt động", "Bảo trì" });
        statusCombo.setPreferredSize(new Dimension(200, 30));

        JTextField price = new JTextField();

        Object[] message = {
                "Tên sân:", name,
                "Loại thảm:", typeCombo,
                "Trạng thái:", statusCombo,
                "Giá/Giờ:", price
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Thêm sân mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                courtController.addCourt(CourtDTO.builder()
                        .courtName(name.getText())
                        .courtType((String) typeCombo.getSelectedItem())
                        .status(statusToDb((String) statusCombo.getSelectedItem()))
                        .pricePerHour(new java.math.BigDecimal(price.getText()))
                        .build());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
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
        CourtDTO current = courtController.getCourt(id);

        JTextField name = new JTextField(current.getCourtName());

        JComboBox<String> typeCombo = new JComboBox<>(COURT_TYPES);
        typeCombo.setPreferredSize(new Dimension(200, 30));
        // Pre-select the current court type if it matches one of the options
        String currentType = current.getCourtType();
        boolean typeFound = false;
        for (String t : COURT_TYPES) {
            if (t.equalsIgnoreCase(currentType)) {
                typeCombo.setSelectedItem(t);
                typeFound = true;
                break;
            }
        }
        if (!typeFound && currentType != null && !currentType.isBlank()) {
            // Keep the existing value as a custom entry without losing data
            typeCombo.setEditable(true);
            typeCombo.setSelectedItem(currentType);
        }

        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Đang hoạt động", "Bảo trì" });
        statusCombo.setPreferredSize(new Dimension(200, 30));
        statusCombo.setSelectedItem(statusToViet(current.getStatus()));

        JTextField price = new JTextField(current.getPricePerHour().toString());

        Object[] message = {
                "Tên sân:", name,
                "Loại thảm:", typeCombo,
                "Trạng thái:", statusCombo,
                "Giá/Giờ:", price
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Cập nhật thông tin sân",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                current.setCourtName(name.getText());
                current.setCourtType((String) typeCombo.getSelectedItem());
                current.setStatus(statusToDb((String) statusCombo.getSelectedItem()));
                current.setPricePerHour(new java.math.BigDecimal(price.getText()));
                courtController.updateCourt(current);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sân này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                courtController.deleteCourt((Integer) table.getValueAt(row, 0));
                loadData();
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
