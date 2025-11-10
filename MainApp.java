package ui;

import dao.FuelDAO;
import dao.VehicleDAO;
import model.FuelEntry;
import model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainApp extends JFrame {

    private JComboBox<Vehicle> vehicleBox;
    private JTextField dateField, litersField, costField, odoField;
    private JLabel efficiencyLabel;
    private JTable fuelTable;
    private DefaultTableModel tableModel;

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private FuelDAO fuelDAO = new FuelDAO();

    public MainApp() {
        setTitle("🚗 Vehicle Fuel Tracker");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL (Input Form) ---
        JPanel top = new JPanel(new GridLayout(7, 2, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("Add Fuel Entry"));

        vehicleBox = new JComboBox<>();
        loadVehicles();
        top.add(new JLabel("Select Vehicle:"));
        top.add(vehicleBox);

        dateField = new JTextField();
        litersField = new JTextField();
        costField = new JTextField();
        odoField = new JTextField();

        top.add(new JLabel("Date (YYYY-MM-DD):"));
        top.add(dateField);
        top.add(new JLabel("Liters:"));
        top.add(litersField);
        top.add(new JLabel("Cost (₹):"));
        top.add(costField);
        top.add(new JLabel("Odometer (km):"));
        top.add(odoField);

        JButton addBtn = new JButton("➕ Add Entry");
        JButton refreshBtn = new JButton("🔄 Refresh");

        top.add(addBtn);
        top.add(refreshBtn);

        efficiencyLabel = new JLabel("Fuel Efficiency: -- km/L");
        efficiencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        efficiencyLabel.setForeground(new Color(0, 128, 0));
        top.add(efficiencyLabel);

        add(top, BorderLayout.NORTH);

        // --- CENTER PANEL (Table) ---
        String[] cols = {"Date", "Liters", "Cost (₹)", "Odometer (km)"};
        tableModel = new DefaultTableModel(cols, 0);
        fuelTable = new JTable(tableModel);
        add(new JScrollPane(fuelTable), BorderLayout.CENTER);

        // --- ACTIONS ---
        addBtn.addActionListener(e -> addFuelEntry());
        refreshBtn.addActionListener(e -> loadEntries());

        loadEntries();
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles) {
            vehicleBox.addItem(v);
        }
    }

    private void addFuelEntry() {
        try {
            Vehicle selectedVehicle = (Vehicle) vehicleBox.getSelectedItem();
            if (selectedVehicle == null) {
                JOptionPane.showMessageDialog(this, "Select a vehicle!");
                return;
            }

            LocalDate date = LocalDate.parse(dateField.getText().trim());
            double liters = Double.parseDouble(litersField.getText().trim());
            double cost = Double.parseDouble(costField.getText().trim());
            double odo = Double.parseDouble(odoField.getText().trim());

            FuelEntry entry = new FuelEntry(0, selectedVehicle.getVehicleId(), date, liters, cost, odo);
            fuelDAO.addFuelEntry(entry);

            JOptionPane.showMessageDialog(this, "✅ Entry added successfully!");
            clearFields();
            loadEntries();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Invalid input: " + ex.getMessage());
        }
    }

    private void loadEntries() {
        tableModel.setRowCount(0);
        Vehicle selectedVehicle = (Vehicle) vehicleBox.getSelectedItem();
        if (selectedVehicle == null) return;

        List<FuelEntry> entries = fuelDAO.getEntriesByVehicle(selectedVehicle.getVehicleId());
        FuelEntry prev = null;
        double totalKm = 0, totalLiters = 0;

        for (FuelEntry f : entries) {
            tableModel.addRow(new Object[]{f.getDate(), f.getLiters(), f.getCost(), f.getOdometer()});

            if (prev != null) {
                totalKm += f.getOdometer() - prev.getOdometer();
                totalLiters += f.getLiters();
            }
            prev = f;
        }

        if (totalLiters > 0) {
            double eff = totalKm / totalLiters;
            efficiencyLabel.setText(String.format("Fuel Efficiency: %.2f km/L", eff));
        } else {
            efficiencyLabel.setText("Fuel Efficiency: -- km/L");
        }
    }

    private void clearFields() {
        dateField.setText("");
        litersField.setText("");
        costField.setText("");
        odoField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}
