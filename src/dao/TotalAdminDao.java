package dao;

import domain.TotalAdmin;
import util.PasswordUtil;

import java.sql.*;

public class TotalAdminDao {

    private static TotalAdminDao instance;
    private TotalAdminDao() {}
    public static TotalAdminDao getInstance() {
        if (instance == null) instance = new TotalAdminDao();
        return instance;
    }

    public boolean existsByCredentials(Connection connection, String adminId, String adminPw) throws SQLException {
        String sql = "SELECT 1 FROM TotalAdmin WHERE taId = ? AND taPw = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.setString(2, PasswordUtil.hash(adminPw));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertTotalAdmin(Connection connection, String taName, String taId, String taPw, String taPhone, String taEmail) throws SQLException {
        String sql = "INSERT INTO TotalAdmin (taName, taId, taPw, taPhone, taEmail, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, taName);
            ps.setString(2, taId);
            ps.setString(3, PasswordUtil.hash(taPw));
            ps.setString(4, taPhone);
            ps.setString(5, taEmail);
            ps.setString(6, "EXIST");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for total admin");
                }
            }
        }
    }

    public void deleteTotalAdmin(Connection connection, String adminId) throws SQLException {
        String sql = "UPDATE TotalAdmin SET status = 'DELETED' WHERE taId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "SELECT taId FROM TotalAdmin WHERE taEmail = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("taId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT taPw FROM TotalAdmin WHERE taId = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("taPw");
                }
                return null;
            }
        }
    }

    public String resetPasswordById(Connection connection, String adminId) throws SQLException {
        String tempPassword = util.PasswordUtil.generateTemporaryPassword();
        String hashedTempPassword = util.PasswordUtil.hash(tempPassword);
        
        String sql = "UPDATE TotalAdmin SET taPw = ? WHERE taId = ? AND status = 'EXIST'";
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

    public TotalAdmin getTotalAdminInfo(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT taIdx, taId, taEmail, taName, taPhone, createdAt FROM TotalAdmin WHERE taId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TotalAdmin admin = new TotalAdmin();
                    admin.setTaIdx((rs.getInt("taIdx")));
                    admin.setTaId(rs.getString("taId"));
                    admin.setTaEmail(rs.getString("taEmail"));
                    admin.setTaName(rs.getString("taName"));
                    admin.setTaPhone(rs.getString("taPhone"));
                    admin.setCreatedAt(rs.getTimestamp("createdAt"));
                    return admin;
                }
                return null;
            }
        }
    }

    public boolean updateTotalAdminInfo(Connection connection, String adminId, String name, String phone) throws SQLException {
        String sql = "UPDATE TotalAdmin SET taName = ?, taPhone = ? WHERE taId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, adminId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateTotalAdminPassword(Connection connection, String adminId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "UPDATE TotalAdmin SET taPw = ? WHERE taId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, adminId);
            return ps.executeUpdate() > 0;
        }
    }
}
