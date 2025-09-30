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
        String sql = "{CALL InsertUser(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, user.getUName());
            cs.setString(2, user.getUId());
            cs.setString(3, PasswordUtil.hash(user.getUserPw()));
            cs.setString(4, user.getUPhone());
            cs.setString(5, user.getUEmail());
            cs.setString(6, "EXIST");
            cs.executeUpdate();

            // 프로시저에서 생성된 키를 반환받기 위해 별도 쿼리 실행
            String getLastIdSql = "SELECT LAST_INSERT_ID()";
            try (PreparedStatement ps = connection.prepareStatement(getLastIdSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated key for user");
                }
            }
        }
    }

    public boolean existsByCredentials(Connection connection, String userId, String userPw) throws SQLException {
        String sql = "{CALL CheckUserCredentials(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            cs.setString(2, PasswordUtil.hash(userPw));
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteUser(Connection connection, String userId) throws SQLException {
        String sql = "{CALL DeleteUser(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            cs.executeUpdate();
        }
    }

    public String findIdByEmail(Connection connection, String email) throws SQLException {
        String sql = "{CALL FindUserIdByEmail(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, email);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("uId");
                }
                return null;
            }
        }
    }

    public String findPasswordById(Connection connection, String userId) throws SQLException {
        String sql = "{CALL FindUserPasswordById(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            try (ResultSet rs = cs.executeQuery()) {
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
        
        String sql = "{CALL ResetUserPassword(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, hashedTempPassword);
            cs.setString(2, userId);
            int updated = cs.executeUpdate();
            if (updated > 0) {
                return tempPassword;
            }
            return null;
        }
    }

    public User getUserInfo(Connection connection, String userId) throws SQLException {
        String sql = "{CALL GetUserInfo(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUIdx(rs.getInt("uIdx"));
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
        String sql = "{CALL UpdateUserInfo(?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, name);
            cs.setString(2, phone);
            cs.setString(3, userId);
            return cs.executeUpdate() > 0;
        }
    }

    public boolean updateUserPassword(Connection connection, String userId, String newPassword) throws SQLException {
        String hashedPassword = util.PasswordUtil.hash(newPassword);
        String sql = "{CALL UpdateUserPassword(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, hashedPassword);
            cs.setString(2, userId);
            return cs.executeUpdate() > 0;
        }
    }
}


