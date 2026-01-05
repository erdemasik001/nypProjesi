package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminScreen extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable flightTable;
    private DefaultTableModel flightTableModel;
    private JTable staffTable;
    private DefaultTableModel staffTableModel;

    public AdminScreen() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin/Staff Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = createGradientPanel(new Color(44, 62, 80), new Color(52, 73, 94));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("⚙ Admin Panel", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.addTab("✈ Flight Management", createFlightPanel());
        tabbedPane.addTab("👥 Staff Management", createStaffPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
        bottomPanel.add(logoutBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createGradientPanel(Color c1, Color c2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private JPanel createFlightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(236, 240, 241));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnPanel.setOpaque(false);
        JButton addBtn = createStyledButton("Add New Flight", new Color(46, 204, 113));
        addBtn.setPreferredSize(new Dimension(150, 40));
        addBtn.addActionListener(e -> addFlight());
        JButton editBtn = createStyledButton("Edit", new Color(52, 152, 219));
        editBtn.setPreferredSize(new Dimension(100, 40));
        editBtn.addActionListener(e -> editFlight());
        JButton delBtn = createStyledButton("Delete", new Color(231, 76, 60));
        delBtn.setPreferredSize(new Dimension(80, 40));
        delBtn.addActionListener(e -> deleteFlight());
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        panel.add(btnPanel, BorderLayout.NORTH);

        String[] cols = { "Flight No", "Departure", "Arrival", "Date", "Time", "Capacity", "Price" };
        flightTableModel = new DefaultTableModel(cols, 0);
        flightTable = new JTable(flightTableModel);
        flightTable.setRowHeight(30);
        loadMockFlights();
        panel.add(new JScrollPane(flightTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(236, 240, 241));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnPanel.setOpaque(false);
        JButton addBtn = createStyledButton("Add Staff", new Color(46, 204, 113));
        addBtn.setPreferredSize(new Dimension(140, 40));
        addBtn.addActionListener(e -> addStaff());
        JButton editBtn = createStyledButton("Edit", new Color(52, 152, 219));
        editBtn.setPreferredSize(new Dimension(100, 40));
        editBtn.addActionListener(e -> editStaff());
        JButton delBtn = createStyledButton("Delete", new Color(231, 76, 60));
        delBtn.setPreferredSize(new Dimension(80, 40));
        delBtn.addActionListener(e -> deleteStaff());
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        panel.add(btnPanel, BorderLayout.NORTH);

        String[] cols = { "ID", "First Name", "Last Name", "Position", "Status" };
        staffTableModel = new DefaultTableModel(cols, 0);
        staffTable = new JTable(staffTableModel);
        staffTable.setRowHeight(30);
        loadMockStaff();
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadMockFlights() {
        flightTableModel.addRow(new Object[] { "TK101", "Istanbul", "Ankara", "06/01/2026", "08:00", 180, "1500 TL" });
        flightTableModel.addRow(new Object[] { "TK102", "Istanbul", "Ankara", "06/01/2026", "12:30", 180, "1350 TL" });
        flightTableModel.addRow(new Object[] { "TK201", "Ankara", "Izmir", "06/01/2026", "09:15", 150, "1200 TL" });
    }

    private void loadMockStaff() {
        staffTableModel.addRow(new Object[] { "STF001", "Ahmet", "Yilmaz", "Manager", "Active" });
        staffTableModel.addRow(new Object[] { "STF002", "Mehmet", "Demir", "Operations", "Active" });
        staffTableModel.addRow(new Object[] { "STF003", "Ayse", "Kaya", "Sales", "Active" });
    }

    private void addFlight() {
        JTextField no = new JTextField(), dep = new JTextField(), arr = new JTextField();
        JTextField date = new JTextField(), time = new JTextField(), price = new JTextField();
        JSpinner cap = new JSpinner(new SpinnerNumberModel(180, 50, 300, 10));
        Object[] f = { "Flight No:", no, "Departure:", dep, "Arrival:", arr, "Date:", date, "Time:", time, "Capacity:",
                cap,
                "Price:", price };
        if (JOptionPane.showConfirmDialog(this, f, "New Flight", JOptionPane.OK_CANCEL_OPTION) == 0) {
            flightTableModel.addRow(new Object[] { no.getText(), dep.getText(), arr.getText(), date.getText(),
                    time.getText(), cap.getValue(), price.getText() + " TL" });
        }
    }

    private void editFlight() {
        int r = flightTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight!");
            return;
        }
        JTextField no = new JTextField((String) flightTableModel.getValueAt(r, 0));
        JTextField dep = new JTextField((String) flightTableModel.getValueAt(r, 1));
        JTextField arr = new JTextField((String) flightTableModel.getValueAt(r, 2));
        JTextField date = new JTextField((String) flightTableModel.getValueAt(r, 3));
        JTextField time = new JTextField((String) flightTableModel.getValueAt(r, 4));
        JSpinner cap = new JSpinner(new SpinnerNumberModel((int) flightTableModel.getValueAt(r, 5), 50, 300, 10));
        JTextField price = new JTextField(((String) flightTableModel.getValueAt(r, 6)).replace(" TL", ""));
        Object[] f = { "Flight No:", no, "Departure:", dep, "Arrival:", arr, "Date:", date, "Time:", time, "Capacity:",
                cap,
                "Price:", price };
        if (JOptionPane.showConfirmDialog(this, f, "Edit Flight", JOptionPane.OK_CANCEL_OPTION) == 0) {
            flightTableModel.setValueAt(no.getText(), r, 0);
            flightTableModel.setValueAt(dep.getText(), r, 1);
            flightTableModel.setValueAt(arr.getText(), r, 2);
            flightTableModel.setValueAt(date.getText(), r, 3);
            flightTableModel.setValueAt(time.getText(), r, 4);
            flightTableModel.setValueAt(cap.getValue(), r, 5);
            flightTableModel.setValueAt(price.getText() + " TL", r, 6);
        }
    }

    private void deleteFlight() {
        int r = flightTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this flight?", "Confirm", JOptionPane.YES_NO_OPTION) == 0)
            flightTableModel.removeRow(r);
    }

    private void addStaff() {
        JTextField id = new JTextField(), firstName = new JTextField(), lastName = new JTextField(),
                pos = new JTextField();
        Object[] f = { "ID:", id, "First Name:", firstName, "Last Name:", lastName, "Position:", pos };
        if (JOptionPane.showConfirmDialog(this, f, "New Staff", JOptionPane.OK_CANCEL_OPTION) == 0) {
            staffTableModel.addRow(
                    new Object[] { id.getText(), firstName.getText(), lastName.getText(), pos.getText(), "Active" });
        }
    }

    private void editStaff() {
        int r = staffTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member!");
            return;
        }
        JTextField id = new JTextField((String) staffTableModel.getValueAt(r, 0));
        JTextField firstName = new JTextField((String) staffTableModel.getValueAt(r, 1));
        JTextField lastName = new JTextField((String) staffTableModel.getValueAt(r, 2));
        JTextField pos = new JTextField((String) staffTableModel.getValueAt(r, 3));
        JComboBox<String> status = new JComboBox<>(new String[] { "Active", "On Leave", "Inactive" });
        status.setSelectedItem(staffTableModel.getValueAt(r, 4));
        Object[] f = { "ID:", id, "First Name:", firstName, "Last Name:", lastName, "Position:", pos, "Status:",
                status };
        if (JOptionPane.showConfirmDialog(this, f, "Edit Staff", JOptionPane.OK_CANCEL_OPTION) == 0) {
            staffTableModel.setValueAt(id.getText(), r, 0);
            staffTableModel.setValueAt(firstName.getText(), r, 1);
            staffTableModel.setValueAt(lastName.getText(), r, 2);
            staffTableModel.setValueAt(pos.getText(), r, 3);
            staffTableModel.setValueAt(status.getSelectedItem(), r, 4);
        }
    }

    private void deleteStaff() {
        int r = staffTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this staff member?", "Confirm", JOptionPane.YES_NO_OPTION) == 0)
            staffTableModel.removeRow(r);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminScreen().setVisible(true));
    }
}
