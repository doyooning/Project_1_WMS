package controller;

import dao.ApprovalDao;
import dao.TotalAdminDao;
import dao.UserDao;
import dao.WarehouseAdminDao;
import domain.EntityStatus;
import domain.User;
import domain.WarehouseAdmin;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberControllerImpl implements MemberController {

    private final UserDao userDao = UserDao.getInstance();
    private final ApprovalDao approvalDao = ApprovalDao.getInstance();
    private final WarehouseAdminDao warehouseAdminDao = WarehouseAdminDao.getInstance();
    private final TotalAdminDao totalAdminDao = TotalAdminDao.getInstance();

    // 싱글톤 패턴 적용
    private static MemberControllerImpl instance;
    private MemberControllerImpl() {}
    public static MemberControllerImpl getInstance() {
        if (instance == null) instance = new MemberControllerImpl();
        return instance;
    }

    @Override
    public void requestSignup(User user, int taIdx) {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            if (connection == null) {
                throw new IllegalStateException("DB connection is null");
            }
            connection.setAutoCommit(false);

            int createdUserIdx = userDao.insertPendingUser(connection, user);

            approvalDao.insertApprovalForUser(connection, createdUserIdx, taIdx, EntityStatus.PENDING);

            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // ignore
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void requestSignupWarehouse(WarehouseAdmin admin, int taIdx) {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            if (connection == null) {
                throw new IllegalStateException("DB connection is null");
            }
            connection.setAutoCommit(false);

            int createdWaIdx = warehouseAdminDao.insertPendingWarehouseAdmin(connection, admin);

            approvalDao.insertApprovalForWarehouse(connection, createdWaIdx, taIdx, EntityStatus.PENDING);

            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // ignore
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public boolean loginUser(String userId, String userPw) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            return userDao.existsByCredentials(connection, userId, userPw);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean loginWarehouseAdmin(String adminId, String adminPw) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            return warehouseAdminDao.existsByCredentials(connection, adminId, adminPw);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean loginTotalAdmin(String adminId, String adminPw) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            return totalAdminDao.existsByCredentials(connection, adminId, adminPw);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


