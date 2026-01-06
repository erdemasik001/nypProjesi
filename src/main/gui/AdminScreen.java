package gui;

import service.FlightManager;
import service.SeatManager;
import service.StaffManager;
import model.flight.Flight;

import model.staff.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import thread.ReportGeneratorThread;

import java.util.List;

public class AdminScreen extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable flightTable;
    private DefaultTableModel flightTableModel;
    private JTable staffTable;
    private DefaultTableModel staffTableModel;
    private FlightManager flightManager;
    private SeatManager seatManager;
    private StaffManager staffManager;

    public AdminScreen() {
        this.flightManager = new FlightManager();
        this.staffManager = new StaffManager();
        this.seatManager = new SeatManager();

        // initializeMockData(); // REMOVED
        initializeUI();
        refreshFlightTable();
        refreshStaffTable();
    }

    // initializeMockData RESERVED for now but unused to avoid complicating map
    // logic with planes

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
        tabbedPane.addTab("📊 Reports", createReportsPanel());
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
        // loadMockFlights(); // Handled by refresh
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
        // loadMockStaff(); // Handled by refresh
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        return panel;
    }

    private void refreshFlightTable() {
        flightTableModel.setRowCount(0);
        List<Flight> flights = flightManager.getAllFlights();
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Flight f : flights) {
            flightTableModel.addRow(new Object[] {
                    f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(),
                    f.getDate().format(dtf), f.getHour(), f.getDuration(), String.format("%.2f TL", f.getPrice())
            });
        }
    }

    private void refreshStaffTable() {
        staffTableModel.setRowCount(0);
        List<Staff> staffList = staffManager.getAllStaff();
        for (Staff s : staffList) {
            staffTableModel.addRow(new Object[] {
                    s.getId(), s.getFirstName(), s.getLastName(), s.getPosition(), s.getStatus()
            });
        }
    }

    // Replace loadMockFlights and loadMockStaff calls in createPanel methods will
    // be handled by refresh calls

    private void addFlight() {
        JTextField no = new JTextField(), dep = new JTextField(), arr = new JTextField();
        JTextField date = new JTextField(), time = new JTextField(), price = new JTextField();
        // Capacity is fixed at 180
        Object[] f = { "Flight No:", no, "Departure:", dep, "Arrival:", arr, "Date:", date, "Time:", time,
                "Price:", price };
        if (JOptionPane.showConfirmDialog(this, f, "New Flight", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                // Parse date (allow d.M.yyyy or dd.MM.yyyy or yyyy-MM-dd)
                String dateStr = date.getText().trim();
                java.time.LocalDate localDate;
                try {
                    // Try European format with dots
                    localDate = java.time.LocalDate.parse(dateStr,
                            java.time.format.DateTimeFormatter.ofPattern("d.MM.yyyy"));
                } catch (Exception e1) {
                    try {
                        // Try European format with slashes
                        localDate = java.time.LocalDate.parse(dateStr,
                                java.time.format.DateTimeFormatter.ofPattern("d/MM/yyyy"));
                    } catch (Exception e2) {
                        // Fallback to default ISO
                        localDate = java.time.LocalDate.parse(dateStr);
                    }
                }

                // Parse time (allow HH:mm or HH.mm)
                String timeStr = time.getText().trim().replace(".", ":");
                if (timeStr.length() == 4 && timeStr.indexOf(':') == 1) {
                    timeStr = "0" + timeStr; // Pad 8:00 to 08:00 if needed for standard parsers, though LocalTime.parse
                                             // is usually smart enough specific patterns might be needed
                }
                java.time.LocalTime localTime = java.time.LocalTime.parse(timeStr);

                // Parse price
                double priceVal = Double.parseDouble(price.getText());

                flightManager.createFlight(no.getText(), dep.getText(), arr.getText(),
                        localDate, localTime,
                        180, priceVal);
                refreshFlightTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage()
                                + "\nPlease use format dd.MM.yyyy for date and HH:mm for time. Price must be a number.");
            }
        }
    }

    private void editFlight() {
        int r = flightTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight!");
            return;
        }
        String oldFlightNum = (String) flightTableModel.getValueAt(r, 0);

        JTextField no = new JTextField((String) flightTableModel.getValueAt(r, 0));
        JTextField dep = new JTextField((String) flightTableModel.getValueAt(r, 1));
        JTextField arr = new JTextField((String) flightTableModel.getValueAt(r, 2));
        JTextField date = new JTextField(flightTableModel.getValueAt(r, 3).toString());
        JTextField time = new JTextField(flightTableModel.getValueAt(r, 4).toString());
        // Capacity fixed at 180, not editable

        // Extract raw price from display string "1500.00 TL" -> "1500.00"
        String rawPrice = ((String) flightTableModel.getValueAt(r, 6)).replace(" TL", "").replace(",", ".");
        JTextField price = new JTextField(rawPrice);

        Object[] f = { "Flight No (Cannot Change):", no, "Departure:", dep, "Arrival:", arr, "Date (dd.MM.yyyy):", date,
                "Time:",
                time, "Price:", price };
        no.setEditable(false);

        if (JOptionPane.showConfirmDialog(this, f, "Edit Flight", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                // Parse date (allow d.M.yyyy or dd.MM.yyyy or yyyy-MM-dd)
                String dateStr = date.getText().trim();
                java.time.LocalDate localDate;
                try {
                    localDate = java.time.LocalDate.parse(dateStr,
                            java.time.format.DateTimeFormatter.ofPattern("d.MM.yyyy"));
                } catch (Exception e1) {
                    try {
                        localDate = java.time.LocalDate.parse(dateStr,
                                java.time.format.DateTimeFormatter.ofPattern("d/MM/yyyy"));
                    } catch (Exception e2) {
                        localDate = java.time.LocalDate.parse(dateStr);
                    }
                }

                // Parse time
                String timeStr = time.getText().trim().replace(".", ":");
                java.time.LocalTime localTime = java.time.LocalTime.parse(timeStr);

                // Parse price
                double priceVal = Double.parseDouble(price.getText());

                flightManager.updateFlight(oldFlightNum, dep.getText(), arr.getText(),
                        localDate, localTime,
                        180, priceVal);
                refreshFlightTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteFlight() {
        int r = flightTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight!");
            return;
        }
        String flightNum = (String) flightTableModel.getValueAt(r, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete this flight?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                flightManager.deleteFlight(flightNum);
                refreshFlightTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void addStaff() {
        JTextField id = new JTextField(), firstName = new JTextField(), lastName = new JTextField(),
                pos = new JTextField();
        Object[] f = { "ID:", id, "First Name:", firstName, "Last Name:", lastName, "Position:", pos };
        if (JOptionPane.showConfirmDialog(this, f, "New Staff", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                staffManager.addStaff(id.getText(), firstName.getText(), lastName.getText(), pos.getText(), "Active");
                refreshStaffTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void editStaff() {
        int r = staffTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member!");
            return;
        }
        String id = (String) staffTableModel.getValueAt(r, 0);
        String firstName = (String) staffTableModel.getValueAt(r, 1);
        String lastName = (String) staffTableModel.getValueAt(r, 2);
        String pos = (String) staffTableModel.getValueAt(r, 3);
        String statusVal = (String) staffTableModel.getValueAt(r, 4);

        JTextField idField = new JTextField(id);
        idField.setEditable(false);
        JTextField firstNameField = new JTextField(firstName);
        JTextField lastNameField = new JTextField(lastName);
        JTextField posField = new JTextField(pos);
        JComboBox<String> statusBox = new JComboBox<>(new String[] { "Active", "On Leave", "Inactive" });
        statusBox.setSelectedItem(statusVal);

        Object[] f = { "ID (Cannot Change):", idField, "First Name:", firstNameField, "Last Name:", lastNameField,
                "Position:", posField, "Status:", statusBox };

        if (JOptionPane.showConfirmDialog(this, f, "Edit Staff", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                staffManager.updateStaff(id, firstNameField.getText(), lastNameField.getText(), posField.getText(),
                        (String) statusBox.getSelectedItem());
                refreshStaffTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteStaff() {
        int r = staffTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member!");
            return;
        }
        String id = (String) staffTableModel.getValueAt(r, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete this staff member?", "Confirm",
                JOptionPane.YES_NO_OPTION) == 0) {
            try {
                staffManager.deleteStaff(id);
                refreshStaffTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(236, 240, 241));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("📊 Flight Occupancy Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(titleLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setText("Click 'Generate Report' to create a flight occupancy report.\n\n" +
                "This report will show:\n" +
                "  • Occupancy rate for all flights\n" +
                "  • Total seats vs occupied seats\n" +
                "  • Overall statistics\n\n" +
                "Note: Report generation runs in a separate thread to avoid blocking the GUI.");
        JScrollPane scrollPane = new JScrollPane(reportArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setOpaque(false);

        JButton generateBtn = createStyledButton("Generate Report", new Color(155, 89, 182));
        generateBtn.setPreferredSize(new Dimension(180, 45));
        generateBtn.addActionListener(e -> generateReport(reportArea, generateBtn));

        btnPanel.add(generateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void generateReport(JTextArea reportArea, JButton generateBtn) {
        generateBtn.setEnabled(false);

        ReportGeneratorThread reportThread = new ReportGeneratorThread(
                flightManager,
                seatManager,
                new ReportGeneratorThread.ReportCallback() {
                    @Override
                    public void onReportStarted() {
                        SwingUtilities.invokeLater(() -> {
                            reportArea.setText(
                                    "⏳ Preparing report...\n\nPlease wait while we calculate occupancy rates for all flights.\n"
                                            +
                                            "This process is running in a separate thread to keep the GUI responsive.");
                        });
                    }

                    @Override
                    public void onReportCompleted(String report) {
                        SwingUtilities.invokeLater(() -> {
                            reportArea.setText(report);
                            reportArea.setCaretPosition(0);
                            generateBtn.setEnabled(true);
                            JOptionPane.showMessageDialog(AdminScreen.this,
                                    "Report generated successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        });
                    }

                    @Override
                    public void onReportError(String error) {
                        SwingUtilities.invokeLater(() -> {
                            reportArea.setText("❌ Error generating report:\n\n" + error);
                            generateBtn.setEnabled(true);
                            JOptionPane.showMessageDialog(AdminScreen.this,
                                    "Error: " + error,
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        });
                    }
                });

        reportThread.start();
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
