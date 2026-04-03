package com.example.winfinal.view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernTable extends JTable {
    private int hoveredRow = -1;

    public ModernTable(DefaultTableModel model) {
        super(model);
        
        // Basic Settings
        setRowHeight(40);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(235, 235, 235));
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(52, 152, 219).brighter()); // Selection color
        setSelectionForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setFillsViewportHeight(true);

        // Header Styling
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setDefaultRenderer(new ModernTableHeaderRenderer());

        // Hover Effect
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                repaint();
            }
        });
    }

    @Override
    public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        if (!isRowSelected(row)) {
            if (row == hoveredRow) {
                comp.setBackground(new Color(240, 244, 250)); // Hover color
            } else if (row % 2 == 0) {
                comp.setBackground(Color.WHITE);
            } else {
                comp.setBackground(new Color(250, 251, 253)); // Zebra striping
            }
            comp.setForeground(new Color(45, 52, 54));
        }
        
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Padding
            
            String colName = getColumnName(column).toLowerCase();
            if (colName.equals("id") || colName.contains("mã id")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (colName.contains("giá") || colName.contains("tiền") || colName.contains("tổng") || colName.contains("doanh thu") || colName.contains("%") || colName.contains("số lượng") || colName.contains("chi tiêu")) {
                label.setHorizontalAlignment(JLabel.RIGHT);
            } else if (colName.contains("ngày") || colName.contains("bắt đầu") || colName.contains("kết thúc") || colName.contains("giờ") || colName.contains("tháng")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (colName.contains("trạng thái") || colName.contains("tình trạng")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else {
                label.setHorizontalAlignment(JLabel.LEFT);
            }
        }
        return comp;
    }

    private static class ModernTableHeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            comp.setBackground(new Color(243, 244, 246));
            comp.setForeground(new Color(31, 41, 55));
            comp.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            JLabel label = (JLabel) comp;
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
            ));
            
            String colName = table.getColumnName(column).toLowerCase();
            if (colName.equals("id") || colName.contains("mã id")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (colName.contains("giá") || colName.contains("tiền") || colName.contains("tổng") || colName.contains("doanh thu") || colName.contains("%") || colName.contains("số lượng") || colName.contains("chi tiêu")) {
                label.setHorizontalAlignment(JLabel.RIGHT);
            } else if (colName.contains("ngày") || colName.contains("bắt đầu") || colName.contains("kết thúc") || colName.contains("giờ") || colName.contains("tháng")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (colName.contains("trạng thái") || colName.contains("tình trạng")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else {
                label.setHorizontalAlignment(JLabel.LEFT);
            }
            
            return comp;
        }
    }
}
