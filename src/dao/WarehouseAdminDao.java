package dao;

import domain.WarehouseAdmin;
import util.PasswordUtil;

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
            ps.setString(3, PasswordUtil.hash(admin.getWaPw()));
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

    public boolean existsByCredentials(Connection connection, String adminId, String adminPw) throws SQLException {
        String sql = "SELECT 1 FROM WarehouseAdmin WHERE waId = ? AND waPw = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.setString(2, PasswordUtil.hash(adminPw));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteWarehouseAdmin(Connection connection, String adminId) throws SQLException {
        String sql = "UPDATE WarehouseAdmin SET status = 'DELETED' WHERE waId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "SELECT waId FROM WarehouseAdmin WHERE waEmail = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("waId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT waPw FROM WarehouseAdmin WHERE waId = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("waPw");
                }
                return null;
            }
        }
    }

    public String resetPasswordById(Connection connection, String adminId) throws SQLException {
        String tempPassword = util.PasswordUtil.generateTemporaryPassword();
        String hashedTempPassword = util.PasswordUtil.hash(tempPassword);
        
        String sql = "UPDATE WarehouseAdmin SET waPw = ? WHERE waId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedTempPassword);
            ps.setString(2, adminId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                return tempPassword;
            }
            return null;
        }
    }

    public WarehouseAdmin getWarehouseAdminInfo(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT waIdx, waId, waEmail, waName, waPhone, createdAt FROM WarehouseAdmin WHERE waId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    WarehouseAdmin admin = new WarehouseAdmin();
                    admin.setWaIdx(rs.getInt("waIdx"));
                    admin.setWaId(rs.getString("waId"));
                    admin.setWaEmail(rs.getString("waEmail"));
                    admin.setWaName(rs.getString("waName"));
                    admin.setWaPhone(rs.getString("waPhone"));
                    admin.setCreatedAt(rs.getTimestamp("createdAt"));
                    return admin;
                }
                return null;
            }
        }
    }

    public boolean updateWarehouseAdminInfo(Connection connection, String adminId, String name, String phone) throws SQLException {
        String sql = "UPDATE WarehouseAdmin SET waName = ?, waPhone = ? WHERE waId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, adminId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateWarehouseAdminPassword(Connection connection, String adminId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "UPDATE WarehouseAdmin SET waPw = ? WHERE waId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, adminId);
            return ps.executeUpdate() > 0;
        }
    }
}


