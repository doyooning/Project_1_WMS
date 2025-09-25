package controller;

import domain.User;
import domain.WarehouseAdmin;

public interface MemberController {
    void requestSignup(User user, int taIdx);
    void requestSignupWarehouse(WarehouseAdmin admin, int taIdx);
    void signupTotalAdmin(String taName, String taId, String taPw, String taPhone, String taEmail);
    boolean loginUser(String userId, String userPw);
    boolean loginWarehouseAdmin(String adminId, String adminPw);
    boolean loginTotalAdmin(String adminId, String adminPw);
    void deleteUser(String userId);
    void deleteWarehouseAdmin(String adminId);
    void deleteTotalAdmin(String adminId);
    String findIdByEmail(String email);
    String findPasswordById(String userId);
    Object getUserInfo(String userId);
    boolean updateUserInfo(String userId, String name, String phone);
    boolean updateUserPassword(String userId, String currentPassword, String newPassword);
}
