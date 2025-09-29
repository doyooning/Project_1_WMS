package view;

import controller.*;

import domain.WarehouseAdmin;
import service.WarehouseServiceImpl;
import util.ErrorHandler;
import java.util.Scanner;

public class Main {
    private MemberController memberControl;
    private FinanceController financeControl;
    private BoardController boardControl;
    private Scanner scan;
    private String currentUserType; // 현재 로그인한 사용자 유형 저장
    private String currentUserId; // 현재 로그인한 사용자 ID 저장
    private String currentUserName; // 현재 로그인한 사용자 이름 저장
    private WarehouseAdmin warehouseAdmin; //재고관리 - 실사로그에서 이 객체 자체가 필요

    public Main() {
        memberControl = MemberControllerImpl.getInstance();
        financeControl = FinanceControllerImpl.getInstance();
        boardControl = BoardControllerImpl.getInstance();
        scan = new Scanner(System.in);
    }

    public static void main(String[] args) {
        info();
        new Main().run();
    }


    private void run() {
        boolean running = true;
        while (running) {
            loginPage();
            String input = scan.nextLine().trim();
            switch (input) {
                case "1":
                    handleSignup();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    boardControl.showBoardMenu();
                    break;
                case "4":
                    handleFindId();
                    break;
                case "5":
                    handleFindPassword();
                    break;
                case "q":
                case "Q":
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("올바른 값을 입력하세요 (1, 2, 3, 4, 5, q)");
            }
        }
        System.out.println("WMS 프로그램 종료");
    }

    private void handleSignup() {
        System.out.println();
        System.out.println("============================================================");
        System.out.print("회원 유형 선택 (1: 일반회원 / 2: 창고관리자): ");
        String type = scan.nextLine().trim();
        System.out.print("ID: ");
        String id = scan.nextLine().trim();
        System.out.print("PW: ");
        String pw = scan.nextLine().trim();
        System.out.print("이름: ");
        String name = scan.nextLine().trim();
        System.out.print("전화번호: ");
        String phone = scan.nextLine().trim();
        System.out.print("이메일: ");
        String email = scan.nextLine().trim();

        int taIdx = 1; // 데모: 총관리자 고정

        if ("1".equals(type)) {
            domain.User user = new domain.User();
            user.setUId(id);
            user.setUserPw(pw);
            user.setUName(name);
            user.setUPhone(phone);
            user.setUEmail(email);

            try {
                memberControl.requestSignup(user, taIdx);
                System.out.println("회원형가입 신청이 접수되었습니다. (관리자 승인 대기)");
            } catch (RuntimeException e) {
                ErrorHandler.displayAndLog("회원가입 신청 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.", e);
            }
        } else if ("2".equals(type)) {
            domain.WarehouseAdmin admin = new domain.WarehouseAdmin();
            admin.setWaId(id);
            admin.setWaPw(pw);
            admin.setWaName(name);
            admin.setWaPhone(phone);
            admin.setWaEmail(email);

            try {
                memberControl.requestSignupWarehouse(admin, taIdx);
                System.out.println("창고관리자 회원가입 신청이 접수되었습니다. (관리자 승인 대기)");
            } catch (RuntimeException e) {
                ErrorHandler.displayAndLog("회원가입 신청 중 오류가 발생했습니다. 잠시 후 다시 시도하세요.", e);
            }
        }
//        else if ("3".equals(type)) {
//            /*
//            totaladmin
//            totaladminpw
//
//            totaladmin2
//            totaladmin2pw
//
//            2명의 총관리자만 만들어 뒀습니다
//             */
//
//
//            // 총관리자 회원가입 (승인 없이 바로 생성)
//            try {
//                memberControl.signupTotalAdmin(name, id, pw, phone, email);
//                System.out.println("총관리자 회원가입이 완료되었습니다.");
//            } catch (RuntimeException e) {
//                System.out.println("회원가입 중 오류가 발생했습니다: " + e.getMessage());
//            }
//        }
        else {
            System.out.println("올바른 회원 유형을 선택하세요.");
        }
    }

