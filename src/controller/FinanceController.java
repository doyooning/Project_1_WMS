
package controller;

import java.util.Map;

public interface FinanceController {
    // 재무 관리 메인 페이지, 진입점
    void showFinanceMenu();

    // 전체 재무 리스트 조회
    Map<String, Object> getFinanceList(String type, String date);

}
