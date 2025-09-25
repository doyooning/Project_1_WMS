package service;

import domain.Expense;
import domain.SubApproval;
import domain.SubModel;
import domain.Warehouse;

import java.util.List;
import java.util.Map;

public interface FinanceService {
    Map<String, Object> getFinanceList(int wIdx, String type, String date);

    List<Warehouse> getWarehouseList();

    boolean addExpense(Expense expense);
    boolean modifyExpense(Expense expense);
    boolean removeExpense(int eIdx, int wIdx);

    SubModel getUserSubInfo(int uIdx);
    String getSubStatus(int uIdx);
    List<SubModel> getSubModelList();

    Boolean addSubscription(SubApproval supApproval);
    Boolean modifySubscription(SubApproval supApproval);
    Boolean cancelSubscription(int uIdx);

    List<SubApproval> getPendingSubApprovalList(int waIdx);

    Map<String, Object> getSubApprovalDetail(int saIdx);

    Boolean rejectSubscription(int saIdx);
}