    private void handleLogin() {
        System.out.println();
        System.out.println("============================================================");
        System.out.print("로그인 유형 선택 (1: 일반회원 / 2: 창고관리자 / 3: 총관리자): ");
        String loginType = scan.nextLine().trim();
        System.out.print("ID: ");
        String loginId = scan.nextLine().trim();
        System.out.print("PW: ");
        String loginPw = scan.nextLine().trim();

        boolean success;
        if ("1".equals(loginType)) {
            success = memberControl.loginUser(loginId, loginPw);
            if (success) {
                String status = memberControl.getUserApprovalStatus(loginId);
                if ("PENDING".equals(status)) {
                    System.out.println("[회원가입 승인 대기중입니다]");
                    return;
                } else if ("REJECTED".equals(status)) {
                    System.out.println("[회원가입 승인 거절되었습니다]");
                    return;
                } else if (!"APPROVED".equals(status)) {
                    System.out.println("[승인 정보가 없습니다]");
                    return;
                }
            }
        } else if ("2".equals(loginType)) {
            success = memberControl.loginWarehouseAdmin(loginId, loginPw);
            if (success) {
                String status = memberControl.getWarehouseAdminApprovalStatus(loginId);
                if ("PENDING".equals(status)) {
                    System.out.println("[회원가입 승인 대기중입니다]");
                    return;
                } else if ("REJECTED".equals(status)) {
                    System.out.println("[회원가입 승인 거절되었습니다]");
                    return;
                } else if (!"APPROVED".equals(status)) {
                    System.out.println("[승인 정보가 없습니다]");
                    return;
                }
            }
        } else if ("3".equals(loginType)) {
            success = memberControl.loginTotalAdmin(loginId, loginPw);
        } else {
            System.out.println("올바른 로그인 유형을 선택하세요.");
            return;
        }

        if (success) {
            System.out.println("로그인 성공");
            currentUserId = loginId; // 로그인한 사용자 ID 저장
            
            // 사용자 이름 가져오기
            try {
                Object userInfo = memberControl.getUserInfo(loginId);
                financeControl.setLoggedInUser(userInfo);
                boardControl.setLoggedInUser(userInfo);
                if (userInfo instanceof domain.User) {
                    currentUserName = ((domain.User) userInfo).getUName();
                } else if (userInfo instanceof domain.WarehouseAdmin) {
                    currentUserName = ((domain.WarehouseAdmin) userInfo).getWaName();
                    warehouseAdmin = (domain.WarehouseAdmin) userInfo; //창고관리자 승인되면 객체 저장해주기
                    int wIdx = memberControl.getWarehouseAdminWIdx(warehouseAdmin.getWaId());
                    if(wIdx != 0){warehouseAdmin.setWIdx(wIdx);}// 0이면 현재 배당받은 창고가 없음
                } else if (userInfo instanceof domain.TotalAdmin) {
                    currentUserName = ((domain.TotalAdmin) userInfo).getTaName();
                }
            } catch (RuntimeException e) {
                ErrorHandler.displayAndLog("사용자 정보를 불러오는 중 오류가 발생했습니다. 기본 이름으로 표시합니다.", e);
                currentUserName = "사용자"; // 기본값
            }
            
            // 사용자 유형에 따른 대시보드 표시 및 메뉴 처리
            if ("1".equals(loginType)) {
                currentUserType = "user";
                handleUserDashboard();
            } else if ("2".equals(loginType)) {
                currentUserType = "warehouse_admin";
                handleWarehouseAdminDashboard();
            } else if ("3".equals(loginType)) {
                currentUserType = "total_admin";
                handleTotalAdminDashboard();
            }
        } else {
            System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
    }
    void loginPage(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<1: 회원가입>");
        System.out.println("<2: 로그인>");
        System.out.println("<3: 고객센터>");
        System.out.println("<4: 아이디 찾기>");
        System.out.println("<5: 비밀번호 찾기>");
        System.out.println("<Q(q): 프로그램 종료>");
        System.out.println("============================================================");
        System.out.println("1, 2, 3, 4, 5, Q(q) 중 입력하세요");
        System.out.println("============================================================");
        System.out.print("입력: ");
    }

    // 일반회원 대시보드 처리
    private void handleUserDashboard() {
        boolean inDashboard = true;
        while (inDashboard) {
            showUserDashboard();
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "0":
                    financeControl.logoutUser();
                    boardControl.logoutUser();
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    currentUserId = null;
                    currentUserName = null;
                    inDashboard = false;
                    break;
                case "1":
                    if (showUserMyPage()) {
                        // 회원탈퇴가 완료된 경우 대시보드 종료
                        inDashboard = false;
                    }
                    break;
                case "2":
                    System.out.println("입고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "3":
                    System.out.println("출고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "4":
                    // financeControl.showFinanceMenu()의 반환값에 따라 로그아웃 처리
                    if (!financeControl.showFinanceMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        boardControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                case "5":
                    // boardControl.showBoardMenu()의 반환값에 따라 로그아웃 처리
                    if (!boardControl.showBoardMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        financeControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                case "6":
                    WarehouseController warehouseController = WarehouseControllerImpl.getInstance();
                    warehouseController.showAllWarehouseList();
                    break;
                case "7":
                    StockController stockController = StockControllerImpl.getInstance();
                    stockController.setCurrentUser(currentUserType, null);
                    stockController.showStockSearchMenu();
                    break;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    // 일반회원 대시보드 화면
    private void showUserDashboard() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("WMS 프로젝트_4조");
        System.out.println("[ " + currentUserName + " ]님 환영합니다!");
        System.out.println("============================================================");
        System.out.println("<일반회원화면>");
        System.out.println("                                    0. 로그아웃     1. 마이페이지");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("2. 입고관리");
        System.out.println("3. 출고관리");
        System.out.println("4. 재무관리");
        System.out.println("5. 고객센터");
        System.out.println("6. 창고관리 - 창고현황리스트 조회");
        System.out.println("7. 재고관리 - 재고조회");
        System.out.println();
        System.out.println("============================================================");
        System.out.print("선택: ");
    }

    // 창고관리자 대시보드 처리
    private void handleWarehouseAdminDashboard() {
        boolean inDashboard = true;
        while (inDashboard) {
            showWarehouseAdminDashboard();
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "0":
                    financeControl.logoutUser();
                    boardControl.logoutUser();
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    currentUserId = null;
                    currentUserName = null;
                    inDashboard = false;
                    break;
                case "1":
                    if (showWarehouseAdminMyPage()) {
                        // 회원탈퇴가 완료된 경우 대시보드 종료
                        inDashboard = false;
                    }
                    break;
                case "2":
                    WarehouseController warehouseController = WarehouseControllerImpl.getInstance();
                    warehouseController.showWarehouseSearchMenu();
                    break;
                case "3":
                    StockController stockController = StockControllerImpl.getInstance();
                    stockController.setCurrentUser(currentUserType, warehouseAdmin);
                    stockController.showStockMenu();
                    break;
                case "4":
                    // financeControl.showFinanceMenu()의 반환값에 따라 로그아웃 처리
                    if (!financeControl.showFinanceMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        boardControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                case "5":
                    // boardControl.showBoardMenu()의 반환값에 따라 로그아웃 처리
                    if (!boardControl.showBoardMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        financeControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    // 창고관리자 대시보드 화면
    private void showWarehouseAdminDashboard() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("WMS 프로젝트_4조");
        System.out.println("[ " + currentUserName + " ]님 환영합니다!");
        System.out.println("============================================================");
        System.out.println("<창고관리자화면>");
        System.out.println("                                    0. 로그아웃     1. 마이페이지");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("2. 창고관리");
        System.out.println("3. 재고관리");
        System.out.println("4. 재무관리");
        System.out.println("5. 고객센터");
        System.out.println();
        System.out.println("============================================================");
        System.out.print("선택: ");
    }

    // 총관리자 대시보드 처리
    private void handleTotalAdminDashboard() {
        boolean inDashboard = true;
        while (inDashboard) {
            showTotalAdminDashboard();
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "0":
                    financeControl.logoutUser();
                    boardControl.logoutUser();
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    currentUserId = null;
                    currentUserName = null;
                    inDashboard = false;
                    break;
                case "1":
                    if (showTotalAdminMyPage()) {
                        // 회원탈퇴가 완료된 경우 대시보드 종료
                        inDashboard = false;
                    }
                    break;
                case "2":
                    handleMemberManagement();
                    break;
                case "3":
                    // financeControl.showFinanceMenu()의 반환값에 따라 로그아웃 처리
                    if (!financeControl.showFinanceMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        boardControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                case "4":
                    System.out.println("입고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "5":
                    System.out.println("출고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "6":
                    WarehouseController warehouseController = WarehouseControllerImpl.getInstance();
                    warehouseController.showWarehouseMenu();
                    break;
                case "7":
                    StockController stockController = StockControllerImpl.getInstance();
                    stockController.setCurrentUser(currentUserType, null);
                    stockController.showStockMenu();
                    break;
                case "8":
                    // boardControl.showBoardMenu()의 반환값에 따라 로그아웃 처리
                    if (!boardControl.showBoardMenu()) {
                        currentUserType = null;
                        currentUserId = null;
                        currentUserName = null;
                        financeControl.logoutUser();
                        inDashboard = false;
                    }
                    break;
                case "9":
                    showWarehouseAllocationMenu();
                    break;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    // 총관리자 대시보드 화면
    private void showTotalAdminDashboard() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("WMS 프로젝트_4조");
        System.out.println("[ " + currentUserName + " ]님 환영합니다!");
        System.out.println("============================================================");
        System.out.println("<총관리자화면>");
        System.out.println("                                    0. 로그아웃     1. 마이페이지");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("2. 회원관리");
        System.out.println("3. 재무관리");
        System.out.println("4. 입고관리");
        System.out.println("5. 출고관리");
        System.out.println("6. 창고관리");
        System.out.println("7. 재고관리");
        System.out.println("8. 고객센터");
        System.out.println("9. 창고배정");
        System.out.println();
        System.out.println("============================================================");
        System.out.print("선택: ");
    }

   //총관리자는 창고관리자의 창고를 배정해줄 수 있음
    private boolean showWarehouseAllocationMenu(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<창고 배정 화면>");
        System.out.println("============================================================");

        java.util.List<domain.Warehouse> notAssignedWarehouses;
        java.util.List<domain.WarehouseAdmin> notAssignedWarehouseAdmins;

        try {
            notAssignedWarehouseAdmins = memberControl.getNotAssignedWarehouseAdmins();
            notAssignedWarehouses = WarehouseServiceImpl.getInstance().getNotAssignedWarehouses();
        } catch (RuntimeException e) {
            util.ErrorHandler.displayAndLog("목록을 불러오는 중 오류가 발생했습니다.", e);
            return false;
        }

        System.out.println("============================================================");
        System.out.println("[창고관리자가 없는 창고] - 총 " + (notAssignedWarehouses == null ? 0 : notAssignedWarehouses.size()) + "명");
        System.out.println("============================================================");

        if (notAssignedWarehouses != null && !notAssignedWarehouses.isEmpty()) {
            for (domain.Warehouse w : notAssignedWarehouses) {
                System.out.println("창고번호: " + w.getWIdx() + ", 창고이름: " + w.getWName()
                        + ", 창고고유번호: " +w.getWUniqueNum());
            }
        } else {
            System.out.println("창고가 없습니다");
            return false;
        }

        System.out.println("============================================================");
        System.out.println("[창고번호를 할당받아야할 창고관리자] - 총 " + (notAssignedWarehouseAdmins== null ? 0 : notAssignedWarehouseAdmins.size()) + "명");
        System.out.println("============================================================");

        if (notAssignedWarehouseAdmins != null && !notAssignedWarehouseAdmins.isEmpty()) {
            for (domain.WarehouseAdmin wa: notAssignedWarehouseAdmins) {
                System.out.println("창고관리자 번호: " + wa.getWaIdx() + ", 이름: " + wa.getWaName()
                        + ", 연락처: " +wa.getWaPhone()+", 이메일: "+wa.getWaEmail());
            }
        } else {
            System.out.println("창고관리자가 없습니다.");
            return false;
        }

        System.out.println("============================================================");
        System.out.println("창고관리자를 할당할 창고번호를 선택해주세요.(종료시 exit를 입력해주세요)");
        System.out.print("선택: ");
        String input = scan.nextLine().trim();
        if (input.equals("exit")) {return false;}

        int wareIdx;
        try {
             wareIdx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해야합니다.");
            return false;
        }

        boolean flag = false;
        for(domain.Warehouse w : notAssignedWarehouses){
            if(w.getWIdx() == wareIdx){
                flag = true;
                break;
            }
        }

        if(!flag){
            System.out.println("리스트에 제시된 창고번호를 입력해야합니다.");
            return false;
        }

        System.out.println("============================================================");
        System.out.println("창고 관리자 번호를 선택해주세요.(종료시 exit를 입력해주세요)");
        System.out.print("선택: ");
        String input2 = scan.nextLine().trim();
        if (input2.equals("exit")) {return false;}

        int warehouseAdminIdx;
        try{
            warehouseAdminIdx = Integer.parseInt(input2);
        }catch(NumberFormatException e){
            System.out.println("숫자를 입력해야 합니다.");
            return false;
        }

        flag = false;
        for(domain.WarehouseAdmin wa : notAssignedWarehouseAdmins){
            if(wa.getWaIdx() == warehouseAdminIdx){
                flag = true;
                break;
            }
        }
        if(!flag){
            System.out.println("리스트에 제시된 창고관리자 번호를 입력하셔야 합니다.");
            return false;
        }
        System.out.println("============================================================");

        try{
            boolean result = memberControl.setWIdxToWarehouseAdmin(wareIdx, warehouseAdminIdx);
            if(result){
                System.out.println("창고관리자에게 창고 관리 권한을 부여했습니다.");
            }else{
                System.out.println("회원가입이 승인 안된 상태이거나 거부된 상태입니다.");
                return false;
            }
        }catch(RuntimeException e){
            util.ErrorHandler.displayAndLog("창고 관리 권한을 부여하는 중 문제가 발생했습니다.", e);
            return false;
        }
        return true;
    }

    // 총관리자: 회원관리(승인) 화면 및 처리
    private void handleMemberManagement() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<회원가입 승인 화면>");
        System.out.println("============================================================");

        java.util.List<domain.PendingUserApproval> pendingUsers;
        java.util.List<domain.PendingWarehouseAdminApproval> pendingAdmins;
        try {
            pendingUsers = memberControl.getPendingUserApprovals();
            pendingAdmins = memberControl.getPendingWarehouseAdminApprovals();
        } catch (RuntimeException e) {
            util.ErrorHandler.displayAndLog("승인 대기 목록을 불러오는 중 오류가 발생했습니다.", e);
            return;
        }

        System.out.println("============================================================");
        System.out.println("[일반회원가입 신청자] - 총 " + (pendingUsers == null ? 0 : pendingUsers.size()) + "명");
        System.out.println("============================================================");
        if (pendingUsers != null && !pendingUsers.isEmpty()) {
            int i = 1;
            for (domain.PendingUserApproval p : pendingUsers) {
                System.out.println(i++ + ". 회원번호: " + p.getUIdx() + ", 이름: " + p.getUName()
                        + ", 전화번호: " + p.getUPhone() + ", 이메일: " + p.getUEmail()
                        + ", 신청일: " + p.getCreatedAt());
            }
        } else {
            System.out.println("신청자가 없습니다.");
        }

        System.out.println("============================================================");
        System.out.println("[창고관리자 회원가입 신청자] - 총 " + (pendingAdmins == null ? 0 : pendingAdmins.size()) + "명");
        System.out.println("============================================================");
        if (pendingAdmins != null && !pendingAdmins.isEmpty()) {
            int i = 1;
            for (domain.PendingWarehouseAdminApproval p : pendingAdmins) {
                System.out.println(i++ + ". 창고관리자번호: " + p.getWaIdx() + ", 이름: " + p.getWaName()
                        + ", 전화번호: " + p.getWaPhone() + ", 이메일: " + p.getWaEmail()
                        + ", 신청일: " + p.getCreatedAt());
            }
        } else {
            System.out.println("신청자가 없습니다.");
        }

        System.out.println("============================================================");
        System.out.println("A : 회원가입 승인(Approved)");
        System.out.println("R : 회원가입 거절(Rejected)");
        System.out.println("Q : 종료");
        System.out.println("============================================================");
        System.out.println("1(일반회원가입승인), 2(창고관리자가입승인) 중 선택하세요.");
        System.out.print("선택 : ");
        String who = scan.nextLine().trim();

        if ("Q".equalsIgnoreCase(who)) return;

        System.out.print("A(승인) 또는 R(거절)을 입력하세요: ");
        String action = scan.nextLine().trim().toUpperCase();
        if (!("A".equals(action) || "R".equals(action))) {
            System.out.println("올바른 값을 입력하세요.");
            return;
        }

        System.out.print("처리할 항목의 순번을 입력하세요: ");
        String idxStr = scan.nextLine().trim();
        int seq;
        try {
            seq = Integer.parseInt(idxStr);
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        Integer aIdx = null;
        if ("1".equals(who)) {
            if (pendingUsers == null || seq < 1 || seq > pendingUsers.size()) {
                System.out.println("올바른 순번이 아닙니다.");
                return;
            }
            aIdx = pendingUsers.get(seq - 1).getAIdx();
        } else if ("2".equals(who)) {
            if (pendingAdmins == null || seq < 1 || seq > pendingAdmins.size()) {
                System.out.println("올바른 순번이 아닙니다.");
                return;
            }
            aIdx = pendingAdmins.get(seq - 1).getAIdx();
        } else {
            System.out.println("올바른 대상을 선택하세요.");
            return;
        }

        Integer approverTaIdx = null;
        try {
            Object info = memberControl.getUserInfo(currentUserId);
            if (info instanceof domain.TotalAdmin) {
                approverTaIdx = ((domain.TotalAdmin) info).getTaIdx();
            }
        } catch (RuntimeException e) {
            util.ErrorHandler.displayAndLog("승인자 정보를 조회하는 중 오류가 발생했습니다.", e);
        }

        try {
            memberControl.updateApprovalStatus(aIdx, "A".equals(action) ? "APPROVED" : "REJECTED", approverTaIdx);
            System.out.println(("A".equals(action) ? "승인" : "거절") + "이 완료되었습니다.");
        } catch (RuntimeException e) {
            util.ErrorHandler.displayAndLog("승인 처리 중 오류가 발생했습니다.", e);
        }
    }

    // 일반회원 마이페이지
    private boolean showUserMyPage() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<마이페이지>");
        System.out.println("============================================================");
        
        try {
            Object userInfo = memberControl.getUserInfo(currentUserId);
            if (userInfo instanceof domain.User) {
                domain.User user = (domain.User) userInfo;
                System.out.println("아이디: " + user.getUId());
                System.out.println("이메일: " + user.getUEmail());
                System.out.println("이름: " + user.getUName());
                System.out.println("전화번호: " + user.getUPhone());
                System.out.println("회원가입 일자: " + user.getCreatedAt());
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("사용자 정보를 불러오는 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("============================================================");
        System.out.println("1. 내 정보 수정");
        System.out.println("2. 비밀번호 수정");
        System.out.println("3. 회원탈퇴");
        System.out.println("============================================================");
        System.out.print("선택: ");
        
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1":
                return handleUserInfoUpdate();
            case "2":
                return handlePasswordUpdate();
            case "3":
                System.out.print("정말로 탈퇴하시겠습니까? (y/n): ");
                String confirm = scan.nextLine().trim().toLowerCase();
                if ("y".equals(confirm) || "yes".equals(confirm)) {
                    System.out.print("비밀번호를 입력하세요: ");
                    String password = scan.nextLine().trim();
                    try {
                        // 비밀번호 확인 후 탈퇴 처리
                        if (memberControl.loginUser(currentUserId, password)) {
                            memberControl.deleteUser(currentUserId);
                            System.out.println("회원탈퇴가 완료되었습니다. 자동으로 로그아웃됩니다.");
                            currentUserType = null;
                            currentUserId = null;
                            currentUserName = null;
                            return true; // 회원탈퇴 완료 시 true 반환
                        } else {
                            System.out.println("비밀번호가 일치하지 않습니다. 탈퇴가 취소되었습니다.");
                            return false;
                        }
                    } catch (RuntimeException e) {
                        ErrorHandler.displayAndLog("회원탈퇴 중 오류가 발생했습니다.", e);
                        return false;
                    }
                } else {
                    System.out.println("회원탈퇴가 취소되었습니다.");
                    return false;
                }
            default:
                System.out.println("올바른 메뉴를 선택하세요.");
                return false;
        }
    }

    // 창고관리자 마이페이지
    private boolean showWarehouseAdminMyPage() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<마이페이지>");
        System.out.println("============================================================");
        
        try {
            Object userInfo = memberControl.getUserInfo(currentUserId);
            if (userInfo instanceof domain.WarehouseAdmin) {
                domain.WarehouseAdmin admin = (domain.WarehouseAdmin) userInfo;
                System.out.println("아이디: " + admin.getWaId());
                System.out.println("이메일: " + admin.getWaEmail());
                System.out.println("이름: " + admin.getWaName());
                System.out.println("전화번호: " + admin.getWaPhone());
                System.out.println("회원가입 일자: " + admin.getCreatedAt());
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("사용자 정보를 불러오는 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("============================================================");
        System.out.println("1. 내 정보 수정");
        System.out.println("2. 회원탈퇴");
        System.out.println("============================================================");
        System.out.print("선택: ");
        
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1":
                return handleUserInfoUpdate();
            case "2":
                return handlePasswordUpdate();
            case "3":
                System.out.print("정말로 탈퇴하시겠습니까? (y/n): ");
                String confirm = scan.nextLine().trim().toLowerCase();
                if ("y".equals(confirm) || "yes".equals(confirm)) {
                    System.out.print("비밀번호를 입력하세요: ");
                    String password = scan.nextLine().trim();
                    try {
                        // 비밀번호 확인 후 탈퇴 처리
                        if (memberControl.loginWarehouseAdmin(currentUserId, password)) {
                            memberControl.deleteWarehouseAdmin(currentUserId);
                            System.out.println("회원탈퇴가 완료되었습니다. 자동으로 로그아웃됩니다.");
                            currentUserType = null;
                            currentUserId = null;
                            currentUserName = null;
                            return true; // 회원탈퇴 완료 시 true 반환
                        } else {
                            System.out.println("비밀번호가 일치하지 않습니다. 탈퇴가 취소되었습니다.");
                            return false;
                        }
                    } catch (RuntimeException e) {
                        ErrorHandler.displayAndLog("회원탈퇴 중 오류가 발생했습니다.", e);
                        return false;
                    }
                } else {
                    System.out.println("회원탈퇴가 취소되었습니다.");
                    return false;
                }
            default:
                System.out.println("올바른 메뉴를 선택하세요.");
                return false;
        }
    }

    // 총관리자 마이페이지
    private boolean showTotalAdminMyPage() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<마이페이지>");
        System.out.println("============================================================");
        
        try {
            Object userInfo = memberControl.getUserInfo(currentUserId);
            if (userInfo instanceof domain.TotalAdmin) {
                domain.TotalAdmin admin = (domain.TotalAdmin) userInfo;
                System.out.println("아이디: " + admin.getTaId());
                System.out.println("이메일: " + admin.getTaEmail());
                System.out.println("이름: " + admin.getTaName());
                System.out.println("전화번호: " + admin.getTaPhone());
                System.out.println("회원가입 일자: " + admin.getCreatedAt());
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("사용자 정보를 불러오는 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("============================================================");
        System.out.println("1. 내 정보 수정");
        System.out.println("2. 비밀번호 수정");
        System.out.println("3. 회원탈퇴");
        System.out.println("============================================================");
        System.out.print("선택: ");
        
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1":
                return handleUserInfoUpdate();
            case "2":
                return handlePasswordUpdate();
            case "3":
                System.out.print("정말로 탈퇴하시겠습니까? (y/n): ");
                String confirm = scan.nextLine().trim().toLowerCase();
                if ("y".equals(confirm) || "yes".equals(confirm)) {
                    System.out.print("비밀번호를 입력하세요: ");
                    String password = scan.nextLine().trim();
                    try {
                        // 비밀번호 확인 후 탈퇴 처리
                        if (memberControl.loginTotalAdmin(currentUserId, password)) {
                            memberControl.deleteTotalAdmin(currentUserId);
                            System.out.println("회원탈퇴가 완료되었습니다. 자동으로 로그아웃됩니다.");
                            currentUserType = null;
                            currentUserId = null;
                            currentUserName = null;
                            return true; // 회원탈퇴 완료 시 true 반환
                        } else {
                            System.out.println("비밀번호가 일치하지 않습니다. 탈퇴가 취소되었습니다.");
                            return false;
                        }
                    } catch (RuntimeException e) {
                        ErrorHandler.displayAndLog("회원탈퇴 중 오류가 발생했습니다.", e);
                        return false;
                    }
                } else {
                    System.out.println("회원탈퇴가 취소되었습니다.");
                    return false;
                }
            default:
                System.out.println("올바른 메뉴를 선택하세요.");
                return false;
        }
    }

    // 아이디 찾기
    private void handleFindId() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<아이디 찾기>");
        System.out.println("============================================================");
        System.out.print("이메일을 입력하세요: ");
        String email = scan.nextLine().trim();
        
        try {
            String result = memberControl.findIdByEmail(email);
            if (result != null) {
                System.out.println("찾은 아이디: " + result);
            } else {
                System.out.println("해당 이메일로 등록된 계정을 찾을 수 없습니다.");
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("아이디 찾기 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("메인 화면으로 돌아갑니다.");
    }

    // 비밀번호 찾기
    private void handleFindPassword() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<비밀번호 찾기>");
        System.out.println("============================================================");
        System.out.print("아이디를 입력하세요: ");
        String userId = scan.nextLine().trim();
        
        try {
            String result = memberControl.findPasswordById(userId);
            if (result != null) {
                System.out.println("회원 권한: " + result);
            } else {
                System.out.println("해당 아이디로 등록된 계정을 찾을 수 없습니다.");
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("비밀번호 찾기 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("비밀번호는 마이페이지에서 수정 가능합니다.");
    }

    // 내 정보 수정
    private boolean handleUserInfoUpdate() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<내 정보 수정>");
        System.out.println("============================================================");
        
        try {
            Object userInfo = memberControl.getUserInfo(currentUserId);
            if (userInfo == null) {
                System.out.println("사용자 정보를 불러올 수 없습니다.");
                return false;
            }
            
            String currentName = "";
            String currentPhone = "";
            
            if (userInfo instanceof domain.User) {
                domain.User user = (domain.User) userInfo;
                currentName = user.getUName();
                currentPhone = user.getUPhone();
            } else if (userInfo instanceof domain.WarehouseAdmin) {
                domain.WarehouseAdmin admin = (domain.WarehouseAdmin) userInfo;
                currentName = admin.getWaName();
                currentPhone = admin.getWaPhone();
            } else if (userInfo instanceof domain.TotalAdmin) {
                domain.TotalAdmin admin = (domain.TotalAdmin) userInfo;
                currentName = admin.getTaName();
                currentPhone = admin.getTaPhone();
            }
            
            System.out.println("현재 이름: " + currentName);
            System.out.println("현재 전화번호: " + currentPhone);
            System.out.println();
            System.out.print("새로운 이름을 입력하세요 (변경하지 않으려면 엔터): ");
            String newName = scan.nextLine().trim();
            if (newName.isEmpty()) {
                newName = currentName;
            } else{
                currentUserName = newName;
            }
            
            System.out.print("새로운 전화번호를 입력하세요 (변경하지 않으려면 엔터): ");
            String newPhone = scan.nextLine().trim();
            if (newPhone.isEmpty()) {
                newPhone = currentPhone;
            }
            
            boolean updated = memberControl.updateUserInfo(currentUserId, newName, newPhone);
            if (updated) {
                System.out.println("정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("정보 수정에 실패했습니다.");
            }
            
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("정보 수정 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("마이페이지로 돌아갑니다.");
        return false;
    }

    // 비밀번호 수정
    private boolean handlePasswordUpdate() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<비밀번호 수정>");
        System.out.println("============================================================");
        
        System.out.print("현재 비밀번호를 입력하세요: ");
        String currentPassword = scan.nextLine().trim();
        
        System.out.print("새로운 비밀번호를 입력하세요: ");
        String newPassword = scan.nextLine().trim();
        
        System.out.print("새로운 비밀번호를 다시 입력하세요: ");
        String confirmPassword = scan.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("새로운 비밀번호가 일치하지 않습니다.");
            System.out.println("마이페이지로 돌아갑니다.");
            return false;
        }
        
        try {
            boolean updated = memberControl.updateUserPassword(currentUserId, currentPassword, newPassword);
            if (updated) {
                System.out.println("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                System.out.println("현재 비밀번호가 일치하지 않거나 변경에 실패했습니다.");
            }
        } catch (RuntimeException e) {
            ErrorHandler.displayAndLog("비밀번호 변경 중 오류가 발생했습니다.", e);
        }
        
        System.out.println("마이페이지로 돌아갑니다.");
        return false;
    }

    static void info(){
        System.out.println("\n" +
                "============================================================\n" +
                "_____  _____  _____\\s\n" +
                "/  ___|/  ___||  __ \\\\\n" +
                "\\\\ `--. \\\\ `--. | |  \\\\/\n" +
                "`--. \\\\ `--. \\\\| | __\\s\n" +
                "/\\\\__/ //\\\\__/ /| |_\\\\ \\\\\n" +
                "\\\\____/ \\\\____/  \\\\____/\n" +
                "                _____  _      _         ___    ___       \\s\n" +
                "                |_   _|| |    (_)       /   |  |_  |      \\s\n" +
                "                  | |  | |__   _  ___  / /| |    | |  ___ \\s\n" +
                "                  | |  | '_ \\\\ | |/ __|/ /_| |    | | / _ \\\\\\s\n" +
                "                  | |  | | | || |\\\\__ \\\\\\\\___  |/\\\\__/ /| (_) |\n" +
                "                  \\\\_/  |_| |_||_||___/    |_/\\\\____/  \\\\___/\\s\n" +
                "");
    }
}
