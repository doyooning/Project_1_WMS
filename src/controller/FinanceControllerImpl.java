//package controller;
//
//import dao.Dao;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//public class FinanceControllerImpl implements FinanceController {
//    private Dao dao;
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//
//
//    // 싱글톤 패턴 적용
//    private static FinanceControllerImpl instance;
//    private FinanceControllerImpl() {}
//    public static FinanceControllerImpl getInstance() {
//        if (instance == null) instance = new FinanceControllerImpl();
//        return instance;
//    }
//
//    @Override
//    public void getAllFinanceList() {
//
//    }
//}
