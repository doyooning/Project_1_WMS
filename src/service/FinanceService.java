package service;

import domain.Expense;
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
}
