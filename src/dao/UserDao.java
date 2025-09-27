package dao;

import domain.User;
import util.PasswordUtil;

import java.sql.*;

public class UserDao {

    private static UserDao instance;
    private UserDao() {}
    public static UserDao getInstance() {
        if (instance == null) instance = new UserDao();
        return instance;
    }

    public int insertPendingUser(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO User (uName, uId, userPw, uPhone, uEmail, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUName());
            ps.setString(2, user.getUId());
            ps.setString(3, PasswordUtil.hash(user.getUserPw()));
            ps.setString(4, user.getUPhone());
            ps.setString(5, user.getUEmail());
            ps.setString(6, "EXIST");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for user");
                }
            }
        }
    }

    public boolean existsByCredentials(Connection connection, String userId, String userPw) throws SQLException {
        String sql = "SELECT 1 FROM User WHERE uId = ? AND userPw = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, PasswordUtil.hash(userPw));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteUser(Connection connection, String userId) throws SQLException {
        String sql = "UPDATE User SET status = 'DELETED' WHERE uId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "SELECT uId FROM User WHERE uEmail = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("uId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String userId) throws SQLException {
        String sql = "SELECT userPw FROM User WHERE uId = ? AND status = 'EXIST' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("userPw");
                }
                return null;
            }
        }
    }

    public String resetPasswordById(Connection connection, String userId) throws SQLException {
        String tempPassword = util.PasswordUtil.generateTemporaryPassword();
        String hashedTempPassword = util.PasswordUtil.hash(tempPassword);
        
        String sql = "UPDATE User SET userPw = ? WHERE uId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedTempPassword);
            ps.setString(2, userId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                return tempPassword;
            }
            return null;
        }
    }

    public User getUserInfo(Connection connection, String userId) throws SQLException {
        String sql = "SELECT uIdx, uId, uEmail, uName, uPhone, createdAt FROM User WHERE uId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUIdx((rs.getInt("uIdx")));
                    user.setUId(rs.getString("uId"));
                    user.setUEmail(rs.getString("uEmail"));
                    user.setUName(rs.getString("uName"));
                    user.setUPhone(rs.getString("uPhone"));
                    user.setCreatedAt(rs.getTimestamp("createdAt"));
                    return user;
                }
                return null;
            }
        }
    }

    public boolean updateUserInfo(Connection connection, String userId, String name, String phone) throws SQLException {
        String sql = "UPDATE User SET uName = ?, uPhone = ? WHERE uId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUserPassword(Connection connection, String userId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "UPDATE User SET userPw = ? WHERE uId = ? AND status = 'EXIST'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}


