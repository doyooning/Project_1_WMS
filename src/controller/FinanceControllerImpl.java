package controller;


import domain.*;
import service.FinanceServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FinanceControllerImpl implements FinanceController {
    private User user;
    private WarehouseAdmin whAdmin;
    private TotalAdmin totalAdmin;
    private int authority = 0;
    //private boolean loop = true;
    //Service 객체
    private FinanceServiceImpl finance;
    //사용자 입력
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    // 싱글톤 패턴 적용
    private static FinanceControllerImpl instance;
    private FinanceControllerImpl() {
        this.finance = FinanceServiceImpl.getInstance();
    }
    public static FinanceControllerImpl getInstance() {
        if (instance == null) instance = new FinanceControllerImpl();
        return instance;
    }

    @Override
    public void setLoggedInUser(Object user) {
        if (user instanceof TotalAdmin) {
            this.totalAdmin = (TotalAdmin) user;
            this.authority = 1;
        } else if (user instanceof WarehouseAdmin) {
            this.whAdmin = (WarehouseAdmin) user;
            if (this.whAdmin.getWIdx() == 0) {
                int wIdx = finance.getWidxByWaidx(this.whAdmin.getWaIdx());
                this.whAdmin.setWIdx(wIdx);
            }
            this.authority = 2;
        } else if (user instanceof User) {
            this.user = (User) user;
            this.authority = 3;
        }
    }

    @Override
    public void logoutUser() {
        this.user = null;
        this.whAdmin = null;
        this.totalAdmin = null;
        this.authority = 0;
    }

    //메인 화면 출력 메서드, 권한에 따라 다른 메서드로 화면 출력
    @Override
    public Boolean showFinanceMenu() {
        while(true) {
            String choice;
            switch (authority) {
                case 1:
                    showTotalAdminMenu();
                    choice = selectTotalAdminMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) {logoutUser(); return false;}
                    break;
                case 2:
                    showWhAdminMenu();
                    choice = selectWhAdminMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) {logoutUser(); return false;}
                    break;
                case 3:
                    showUserMenu();
                    choice = selectUserMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) {logoutUser(); return false;}
                    break;
                default:
                    System.out.println("접속 불가! 권한이 존재하지 않습니다.");
            }
        }
    }

    // 권한별 메인화면
    private void showTotalAdminMenu(){
        // 관리자 화면
        System.out.print("""
                            ============================================================
                                                      재무관리
                            ============================================================
                             1.전체 재무 조회 | 2.창고별 재무 조회 | 3.메인 메뉴 | 4.로그아웃
                            >\t""");
    }
    private void showWhAdminMenu(){
        //창고 관리자 화면
        System.out.print("""
                            ============================================================
                                                      재무관리
                            ============================================================
                              1.재무 조회 | 2.지출 관리 | 3.구독승인 관리 | 4.메인 메뉴 |
                              5.로그아웃
                            ============================================================
                            >\t""");
    }
    private void showUserMenu(){
        //일반회원 화면
        System.out.print("""
                           ============================================================
                                                     재무관리
                           ============================================================
                                       1.구독 관리  |  2.메인 메뉴  |  3.로그아웃
                           >\t""");
    }
    private void showExpenseMenu(){
        System.out.print("""
                ============================================================
                  1. 지출 내역 등록 | 2. 지출 내역 수정 | 3. 지출 내역 삭제
                ============================================================
                >\t""");
        selectExpenseMenu();
    }
    private void showUserSubMenu(){
        int uIdx = user.getUIdx();
        SubModel subInfo = finance.getUserSubInfo(uIdx);
        String subStatus = finance.getSubStatus(uIdx);
        if(subInfo == null){
            System.out.printf("""
                    ============================================================
                    내 구독 정보 >  없음 (%s)
                    ============================================================
                       1.구독 신청  |  2.재무관리 메뉴
                    ============================================================
                    >\t""", subStatus);
            selectNonSubUserMenu();
        } else{
            System.out.printf("""
                    ============================================================
                    내 구독 정보 > %d | %s | %d | %d
                                 (%s)
                    ============================================================
                        1.구독 변경  |  2.구독 취소 |  3.재무관리 메뉴
                    ============================================================
                    >\t""", subInfo.getSmIdx(), subInfo.getSmName(), subInfo.getSmPrice(), subInfo.getSmAmount(), subStatus);
            selectSubUserMenu();
        }
    }
    private void showWhSubMenu(){
        System.out.print("""
                ============================================================
                  1.구독신청 조회 | 2.재무관리 메뉴
                ============================================================
                >\t""");
        selectSubWhMenu();
    }
    private void showWhSubMgMenu(){
        int waIdx = whAdmin.getWaIdx();

        //구독 신청 목록 출력
        List<SubApproval> list = getPendingSubApprovalList(waIdx);
        printSubApprovalList(list);

        System.out.print("""
                ============================================================
                  1.구독신청 검토 | 2.재무관리 메뉴
                ============================================================
                >\t""");
        selectSubWhMgMenu();
    }

    //권한별 메뉴선택 및 메서드 호출
    private String selectTotalAdminMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetAllFinance();
                case "2" -> handleGetWhFinance();
                case "3" -> {return "mainMenu";}
                case "4" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("오류가 발생했습니다.");
            //throw new RuntimeException(e);
        }
        return "continue";
    }
    private String selectWhAdminMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetWhFinance();
                case "2" -> showExpenseMenu();
                case "3" -> showWhSubMenu();
                case "4" -> {return "mainMenu";}
                case "5" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("오류가 밸생했습니다.");
            //throw new RuntimeException(e);
        }
        return "continue";
    }
    private String selectUserMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> showUserSubMenu();
                case "2" -> {return "mainMenu";}
                case "3" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("오류가 발생했습니다.");
            //throw new RuntimeException(e);
        }
        return "continue";
    }
    private void selectExpenseMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleAddExpense(); // 핸들러 호출
                case "2" -> handleModifyExpense();
                case "3" -> handleRemoveExpense();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
    }
    private void selectNonSubUserMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleAddSubscription();
                case "2" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
    }
    private void selectSubUserMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleModifySubscription();
                case "2" -> handleCancelSubscription();
                case "3" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
    }
    private void selectSubWhMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> showWhSubMgMenu(); // 핸들러 호출
                case "2" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
    }
    private void selectSubWhMgMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleWhSubscription(); // 핸들러 호출
                case "2" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
    }


    private void handleGetAllFinance() {
        String type = getFinanceType();
        String date = getFinanceDate();
        // API 메서드 호출
        Map<String, Object> result = getFinanceList(type, date);
        // 결과 출력
        printFinanceList(result, date, type);
    }
    private void handleGetWhFinance() {
        int wIdx;
        if(whAdmin == null) {
            // API를 통해 창고 리스트를 받아와서 출력
            List<Warehouse> warehouses = getWarehouseList();
            printWarehouseList(warehouses);
            wIdx = getFinanceWIdx();
        } else {
            wIdx = finance.getWidxByWaidx(whAdmin.getWaIdx());
        }

        String type = getFinanceType();
        String date = getFinanceDate();
        // API 메서드 호출
        Map<String, Object> result = getWhFinanceList(wIdx, type, date);
        // 결과 출력
        printFinanceList(result, date, type);
    }
    private void handleAddExpense() {
        int wIdx;
        if(whAdmin == null) {
            System.out.println("권한이 없습니다!");
            return;
        } else {
            wIdx = finance.getWidxByWaidx(whAdmin.getWaIdx());
        }

        String type = getExpenseType();
        long amount = getExpenseAmount();
        Date date = getExpenseDate();

        Expense expense = new Expense();
        expense.setWIdx(wIdx);
        expense.setEType(type);
        expense.setEAmount(amount);
        expense.setEDate(date);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = addExpense(expense);
            if(result == true) System.out.println("지출 내역이 등록되었습니다.");
        } catch (Exception e) {
            System.out.println("지출 내역 등록에 실패했습니다.");
        }
    }
    private void handleModifyExpense() {
        int wIdx;
        if(whAdmin == null) {
            System.out.println("권한이 없습니다!");
            return;
        } else {
            wIdx = finance.getWidxByWaidx(whAdmin.getWaIdx());
        }

        int eIdx = getExpenseId();
        String type = getExpenseType();
        long amount = getExpenseAmount();
        Date date = getExpenseDate();

        Expense expense = new Expense();
        expense.setEIdx(eIdx);
        expense.setWIdx(wIdx);
        expense.setEType(type);
        expense.setEAmount(amount);
        expense.setEDate(date);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = modifyExpense(expense);
            if(result == true) System.out.println("지출 내역이 수정되었습니다.");
        } catch (Exception e) {
            System.out.println("지출 내역 수정에 실패했습니다.");
        }
    }
    private void handleRemoveExpense() {
        int wIdx;
        if(whAdmin == null) {
            System.out.println("권한이 없습니다!");
            return;
        } else {
            wIdx = finance.getWidxByWaidx(whAdmin.getWaIdx());
        }

        int eIdx = getExpenseId();

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = removeExpense(eIdx, wIdx);
            if(result == true) System.out.println("지출 내역이 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("지출 내역 삭제에 실패했습니다.");
        }
    }
    private void handleAddSubscription() {
        // 구독 모델 출력
        List<SubModel> subList = getSubModelList();
        printSubModelList(subList);

        int smIdx = getSubModelId();

        SubApproval subApproval = new SubApproval();
        subApproval.setUIdx(user.getUIdx());
        subApproval.setSmIdx(smIdx);

        int wIdx = getWarehouseId();
        subApproval.setWaIdx(wIdx);

        String payment = getPayment();
        subApproval.setSaPayment(payment);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = addSubscription(subApproval);
            if(result == true) System.out.println("구독이 신청되었습니다.");
        } catch (Exception e) {
            System.out.println("구독 신청에 실패했습니다.");
        }
    }
    private void handleModifySubscription() {
        int uIdx = user.getUIdx();

        // 구독 모델 출력
        List<SubModel> subList = getSubModelList();
        printSubModelList(subList);
        int smIdx = getSubModelId();

        int wIdx = getWarehouseId();

        String payment = getPayment();

        SubApproval subApproval = new SubApproval();
        subApproval.setUIdx(uIdx);
        subApproval.setSmIdx(smIdx);
        subApproval.setWaIdx(wIdx);
        subApproval.setSaPayment(payment);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = modifySubscription(subApproval);
            if(result == true) System.out.println("구독 변경이 신청되었습니다. 담당자 승인시 종료일 이후 선택하신 모델로 변경됩니다.");
        } catch (Exception e) {
            System.out.println("구독 변경 신청에 실패했습니다.");
        }
    }
    private void handleCancelSubscription() {
        int uIdx = user.getUIdx();

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = cancelSubscription(uIdx);
            if(result == true) System.out.println("구독이 취소되었습니다. 종료일 이후 갱신되지 않습니다.");
        } catch (Exception e) {
            System.out.println("구독 취소에 실패했습니다.");
        }
    }
    private void handleWhSubscription(){
        int saIdx = getSubApprovalId();

        Map<String, Object> detail = getSubApprovalDetail(saIdx);
        printSubApprovalDetail(detail);

        boolean tf = getApproved();
        try {
            // API 메서드 호출
            Boolean result = true;
            if(tf==false) result = rejectSubscription(saIdx);
            else result = approveSubscription(saIdx);
            if(result == true) System.out.println("구독 신청이 처리되었습니다.");
        } catch (Exception e) {
            System.out.println("구독 신청 처리에 실패했습니다.");
        }
    }

    @Override
    public Map<String, Object> getFinanceList(String type, String date) {
        // service 호출 후 결과 바로 반환 (입력/출력 로직 제거)
        return finance.getFinanceList(0, type, date);
    }

    @Override
    public Map<String, Object> getWhFinanceList(int wIdx, String type, String date) {
        // service 호출 후 결과 바로 반환 (입력/출력 로직 제거)
        return finance.getFinanceList(wIdx, type, date);
    }

    @Override
    public List<Warehouse> getWarehouseList() {
        // service 호출 후 결과 바로 반환 (출력 로직 제거)
        return finance.getWarehouseList();
    }

    @Override
    public Boolean addExpense(Expense expense) {
        return finance.addExpense(expense);
    }

    @Override
    public Boolean modifyExpense(Expense expense) {
        return finance.modifyExpense(expense);
    }

    @Override
    public Boolean removeExpense(int eIdx, int wIdx) {
        return finance.removeExpense(eIdx, wIdx);
    }

    @Override
    public List<SubModel> getSubModelList() {
        return finance.getSubModelList();
    }

    @Override
    public Boolean addSubscription(SubApproval subApproval) {
        return finance.addSubscription(subApproval);
    }

    @Override
    public Boolean modifySubscription(SubApproval subApproval) {
        return finance.modifySubscription(subApproval);
    }

    @Override
    public Boolean cancelSubscription(int uIdx) {
        return finance.cancelSubscription(uIdx);
    }

    @Override
    public List<SubApproval> getPendingSubApprovalList(int waIdx) {
        return finance.getPendingSubApprovalList(waIdx);
    }

    @Override
    public Map<String, Object> getSubApprovalDetail(int saIdx) {
        return finance.getSubApprovalDetail(saIdx);
    }

    @Override
    public Boolean rejectSubscription(int saIdx) {
        return finance.rejectSubscription(saIdx);
    }

    @Override
    public Boolean approveSubscription(int saIdx) {
        return finance.approveSubscription(saIdx);
    }



    private void printFinanceList(Map<String, Object> result, String date, String type) {
        boolean isYear = date.length() == 4;

        System.out.println("\n📊 [" + date + (isYear ? "년" : "월") + " " + type + " 정산 내역]");
        System.out.println("-".repeat(60));

        switch (type) {
            case "All" -> {
                if (isYear) {
                    Map<String, Map<String, Long>> monthlySummary = (Map<String, Map<String, Long>>) result.get("monthlySummary");
                    long totalSales = (long) result.get("totalSales");
                    long totalExpense = (long) result.get("totalExpense");
                    long netAmount = (long) result.get("netAmount");

                    System.out.printf("%10s | %14s | %14s | %14s \n", "월", "매출", "지출", "정산");
                    for (String month : monthlySummary.keySet()) {
                        Map<String, Long> data = monthlySummary.get(month);
                        System.out.printf("%10s | %14d | %14d | %14d \n",
                                month, data.get("sales"), data.get("expense"), data.get("net"));
                    }
                    System.out.println("-".repeat(60));
                    System.out.println("총 매출: " + String.format("%d원", totalSales));
                    System.out.println("총 지출: " + String.format("%d원", totalExpense));
                    System.out.println("총 정산: " + String.format("%d원", netAmount));
                } else {
                    List<Sales> salesList = (List<Sales>) result.get("salesList");
                    List<Expense> expenseList = (List<Expense>) result.get("expenseList");
                    long totalSales = (long) result.get("totalSales");
                    long totalExpense = (long) result.get("totalExpense");
                    long netAmount = (long) result.get("netAmount");

                    System.out.println("[매출 내역]");
                    System.out.printf("%5s | %15s | %21s  \n",
                            "매출번호", "매출금액", "매출발생일");
                    System.out.println("-".repeat(60));
                    for (Sales s : salesList) {
                        System.out.printf("%5d | %15d | %21s \n",
                                s.getSIdx(), s.getSPrice(), s.getSDate());
                        //System.out.printf("- [%s] %,d원\n", s.getSDate(), s.getSPrice());
                    }

                    System.out.println("-".repeat(60));
                    System.out.println("\n[지출 내역]");
                    System.out.printf("%5s | %10s | %15s | %21s \n",
                            "지출번호", "지출타입", "지출금액", "지출발생일");
                    System.out.println("-".repeat(60));

                    for (Expense e : expenseList) {
                        System.out.printf("%5d | %10s | %15,d | %21s \n",
                                e.getEIdx(), e.getEType(), e.getEAmount(), e.getEDate());
                        //System.out.printf("- [%s] %s: %,d원\n", e.getEDate(), e.getEType(), e.getEAmount());
                    }
                    System.out.println("-".repeat(60));
                    System.out.println("\n총 매출: " + String.format("%d원", totalSales));
                    System.out.println("총 지출: " + String.format("%d원", totalExpense));
                    System.out.println("총 잔액: " + String.format("%d원", netAmount));
                }
            }

            case "Sales" -> {
                if (isYear) {
                    Map<String, Map<String, Long>> monthlySummary = (Map<String, Map<String, Long>>) result.get("monthlySummary");
                    long totalSales = (long) result.get("totalSales");

                    System.out.printf("%10s | %14s \n", "월", "매출");
                    System.out.println("-".repeat(60));

                    for (String month : monthlySummary.keySet()) {
                        Map<String, Long> data = monthlySummary.get(month);
                        System.out.printf("%10s | %14,d \n", month, data.get("sales"));
                        //System.out.printf("%s월: 매출 %,d원\n", month, data.get("sales"));
                    }
                    System.out.println("-".repeat(60));
                    System.out.println("\n총 매출: " + String.format("%,d원", totalSales));
                } else {
                    List<Sales> salesList = (List<Sales>) result.get("salesList");
                    long totalSales = (long) result.get("totalSales");

                    System.out.println("[매출 내역]");
                    System.out.printf("%5s | %15s | %21s  \n",
                            "매출번호", "매출금액", "매출발생일");
                    System.out.println("-".repeat(60));
                    for (Sales s : salesList) {
                        System.out.printf("%5d | %15d | %21s \n",
                                s.getSIdx(), s.getSPrice(), s.getSDate());
                        //System.out.printf("- [%s] %,d원\n", s.getSDate(), s.getSPrice());
                    }

                    System.out.println("-".repeat(60));
                    System.out.println("\n총 매출: " + String.format("%,d원", totalSales));
                }
            }

            case "Expense" -> {
                if (isYear) {
                    Map<String, Map<String, Long>> monthlySummary = (Map<String, Map<String, Long>>) result.get("monthlySummary");
                    long totalExpense = (long) result.get("totalExpense");

                    System.out.printf("%10s | %14s \n", "월", "지출");
                    System.out.println("-".repeat(60));

                    for (String month : monthlySummary.keySet()) {
                        Map<String, Long> data = monthlySummary.get(month);
                        System.out.printf("%10s | %14,d \n", month, data.get("expense"));
                        //System.out.printf("%s월: 매출 %,d원\n", month, data.get("sales"));
                    }
                    System.out.println("-".repeat(60));
                    System.out.println("\n총 지출: " + String.format("%,d원", totalExpense));
                } else {
                    List<Expense> expenseList = (List<Expense>) result.get("expenseList");
                    long totalExpense = (long) result.get("totalExpense");

                    System.out.printf("%5s | %10s | %15s | %21s \n",
                            "지출번호", "지출타입", "지출금액", "지출발생일");
                    System.out.println("-".repeat(60));

                    for (Expense e : expenseList) {
                        System.out.printf("%5d | %10s | %15,d | %21s \n",
                                e.getEIdx(), e.getEType(), e.getEAmount(), e.getEDate());
                        //System.out.printf("- [%s] %s: %,d원\n", e.getEDate(), e.getEType(), e.getEAmount());
                    }

                    System.out.println("-".repeat(60));
                    System.out.println("\n총 지출: " + String.format("%,d원", totalExpense));
                }
            }
        }
    }
    private void printWarehouseList(List<Warehouse> warehouses) {
        System.out.println("[창고 목록]");
        System.out.printf(" %5s | %10s | %10s \n", "창고번호", "최대수용용량", "창고별재고");
        System.out.println("-".repeat(60));
        for (Warehouse w : warehouses) {
            System.out.printf(" %5d | %10d | %10d \n", w.getWIdx(), w.getWMaxAmount(), w.getWStock());
        }
        System.out.println("-".repeat(60));
    }
    private void printSubModelList(List<SubModel> subModels) {
        System.out.println("[구독모델 목록]");
        System.out.printf(" %5s | %10s | %10s | %10s \n", "모델번호", "이름", "가격", "창고 용량");
        System.out.println("-".repeat(60));
        for (SubModel s : subModels) {
            System.out.printf(" %5d | %10s | %10d | %10d \n", s.getSmIdx(), s.getSmName(), s.getSmPrice(), s.getSmAmount());
        }
        System.out.println("-".repeat(60));
    }
    private void printSubApprovalList(List<SubApproval> subApprovals) {
        System.out.println("[구독신청 목록]");
        System.out.printf(" %8s | %8s | %15s \n", "구독승인번호", "회원번호", "신청일");
        System.out.println("-".repeat(60));
        for (SubApproval s : subApprovals) {
            System.out.printf(" %8d | %8d | %15s \n", s.getSaIdx(), s.getUIdx(), s.getSaDate());
        }
        System.out.println("-".repeat(60));
    }
    private void printSubApprovalDetail(Map<String, Object> result){
        System.out.println("[구독승인 검토 내용]");
        System.out.printf("수용가능용량 : %d  |  사용자 요구용량 : %d  |  승인가능여부 : %s \n", (int)result.get("availableAmount"), (int)result.get("requiredAmount"), String.valueOf(result.get("result")));
    }


    private String getFinanceDate(){
        while(true) {
            String num = inputNum("""
                    ============================================================
                                    1. 월 단위  |  2. 연 단위
                    ============================================================
                    >  """);

            //반환할 날짜 변수
            String date;

            String year;
            String month;
            switch (num) {
                    case "1": //월별정산 선택 시
                        year = inputNum("년도>  ");
                        month = inputNum("월>  ");
                        date = year + "-" + month;
                        return date;
                    case "2": //연도별 정산 선택 시
                        year = inputNum("년도>  ");
                        date = year;
                        return date;
                    default:
                        System.out.println("번호를 잘못 입력했습니다.");
            }
        }
    }
    private String getFinanceType(){
        while(true) {
            //메뉴 번호 입력
            String num = inputNum("""
                ============================================================
                    1.내역 조회 | 2.매출 조회 | 3.지출 조회  
                ============================================================
                >  """);

            switch (num) {
                case "1" -> {
                    return "All";
                }
                case "2" -> {
                    return "Sales";
                }
                case "3" -> {
                    return "Expense";
                }
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        }
    }
    private int getFinanceWIdx() {
        while(true) {
            try {
                int wIdx = Integer.parseInt(inputNum("창고입력번호> "));
                System.out.println("=".repeat(60));
                return wIdx;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
                //throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("오류가 밸생했습니다.");
                //throw new RuntimeException(e);
            }
        }
    }
    private String getExpenseType(){
        while(true) {
            //메뉴 번호 입력
            String num = inputNum("""
                    ============================================================
                       1.MAINTENANCE | 2.RENT
                    ============================================================
                    > """);
            switch (num) {
                case "1" -> {
                    return "MAINTENANCE";
                }
                case "2" -> {
                    return "RENT";
                }
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        }
    }
    private long getExpenseAmount(){
        System.out.println("=".repeat(60));
        return Long.parseLong(inputNum("지출 금액> "));
    }
    private Date getExpenseDate() {
        while(true) {
            System.out.println("=".repeat(60));
            String year = inputNum("지출 년도(YYYY)> ");
            String month = inputNum("지출 월(MM)> ");
            String day = inputNum("지출 일(DD)> ");
            String dateStr = String.format("%s-%s-%s", year, month, day);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date date = format.parse(dateStr);  // java.util.Date
                return date;
            } catch (ParseException e) {
                System.out.println("입력이 잘못됐습니다.");
                //throw new RuntimeException(e);
            }
        }

    }
    private Boolean getConfirm(){
        while(true) {
            //메뉴 번호 입력
            String num = inputNum("""
                    ============================================================
                      1. 확인  |  2. 취소
                    ============================================================
                     >  """);
            switch (num) {
                case "1" -> {
                    return true;
                }
                case "2" -> {
                    return false;
                }
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        }
    }
    private int getExpenseId(){
        System.out.println("=".repeat(60));
        while(true){
            int num = Integer.parseInt(inputNum("수정할 지출번호>  "));
            if(num != 0) return num;
        }
    }
    private int getSubModelId(){
        System.out.println("=".repeat(60));
        while(true){
            int num = Integer.parseInt(inputNum("구독할 모델번호>  "));
            if(num != 0) return num;
        }
    }
    private int getWarehouseId(){
        System.out.println("=".repeat(60));
        List<Warehouse> list = getWarehouseList();
        printWarehouseList(list);
        while(true) {
            int num = Integer.parseInt(inputNum("창고번호 선택>  "));
            if(num != 0) return num;
        }
    }
    private String getPayment(){
        System.out.println("=".repeat(60));
        while(true){
            String num = inputNum("결제방법 입력>  ");
            if(num != null) return num;
        }
    }
    private int getSubApprovalId(){
        return Integer.parseInt(inputNum("검토할 번호 입력>  "));
    }
    private Boolean getApproved(){
        while(true) {
            //메뉴 번호 입력
            String num = inputNum("""
                    ============================================================
                      1. 승인  |  2. 거절
                    ============================================================
                     >  """);
            switch (num) {
                case "1" -> {
                    return true;
                }
                case "2" -> {
                    return false;
                }
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        }
    }

    private String inputNum(String msg){
        System.out.print(msg);
        try {
            return input.readLine();
        } catch (IOException e) {
            System.out.println("입력이 잘못됐습니다.");
            //throw new RuntimeException(e);
        }
        return null;
    }






}
