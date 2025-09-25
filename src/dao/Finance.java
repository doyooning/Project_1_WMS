package dao;

import domain.*;

import java.util.List;
import java.util.Map;

public interface Finance {

    List<Expense> getMontlyExpenseList(int wIdx, String date);
    List<Sales> getMontlySalesList(int wIdx, String date);
    Integer getMontlySalesTotal(int wIdx, String date);
    Integer getMontlyExpenseTotal(int wIdx, String date);
    Map<String, Long> getYearlySalesList(int wIdx, String date);
    Map<String, Long> getYearlyExpenseList(int wIdx, String date);

    List<Warehouse> getWarehouseList();
    int addExpense(Expense expense);
    int modifyExpense(Expense expense);
    int removeExpense(int eIdx, int wIdx);

    SubModel getUserSubInfo(int uIdx);
    List<SubModel> getSubModelList();

    int addSubscription(SubApproval subApproval);

    SubApproval getPrevSub(int uIdx);

    int updateSubscriptionStatus(int saIdx, EntityStatus status);

    List<SubApproval> getPendingSubApprovalList();

    int getSubApprovalDetail(int saIdx);
}
