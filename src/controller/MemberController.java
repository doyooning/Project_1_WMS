package controller;

import domain.User;
import domain.WarehouseAdmin;

public interface MemberController {
    void requestSignup(User user, int taIdx);
    void requestSignupWarehouse(WarehouseAdmin admin, int taIdx);
}
