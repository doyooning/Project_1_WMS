package dao;

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
        String sql = "SELECT 1 FROM TotalAdmin WHERE taId = ? AND taPw = ? AND status = '총관리자' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            ps.setString(2, PasswordUtil.hash(adminPw));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
