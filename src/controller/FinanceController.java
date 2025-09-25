
package controller;

import domain.Expense;
import domain.SubApproval;
import domain.SubModel;
import domain.Warehouse;

import java.util.List;
import java.util.Map;

public interface FinanceController {
    // 재무 관리 메인 페이지, 진입점
    void showFinanceMenu();

    // 전체 재무 리스트 조회
    Map<String, Object> getFinanceList(String type, String date);

    //창고 재무 리스트 조회
    Map<String, Object> getWhFinanceList(int wIdx, String type, String date);

    //창고 목록 조회
    List<Warehouse> getWarehouseList();

    Boolean addExpense(Expense expense);

    Boolean modifyExpense(Expense expense);

    Boolean removeExpense(int eIdx, int wIdx);

    List<SubModel> getSubModelList();

    Boolean addSubscription(SubApproval subApproval);

    Boolean modifySubscription(SubApproval subApproval);

    Boolean cancelSubscription(int uIdx);

    List<SubApproval> getPendingSubApprovalList(int waIdx);

    Map<String, Object> getSubApprovalDetail(int saIdx);

    Boolean rejectSubscription(int saIdx);
}
