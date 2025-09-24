
package controller;

import java.util.Map;

public interface FinanceController {
    // 재무 관리 메인 페이지, 진입점
    void showFinanceMenu();

    // 재무 리스트 조회
    void getAllFinanceList();
    void getWhFinanceList();

    // 창고 리스트 조회
    void getWarehouseList();
}
