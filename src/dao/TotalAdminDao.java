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
        String sql = "{CALL CheckTotalAdminCredentials(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            cs.setString(2, PasswordUtil.hash(adminPw));
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertTotalAdmin(Connection connection, String taName, String taId, String taPw, String taPhone, String taEmail) throws SQLException {
        String sql = "{CALL InsertTotalAdmin(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, taName);
            cs.setString(2, taId);
            cs.setString(3, PasswordUtil.hash(taPw));
            cs.setString(4, taPhone);
            cs.setString(5, taEmail);
            cs.setString(6, "EXIST");
            cs.executeUpdate();

            // 프로시저에서 생성된 키를 반환받기 위해 별도 쿼리 실행
            String getLastIdSql = "SELECT LAST_INSERT_ID()";
            try (PreparedStatement ps = connection.prepareStatement(getLastIdSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for total admin");
                }
            }
        }
    }

    public void deleteTotalAdmin(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL DeleteTotalAdmin(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            cs.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "{CALL FindTotalAdminIdByEmail(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, email);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("taId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL FindTotalAdminPasswordById(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
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
        
        String sql = "{CALL ResetTotalAdminPassword(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, hashedTempPassword);
            cs.setString(2, adminId);
            int updated = cs.executeUpdate();
            if (updated > 0) {
                return tempPassword;
            }
            return null;
        }
    }

    public TotalAdmin getTotalAdminInfo(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL GetTotalAdminInfo(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    TotalAdmin admin = new TotalAdmin();
                    admin.setTaIdx(rs.getInt("taIdx"));
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
        String sql = "{CALL UpdateTotalAdminInfo(?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, name);
            cs.setString(2, phone);
            cs.setString(3, adminId);
            return cs.executeUpdate() > 0;
        }
    }

    public boolean updateTotalAdminPassword(Connection connection, String adminId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "{CALL UpdateTotalAdminPassword(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, hashedPassword);
            cs.setString(2, adminId);
            return cs.executeUpdate() > 0;
        }
    }
}
