package dao;

import domain.EntityStatus;
import domain.PendingUserApproval;
import domain.PendingWarehouseAdminApproval;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApprovalDao {
    private static ApprovalDao instance;
    private ApprovalDao() {}
    public static ApprovalDao getInstance() {
        if (instance == null) instance = new ApprovalDao();
        return instance;
    }

    public List<PendingUserApproval> findPendingUsers(Connection connection) throws SQLException {
        String sql = "SELECT a.aIdx, u.uIdx, u.uName, u.uPhone, u.uEmail, u.createdAt " +
                "FROM Approval a JOIN User u ON a.uIdx = u.uIdx " +
                "WHERE a.status = 'PENDING' AND a.uIdx IS NOT NULL AND a.waIdx IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<PendingUserApproval> list = new ArrayList<>();
            while (rs.next()) {
                PendingUserApproval p = new PendingUserApproval();
                p.setAIdx(rs.getInt("aIdx"));
                p.setUIdx(rs.getInt("uIdx"));
                p.setUName(rs.getString("uName"));
                p.setUPhone(rs.getString("uPhone"));
                p.setUEmail(rs.getString("uEmail"));
                p.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(p);
            }
            return list;
        }
    }

    public List<PendingWarehouseAdminApproval> findPendingWarehouseAdmins(Connection connection) throws SQLException {
        String sql = "SELECT a.aIdx, w.waIdx, w.waName, w.waPhone, w.waEmail, w.createdAt " +
                "FROM Approval a JOIN WarehouseAdmin w ON a.waIdx = w.waIdx " +
                "WHERE a.status = 'PENDING' AND a.uIdx IS NULL AND a.waIdx IS NOT NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<PendingWarehouseAdminApproval> list = new ArrayList<>();
            while (rs.next()) {
                PendingWarehouseAdminApproval p = new PendingWarehouseAdminApproval();
                p.setAIdx(rs.getInt("aIdx"));
                p.setWaIdx(rs.getInt("waIdx"));
                p.setWaName(rs.getString("waName"));
                p.setWaPhone(rs.getString("waPhone"));
                p.setWaEmail(rs.getString("waEmail"));
                p.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(p);
            }
            return list;
        }
    }

    public void updateApprovalStatus(Connection connection, int aIdx, String status, Integer taIdx) throws SQLException {
        String sql = "UPDATE Approval SET status = ?, taIdx = ? WHERE aIdx = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            if (taIdx == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, taIdx);
            }
            ps.setInt(3, aIdx);
            ps.executeUpdate();
        }
    }
    public void insertApprovalForUser(Connection connection, int userIdx, int totalAdminIdx, EntityStatus status) throws SQLException {
        // taIdx는 승인 전까지 NULL
        String sql = "INSERT INTO Approval (uIdx, status) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userIdx);
            ps.setString(2, status.name());
            ps.executeUpdate();
        }
    }

    public void insertApprovalForWarehouse(Connection connection, int warehouseAdminIdx, int totalAdminIdx, EntityStatus status) throws SQLException {
        // taIdx는 승인 전까지 NULL
        String sql = "INSERT INTO Approval (waIdx, status) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseAdminIdx);
            ps.setString(2, status.name());
            ps.executeUpdate();
        }
    }

    // Approval 체크: 일반회원 승인 여부 (userId 기반)
    public boolean isUserApproved(Connection connection, String userId) throws SQLException {
        String sql = "SELECT 1 FROM Approval a JOIN User u ON a.uIdx = u.uIdx " +
                "WHERE u.uId = ? AND a.status = 'APPROVED' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Approval 체크: 창고관리자 승인 여부 (waId 기반)
    public boolean isWarehouseAdminApproved(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT 1 FROM Approval a JOIN WarehouseAdmin w ON a.waIdx = w.waIdx " +
                "WHERE w.waId = ? AND a.status = 'APPROVED' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // 단일 상태 조회: 일반회원(userId)
    public String getUserApprovalStatus(Connection connection, String userId) throws SQLException {
        String sql = "SELECT a.status FROM Approval a JOIN User u ON a.uIdx = u.uIdx " +
                "WHERE u.uId = ? ORDER BY a.aIdx DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        }
    }

    // 단일 상태 조회: 창고관리자(waId)
    public String getWarehouseAdminApprovalStatus(Connection connection, String adminId) throws SQLException {
        String sql = "SELECT a.status FROM Approval a JOIN WarehouseAdmin w ON a.waIdx = w.waIdx " +
                "WHERE w.waId = ? ORDER BY a.aIdx DESC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        }
    }
}

