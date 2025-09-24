package dao;

import domain.Expense;
import domain.Sales;

import java.util.List;
import java.util.Map;

public interface Finance {

    List<Expense> getMontlyExpenseList(int wIdx, String date);
    List<Sales> getMontlySalesList(int wIdx, String date);

}
