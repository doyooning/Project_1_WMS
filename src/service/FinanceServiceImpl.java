//package service;
//
//import controller.FinanceControllerImpl;
//import dao.FinanceDao;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//public class FinanceServiceImpl {
//
//    private FinanceDao finance;
//
//    // 싱글톤 패턴 적용
//    private static FinanceServiceImpl instance;
//    private FinanceServiceImpl() {}
//    public static FinanceServiceImpl getInstance() {
//        if (instance == null) instance = new FinanceServiceImpl();
//        return instance;
//    }
//}
