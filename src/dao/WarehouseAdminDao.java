package dao;

import domain.WarehouseAdmin;

import java.sql.*;

public class WarehouseAdminDao {

    private static WarehouseAdminDao instance;
    private WarehouseAdminDao() {}
    public static WarehouseAdminDao getInstance() {
        if (instance == null) instance = new WarehouseAdminDao();
        return instance;
    }

    public int insertPendingWarehouseAdmin(Connection connection, WarehouseAdmin admin) throws SQLException {
        String sql = "INSERT INTO WarehouseAdmin (waName, waId, waPw, waPhone, waEmail, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, admin.getWaName());
            ps.setString(2, admin.getWaId());
            ps.setString(3, admin.getWaPw());
            ps.setString(4, admin.getWaPhone());
            ps.setString(5, admin.getWaEmail());
            ps.setString(6, "EXIST");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for warehouse admin");
                }
            }
        }
    }
}


