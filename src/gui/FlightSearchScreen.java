package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import service.ReservationManager;
import model.flight.*;
import model.reservation.*;
import java.time.LocalDate;

public class FlightSearchScreen extends JFrame {

    private JComboBox<String> departureCombo;
    private JComboBox<String> arrivalCombo;
    private JSpinner dateSpinner;
    private JTable flightTable;
    private DefaultTableModel tableModel;

    // City list
    private String[] cities = { "Istanbul", "Ankara", "Izmir", "Antalya", "Trabzon", "Adana", "Bursa", "Konya" };

    private ReservationManager reservationManager;
    private service.FlightManager flightManager;

    // Buttons
    private JButton selectSeatBtn;
    private JButton reserveBtn;
    private JButton cancelBtn;
    private JButton backBtn;

    // Temporary Passenger Data
    private String tempName;
    private String tempSurname;
    private Flight tempFlight;

    public FlightSearchScreen() {
        this.reservationManager = new ReservationManager();
        this.flightManager = new service.FlightManager();
        initializeUI();
        // loadMockFlights(); // REMOVED
    }

    private void initializeUI() {
        setTitle("Flight Search and Booking");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ... (rest of UI setup) ...
        // Main panel - gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(41, 128, 185),
                        0, getHeight(), new Color(109, 213, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("✈ Flight Search", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.WEST);

        // Results panel
        JPanel resultsPanel = createResultsPanel();
        mainPanel.add(resultsPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load all flights initially
        loadAllFlights();
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 400));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Departure city
        JLabel depLabel = new JLabel("Departure City:");
        depLabel.setForeground(Color.WHITE);
        depLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        depLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(depLabel);
        panel.add(Box.createVerticalStrut(5));

        departureCombo = new JComboBox<>(cities);
        departureCombo.setMaximumSize(new Dimension(220, 35));
        departureCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(departureCombo);
        panel.add(Box.createVerticalStrut(15));

        // Arrival city
        JLabel arrLabel = new JLabel("Arrival City:");
        arrLabel.setForeground(Color.WHITE);
        arrLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        arrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(arrLabel);
        panel.add(Box.createVerticalStrut(5));

        arrivalCombo = new JComboBox<>(cities);
        arrivalCombo.setSelectedIndex(1);
        arrivalCombo.setMaximumSize(new Dimension(220, 35));
        arrivalCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(arrivalCombo);
        panel.add(Box.createVerticalStrut(15));

        // Date
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(5));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setMaximumSize(new Dimension(220, 35));
        dateSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateSpinner);
        panel.add(Box.createVerticalStrut(25));

        // Search button
        JButton searchBtn = createStyledButton("Search Flights", new Color(46, 204, 113));
        searchBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchBtn.setMaximumSize(new Dimension(220, 45));
        searchBtn.addActionListener(e -> searchFlights());
        panel.add(searchBtn);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel resultsLabel = new JLabel("Flight Results");
        resultsLabel.setForeground(Color.WHITE);
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(resultsLabel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Flight No", "Departure", "Arrival", "Date", "Time", "Price", "Available Seats" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        flightTable = new JTable(tableModel);
        flightTable.setRowHeight(30);
        flightTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        flightTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        selectSeatBtn = createStyledButton("Select Seat", new Color(52, 152, 219));
        selectSeatBtn.setPreferredSize(new Dimension(150, 45));
        selectSeatBtn.addActionListener(e -> openSeatSelection());
        selectSeatBtn.setVisible(false); // Initially hidden
        panel.add(selectSeatBtn);

        reserveBtn = createStyledButton("Make Reservation", new Color(155, 89, 182));
        reserveBtn.setPreferredSize(new Dimension(180, 45));
        reserveBtn.addActionListener(e -> initiateReservationProcess());
        panel.add(reserveBtn);

        cancelBtn = createStyledButton("Cancel", new Color(231, 76, 60));
        cancelBtn.setPreferredSize(new Dimension(120, 45));
        cancelBtn.addActionListener(e -> cancelReservationProcess());
        cancelBtn.setVisible(false); // Initially hidden
        panel.add(cancelBtn);

        backBtn = createStyledButton("Go Back", new Color(149, 165, 166));
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.addActionListener(e -> dispose());
        panel.add(backBtn);

        return panel;
    }

    private void loadAllFlights() {
        tableModel.setRowCount(0);
        java.util.List<Flight> allFlights = flightManager.getAllFlights();
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Flight f : allFlights) {
            int reservedCount = reservationManager.getReservedSeatCountForFlight(f.getFlightNum());
            int availableSeats = 180 - reservedCount;
            tableModel.addRow(new Object[] {
                    f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(),
                    f.getDate().format(dtf), f.getHour(), String.format("%.2f TL", f.getPrice()), availableSeats
            });
        }
    }

    private void searchFlights() {
        String departure = (String) departureCombo.getSelectedItem();
        String arrival = (String) arrivalCombo.getSelectedItem();

        if (departure.equals(arrival)) {
            JOptionPane.showMessageDialog(this,
                    "Departure and arrival cities cannot be the same!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Convert Spinner Date to LocalDate
        Date spinnerDate = (Date) dateSpinner.getValue();
        LocalDate searchDate = spinnerDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        java.util.List<Flight> allFlights = flightManager.getAllFlights();
        int foundCount = 0;
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Flight f : allFlights) {
            boolean routeMatch = f.getDeparturePlace().equalsIgnoreCase(departure) &&
                    f.getArrivalPlace().equalsIgnoreCase(arrival);
            boolean dateMatch = f.getDate().equals(searchDate);

            if (routeMatch && dateMatch) {
                int reservedCount = reservationManager.getReservedSeatCountForFlight(f.getFlightNum());
                int availableSeats = 180 - reservedCount;
                tableModel.addRow(new Object[] {
                        f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(),
                        f.getDate().format(dtf), f.getHour(), String.format("%.2f TL", f.getPrice()), availableSeats
                });
                foundCount++;
            }
        }

        if (foundCount == 0) {
            JOptionPane.showMessageDialog(this, "No flights found for this route and date.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            // Optionally reload all flights or leave empty? User usually expects filtered
            // list even if empty.
            // Leaving it empty is correct for a "search result".
        } else {
            JOptionPane.showMessageDialog(this, foundCount + " flights found.", "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initiateReservationProcess() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a flight!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get passenger info
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();

        Object[] fields = {
                "First Name:", nameField,
                "Last Name:", surnameField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Passenger Information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "First name and last name are required!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Store Info Temporarily
            this.tempName = name;
            this.tempSurname = surname;

            // Fetch flight details
            String flightNo = (String) tableModel.getValueAt(selectedRow, 0);
            this.tempFlight = flightManager.getFlight(flightNo);

            // Update UI State
            flightTable.setEnabled(false); // Lock selection
            reserveBtn.setVisible(false);
            backBtn.setVisible(false);
            selectSeatBtn.setVisible(true);
            cancelBtn.setVisible(true);
        }
    }

    private void cancelReservationProcess() {
        // Reset UI State
        tempName = null;
        tempSurname = null;
        tempFlight = null;

        flightTable.setEnabled(true);
        selectSeatBtn.setVisible(false);
        cancelBtn.setVisible(false);
        reserveBtn.setVisible(true);
        backBtn.setVisible(true);
    }

    private void openSeatSelection() {
        if (tempFlight == null || tempName == null) {
            JOptionPane.showMessageDialog(this, "Process Error. Please restart.", "Error", JOptionPane.ERROR_MESSAGE);
            cancelReservationProcess();
            return;
        }

        String passengerId = "PID" + System.currentTimeMillis() % 10000;
        Passenger passenger = new Passenger(passengerId, tempName, tempSurname, "Unknown");

        SeatReservationPanel seatPanel = new SeatReservationPanel(
                tempFlight.getFlightNum(),
                reservationManager,
                passenger,
                () -> {
                    // On Success Callback
                    JOptionPane.showMessageDialog(this, "Reservation process completed.");
                    loadAllFlights(); // Refresh table (seats count)
                    cancelReservationProcess(); // Reset UI
                });
        seatPanel.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(backgroundColor.brighter());
                } else {
                    g2d.setColor(backgroundColor);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlightSearchScreen().setVisible(true);
        });
    }
}
