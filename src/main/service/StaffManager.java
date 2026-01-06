package service;

import model.staff.Staff;
import util.FileManager;
import java.util.*;

public class StaffManager {
    private static final String STAFF_FILE = "staff.txt";
    private Map<String, Staff> staffMap;

    public StaffManager() {
        this.staffMap = new HashMap<>();
        loadStaff();
    }

    public void addStaff(String id, String firstName, String lastName, String position, String status) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        if (staffMap.containsKey(id)) {
            throw new IllegalArgumentException("Staff with ID " + id + " already exists");
        }
        Staff staff = new Staff(id, firstName, lastName, position, status);
        staffMap.put(id, staff);
        saveStaff();
    }

    public void updateStaff(String id, String firstName, String lastName, String position, String status) {
        Staff staff = staffMap.get(id);
        if (staff == null) {
            throw new IllegalArgumentException("Staff with ID " + id + " not found");
        }
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setPosition(position);
        staff.setStatus(status);
        saveStaff();
    }

    public void deleteStaff(String id) {
        if (!staffMap.containsKey(id)) {
            throw new IllegalArgumentException("Staff with ID " + id + " not found");
        }
        staffMap.remove(id);
        saveStaff();
    }

    public Staff getStaff(String id) {
        return staffMap.get(id);
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMap.values());
    }

    private void saveStaff() {
        List<String> lines = new ArrayList<>();
        for (Staff s : staffMap.values()) {
            // CSV format: id,firstName,lastName,position,status
            String line = String.format("%s,%s,%s,%s,%s",
                    s.getId(), s.getFirstName(), s.getLastName(), s.getPosition(), s.getStatus());
            lines.add(line);
        }
        FileManager.writeLines(FileManager.getDataFilePath(STAFF_FILE), lines, false);
    }

    private void loadStaff() {
        List<String> lines = FileManager.readLines(FileManager.getDataFilePath(STAFF_FILE));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String id = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                String position = parts[3];
                String status = parts[4];
                Staff staff = new Staff(id, firstName, lastName, position, status);
                staffMap.put(id, staff);
            }
        }
    }
}
