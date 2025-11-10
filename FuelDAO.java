package dao;
import model.FuelEntry;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class FuelDAO {

    public void addFuelEntry(FuelEntry entry) {
        String sql = "INSERT INTO fuel_entries(vehicle_id, date, liters, cost, odometer) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entry.getVehicleId());
            ps.setDate(2, Date.valueOf(entry.getDate()));
            ps.setDouble(3, entry.getLiters());
            ps.setDouble(4, entry.getCost());
            ps.setDouble(5, entry.getOdometer());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<FuelEntry> getEntriesByVehicle(int vehicleId) {
        List<FuelEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM fuel_entries WHERE vehicle_id=? ORDER BY date";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new FuelEntry(
                        rs.getInt("entry_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getDouble("liters"),
                        rs.getDouble("cost"),
                        rs.getDouble("odometer")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
