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
}


