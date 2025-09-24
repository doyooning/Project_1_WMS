package service;

import controller.FinanceControllerImpl;
import dao.FinanceDao;
import domain.Expense;
import domain.Sales;

import java.util.*;

public class FinanceServiceImpl implements FinanceService {
    //dao 객체
    private FinanceDao financeDao;

    // 싱글톤 패턴 적용
    private static FinanceServiceImpl instance;
    private FinanceServiceImpl() {}
    public static FinanceServiceImpl getInstance() {
        if (instance == null) instance = new FinanceServiceImpl();
        return instance;
    }

    @Override
    public Map<String, Object> getFinanceList(int wIdx, String type, String date) {
        financeDao = FinanceDao.getInstance();
        boolean isYear = date.length() == 4;

        List<Sales> salesList = new ArrayList<>();
        List<Expense> expenseList = new ArrayList<>();
        long totalSales = 0;
        long totalExpense = 0;

        switch (type) {
            case "All" -> {
                if (isYear) {
                    Map<String, Long> salesSummary = financeDao.getYearlySalesList(wIdx, date);
                    Map<String, Long> expenseSummary = financeDao.getYearlyExpenseList(wIdx, date);
                    return buildMonthlySummary(salesSummary, expenseSummary, date);
                } else {
                    salesList = financeDao.getMontlySalesList(wIdx, date);
                    expenseList = financeDao.getMontlyExpenseList(wIdx, date);
                    totalSales = financeDao.getMontlySalesTotal(wIdx, date);
                    totalExpense = financeDao.getMontlyExpenseTotal(wIdx, date);
                }
            }
            case "Sales" -> {
                if (isYear) {
                    Map<String, Long> salesSummary = financeDao.getYearlySalesList(wIdx, date);
                    return buildMonthlySummary(salesSummary, new HashMap<>(), date);
                } else {
                    salesList = financeDao.getMontlySalesList(wIdx, date);
                    totalSales = financeDao.getMontlySalesTotal(wIdx, date);
                }
            }
            case "Expense" -> {
                if (isYear) {
                    Map<String, Long> expenseSummary = financeDao.getYearlyExpenseList(wIdx, date);
                    return buildMonthlySummary(new HashMap<>(), expenseSummary, date);
                } else {
                    expenseList = financeDao.getMontlyExpenseList(wIdx, date);
                    totalExpense = financeDao.getMontlyExpenseTotal(wIdx, date);
                }
            }
        }

        return Map.of(
                "salesList", salesList,
                "expenseList", expenseList,
                "totalSales", totalSales,
                "totalExpense", totalExpense,
                "netAmount", totalSales - totalExpense
        );
    }

    private Map<String, Object> buildMonthlySummary(Map<String, Long> salesMap, Map<String, Long> expenseMap, String date) {
        Map<String, Map<String, Long>> monthlySummary = new LinkedHashMap<>();
        long totalSales = 0;
        long totalExpense = 0;

        for (int i = 1; i <= 12; i++) {
            String month = date + "-" + String.format("%02d", i);
            long sales = salesMap.getOrDefault(month, 0L);
            long expense = expenseMap.getOrDefault(month, 0L);
            long net = sales - expense;

            monthlySummary.put(month, Map.of(
                    "sales", sales,
                    "expense", expense,
                    "net", net
            ));

            totalSales += sales;
            totalExpense += expense;
        }

        return Map.of(
                "monthlySummary", monthlySummary,
                "totalSales", totalSales,
                "totalExpense", totalExpense,
                "netAmount", totalSales - totalExpense
        );
    }


}
