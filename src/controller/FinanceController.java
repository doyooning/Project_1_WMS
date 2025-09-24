
package controller;

import java.util.Map;

public interface FinanceController {
    // 재무 관리 메인 페이지
    void showFinanceMenu();
    void showTotalAdminMenu();
    void showWhAdminMenu();
    void showUserMenu();

    void selectTotalAdminMenu();
    void selectWhAdminMenu();
    void selectUserMenu();

    void getAllFinanceList();
    String getFinanceDate();
    String getFinanceType();

    void printFinanceList(Map<String, Object> result, String date, String type);
    void getWhFinanceList();
    int getFinanceWIdx();
    void getWarehouseList();
}
