package dao;
import model.Vehicle;
import java.sql.*;
import java.util.*;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("name"),
                        rs.getString("fuel_type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
