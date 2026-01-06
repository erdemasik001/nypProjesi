package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import service.ReservationManager;

public class ReservationManagementScreen extends JFrame {

    private ReservationManager reservationManager;

    private JTextField searchField;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ReservationManagementScreen() {
        this.reservationManager = new ReservationManager();
        initializeUI();
        refreshReservations();
    }

    private void initializeUI() {
        setTitle("Reservation Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(142, 68, 173), 0, getHeight(),
                        new Color(192, 132, 224));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("📋 Reservation Management", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Reservation Code:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(searchLabel);
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchField);
        JButton searchBtn = createStyledButton("Search", new Color(52, 152, 219));
        searchBtn.setPreferredSize(new Dimension(80, 35));
        searchBtn.addActionListener(e -> searchReservation());
        searchPanel.add(searchBtn);
        JButton showAllBtn = createStyledButton("Show All", new Color(46, 204, 113));
        showAllBtn.setPreferredSize(new Dimension(130, 35));
        showAllBtn.addActionListener(e -> refreshReservations());
        searchPanel.add(showAllBtn);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        String[] columns = { "Reservation Code", "Flight No", "Passenger Name", "Departure", "Arrival", "Date", "Seat",
                "Price",
                "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);
        reservationTable.setRowHeight(35);
        reservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        JButton detailBtn = createStyledButton("View Details", new Color(52, 152, 219));
        detailBtn.setPreferredSize(new Dimension(160, 45));
        detailBtn.addActionListener(e -> showReservationDetails());
        panel.add(detailBtn);
        JButton cancelBtn = createStyledButton("Cancel Reservation", new Color(231, 76, 60));
        cancelBtn.setPreferredSize(new Dimension(180, 45));
        cancelBtn.addActionListener(e -> cancelReservation());
        panel.add(cancelBtn);
        JButton backBtn = createStyledButton("Go Back", new Color(149, 165, 166));
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addActionListener(e -> dispose());
        panel.add(backBtn);
        return panel;
    }

    private void refreshReservations() {
        tableModel.setRowCount(0);
        java.util.List<model.reservation.Reservation> reservations = reservationManager.getAllReservations();

        for (model.reservation.Reservation r : reservations) {
            Object[] row = {
                    r.getReservationCode(),
                    r.getFlight() != null ? r.getFlight().getFlightNum() : "N/A",
                    r.getPassenger() != null ? r.getPassenger().getName() + " " + r.getPassenger().getSurname() : "N/A",
                    r.getFlight() != null ? r.getFlight().getDeparturePlace() : "N/A",
                    r.getFlight() != null ? r.getFlight().getArrivalPlace() : "N/A",
                    r.getDateOfReservation() != null
                            ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getDateOfReservation())
                            : "N/A",
                    r.getSeat() != null ? r.getSeat().getSeatNum() : "N/A",
                    (r.getSeat() != null)
                            ? (String.format("%.2f TL%s", r.getSeat().getPrice(),
                                    (r.getSeat().getSeatClass() == model.flight.SeatClass.BUSINESS ? " (B)" : "")))
                            : "0.00 TL",
                    r.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void searchReservation() {
        String searchCode = searchField.getText().trim().toUpperCase();
        if (searchCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a reservation code!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (((String) tableModel.getValueAt(i, 0)).equalsIgnoreCase(searchCode)) {
                reservationTable.setRowSelectionInterval(i, i);
                reservationTable.scrollRectToVisible(reservationTable.getCellRect(i, 0, true));
                found = true;
                break;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this,
                    "Reservation not found: " + searchCode,
                    "Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showReservationDetails() {
        int r = reservationTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String details = String.format(
                "RESERVATION DETAILS\n\nCode: %s\nStatus: %s\n\nFLIGHT INFO\nFlight: %s\nFrom: %s\nTo: %s\nDate: %s\nSeat: %s\n\nPassenger: %s",
                tableModel.getValueAt(r, 0), tableModel.getValueAt(r, 7), tableModel.getValueAt(r, 1),
                tableModel.getValueAt(r, 3), tableModel.getValueAt(r, 4), tableModel.getValueAt(r, 5),
                tableModel.getValueAt(r, 6), tableModel.getValueAt(r, 2));
        JOptionPane.showMessageDialog(this, details,
                "Reservation Details", JOptionPane.PLAIN_MESSAGE);
    }

    private void cancelReservation() {
        int r = reservationTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getValueAt(r, 7).equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "Already cancelled!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String code = (String) tableModel.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reservation will be cancelled: " + code + "\n\nAre you sure?",
                "Cancellation Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String flightNum = (String) tableModel.getValueAt(r, 1);
            String seatNum = (String) tableModel.getValueAt(r, 6);

            reservationManager.cancelReservation(code);
            new service.FlightManager().updateSeatStatus(flightNum, seatNum, false);

            refreshReservations();

            JOptionPane.showMessageDialog(this,
                    "Reservation cancelled successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.brighter() : bg);
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
        SwingUtilities.invokeLater(() -> new ReservationManagementScreen().setVisible(true));
    }
}
