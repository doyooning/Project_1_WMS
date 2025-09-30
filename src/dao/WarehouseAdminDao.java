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
        String sql = "{CALL InsertWarehouseAdmin(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, admin.getWaName());
            cs.setString(2, admin.getWaId());
            cs.setString(3, PasswordUtil.hash(admin.getWaPw()));
            cs.setString(4, admin.getWaPhone());
            cs.setString(5, admin.getWaEmail());
            cs.setString(6, "EXIST");
            cs.executeUpdate();

            // 프로시저에서 생성된 키를 반환받기 위해 별도 쿼리 실행
            String getLastIdSql = "SELECT LAST_INSERT_ID()";
            try (PreparedStatement ps = connection.prepareStatement(getLastIdSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for warehouse admin");
                }
            }
        }
    }

    public boolean existsByCredentials(Connection connection, String adminId, String adminPw) throws SQLException {
        String sql = "{CALL CheckWarehouseAdminCredentials(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            cs.setString(2, PasswordUtil.hash(adminPw));
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteWarehouseAdmin(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL DeleteWarehouseAdmin(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            cs.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "{CALL FindWarehouseAdminIdByEmail(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, email);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("waId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL FindWarehouseAdminPasswordById(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
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
        
        String sql = "{CALL ResetWarehouseAdminPassword(?, ?)}";
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

    public WarehouseAdmin getWarehouseAdminInfo(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL GetWarehouseAdminInfo(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
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
        String sql = "{CALL UpdateWarehouseAdminInfo(?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, name);
            cs.setString(2, phone);
            cs.setString(3, adminId);
            return cs.executeUpdate() > 0;
        }
    }

    public boolean updateWarehouseAdminPassword(Connection connection, String adminId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "{CALL UpdateWarehouseAdminPassword(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, hashedPassword);
            cs.setString(2, adminId);
            return cs.executeUpdate() > 0;
        }
    }
}


