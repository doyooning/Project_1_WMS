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
        String sql = "{CALL GetPendingUsers()}";
        try (CallableStatement cs = connection.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
        String sql = "{CALL GetPendingWarehouseAdmins()}";
        try (CallableStatement cs = connection.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
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
        String sql = "{CALL UpdateApprovalStatus(?, ?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, aIdx);
            cs.setString(2, status);
            if (taIdx == null) {
                cs.setNull(3, Types.INTEGER);
            } else {
                cs.setInt(3, taIdx);
            }
            cs.executeUpdate();
        }
    }
    public void insertApprovalForUser(Connection connection, int userIdx, int totalAdminIdx, EntityStatus status) throws SQLException {
        String sql = "{CALL InsertApprovalForUser(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, userIdx);
            cs.setString(2, status.name());
            cs.executeUpdate();
        }
    }

    public void insertApprovalForWarehouse(Connection connection, int warehouseAdminIdx, int totalAdminIdx, EntityStatus status) throws SQLException {
        String sql = "{CALL InsertApprovalForWarehouse(?, ?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, warehouseAdminIdx);
            cs.setString(2, status.name());
            cs.executeUpdate();
        }
    }

    // Approval 체크: 일반회원 승인 여부 (userId 기반)
    public boolean isUserApproved(Connection connection, String userId) throws SQLException {
        String sql = "{CALL IsUserApproved(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Approval 체크: 창고관리자 승인 여부 (waId 기반)
    public boolean isWarehouseAdminApproved(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL IsWarehouseAdminApproved(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next();
            }
        }
    }

    // 단일 상태 조회: 일반회원(userId)
    public String getUserApprovalStatus(Connection connection, String userId) throws SQLException {
        String sql = "{CALL GetUserApprovalStatus(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, userId);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        }
    }

    // 단일 상태 조회: 창고관리자(waId)
    public String getWarehouseAdminApprovalStatus(Connection connection, String adminId) throws SQLException {
        String sql = "{CALL GetWarehouseAdminApprovalStatus(?)}";
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, adminId);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        }
    }
}

