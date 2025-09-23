//package controller;
//
//
//import domain.User;
//import service.FinanceServiceImpl;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//public class FinanceControllerImpl implements FinanceController {
//    private User user;
//    private int authority = 0;
//
//    private FinanceServiceImpl finance;
//    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//
//
//    // 싱글톤 패턴 적용
//    private static FinanceControllerImpl instance;
//
//    private FinanceControllerImpl() {
//    }
//
//    public static FinanceControllerImpl getInstance() {
//        if (instance == null) instance = new FinanceControllerImpl();
//        return instance;
//    }
//
//    @Override
//    public void showFinanceMenu() {
//        switch (authority) {
//            case 0:
//                showTotalAdminMenu();
//                selectTotalAdminMenu();
//                break;
//            case 1:
//                showWarehouseAdminMenu();
//                selectWarehouseAdminMenu();
//                break;
//            case 2:
//                showUserMenu();
//                selectUserMenu();
//                break;
//            default:
//                System.out.println("접속 불가! 권한이 존재하지 않습니다.");
//        }
//    }
//
//    public void showTotalAdminMenu(){
//        // 관리자 화면
//        System.out.println("""
//                            ============================================================
//                                                      재무관리
//                            ============================================================
//                             1.전체 재무 조회 | 2.창고별 재무 조회 | 3.메인 메뉴 | 4.로그아웃
//                            >  """);
//    }
//
//    public void showWarehouseAdminMenu(){
//        //창고 관리자 화면
//        System.out.println("""
//                            ============================================================
//                                                      재무관리
//                            ============================================================
//                              1.창고별 재무 관리 | 2.구독승인 관리 | 3.메인 메뉴 | 4.로그아웃
//                            >  """);
//    }
//
//    public void showUserMenu(){
//        //일반회원 화면
//        System.out.println("""
//                           ============================================================
//                                                     재무관리
//                           ============================================================
//                                       1.구독 관리  |  2.메인 메뉴  |  3.로그아웃
//                           >  """);
//    }
//
//    public void selectTotalAdminMenu(){
//        try {
//            int num = Integer.parseInt(input.readLine().trim());
//            switch (num) {
//                case 0 -> getAllFinanceList();
//                case 1 -> getWhFinanceList();
//                case 2 -> System.out.println("\n\n\n");
//                case 3 -> System.out.println("logout");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NumberFormatException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void selectWarehouseAdminMenu(){
//        try {
//            int num = Integer.parseInt(input.readLine().trim());
//            switch (num) {
//                case 0 -> getAllFinanceList();
//                case 1 -> getWhFinanceList();
//                case 2 -> System.out.println("\n\n\n");
//                case 3 -> System.out.println("logout");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NumberFormatException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void selectUserMenu(){
//
//    }
//
//    @Override
//    public void getAllFinanceList() {
//
//    }
//
//    public void getWhFinanceList(){
//
//    }
//}
