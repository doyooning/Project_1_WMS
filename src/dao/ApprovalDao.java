package dao;

import domain.EntityStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ApprovalDao {

    private static ApprovalDao instance;
    private ApprovalDao() {}
    public static ApprovalDao getInstance() {
        if (instance == null) instance = new ApprovalDao();
        return instance;
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
}


