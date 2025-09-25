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

    @Override
    public void signupTotalAdmin(String taName, String taId, String taPw, String taPhone, String taEmail) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            totalAdminDao.insertTotalAdmin(connection, taName, taId, taPw, taPhone, taEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(String userId) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            userDao.deleteUser(connection, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteWarehouseAdmin(String adminId) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            warehouseAdminDao.deleteWarehouseAdmin(connection, adminId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTotalAdmin(String adminId) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            totalAdminDao.deleteTotalAdmin(connection, adminId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findIdByEmail(String email) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            
            // User 테이블에서 검색
            String userId = userDao.findIdByEmail(connection, email);
            if (userId != null) {
                return "일반회원: " + userId;
            }
            
            // WarehouseAdmin 테이블에서 검색
            String warehouseAdminId = warehouseAdminDao.findIdByEmail(connection, email);
            if (warehouseAdminId != null) {
                return "창고관리자: " + warehouseAdminId;
            }
            
            // TotalAdmin 테이블에서 검색
            String totalAdminId = totalAdminDao.findIdByEmail(connection, email);
            if (totalAdminId != null) {
                return "총관리자: " + totalAdminId;
            }
            
            return null; // 찾지 못함
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findPasswordById(String userId) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            
            // User 테이블에서 검색 및 임시 비밀번호 생성
            String tempUserPw = userDao.resetPasswordById(connection, userId);
            if (tempUserPw != null) {
                return "일반회원 임시 비밀번호: " + tempUserPw;
            }
            
            // WarehouseAdmin 테이블에서 검색 및 임시 비밀번호 생성
            String tempWarehouseAdminPw = warehouseAdminDao.resetPasswordById(connection, userId);
            if (tempWarehouseAdminPw != null) {
                return "창고관리자 임시 비밀번호: " + tempWarehouseAdminPw;
            }
            
            // TotalAdmin 테이블에서 검색 및 임시 비밀번호 생성
            String tempTotalAdminPw = totalAdminDao.resetPasswordById(connection, userId);
            if (tempTotalAdminPw != null) {
                return "총관리자 임시 비밀번호: " + tempTotalAdminPw;
            }
            
            return null; // 찾지 못함
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getUserInfo(String userId) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            
            // User 테이블에서 검색
            domain.User user = userDao.getUserInfo(connection, userId);
            if (user != null) {
                return user;
            }
            
            // WarehouseAdmin 테이블에서 검색
            domain.WarehouseAdmin warehouseAdmin = warehouseAdminDao.getWarehouseAdminInfo(connection, userId);
            if (warehouseAdmin != null) {
                return warehouseAdmin;
            }
            
            // TotalAdmin 테이블에서 검색
            domain.TotalAdmin totalAdmin = totalAdminDao.getTotalAdminInfo(connection, userId);
            if (totalAdmin != null) {
                return totalAdmin;
            }
            
            return null; // 찾지 못함
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserInfo(String userId, String name, String phone) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            
            // User 테이블에서 수정 시도
            boolean userUpdated = userDao.updateUserInfo(connection, userId, name, phone);
            if (userUpdated) {
                return true;
            }
            
            // WarehouseAdmin 테이블에서 수정 시도
            boolean warehouseAdminUpdated = warehouseAdminDao.updateWarehouseAdminInfo(connection, userId, name, phone);
            if (warehouseAdminUpdated) {
                return true;
            }
            
            // TotalAdmin 테이블에서 수정 시도
            boolean totalAdminUpdated = totalAdminDao.updateTotalAdminInfo(connection, userId, name, phone);
            if (totalAdminUpdated) {
                return true;
            }
            
            return false; // 수정 실패
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserPassword(String userId, String currentPassword, String newPassword) {
        try (Connection connection = DBUtil.getConnection()) {
            if (connection == null) throw new IllegalStateException("DB connection is null");
            
            // User 테이블에서 비밀번호 확인 및 수정 시도
            if (userDao.existsByCredentials(connection, userId, currentPassword)) {
                return userDao.updateUserPassword(connection, userId, newPassword);
            }
            
            // WarehouseAdmin 테이블에서 비밀번호 확인 및 수정 시도
            if (warehouseAdminDao.existsByCredentials(connection, userId, currentPassword)) {
                return warehouseAdminDao.updateWarehouseAdminPassword(connection, userId, newPassword);
            }
            
            // TotalAdmin 테이블에서 비밀번호 확인 및 수정 시도
            if (totalAdminDao.existsByCredentials(connection, userId, currentPassword)) {
                return totalAdminDao.updateTotalAdminPassword(connection, userId, newPassword);
            }
            
            return false; // 현재 비밀번호가 일치하지 않음
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


