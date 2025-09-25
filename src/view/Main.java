package view;

import controller.MemberController;
import controller.MemberControllerImpl;

import java.util.Scanner;

public class Main {
    private MemberController memberControl;
    private Scanner scan;
    private String currentUserType; // 현재 로그인한 사용자 유형 저장

    public Main() {
        memberControl = MemberControllerImpl.getInstance();
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
                case "q":
                case "Q":
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("올바른 값을 입력하세요 (1, 2, q)");
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
                System.out.println("회원가입 신청 중 오류가 발생했습니다: " + e.getMessage());
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
                System.out.println("회원가입 신청 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
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
        } else if ("2".equals(loginType)) {
            success = memberControl.loginWarehouseAdmin(loginId, loginPw);
        } else if ("3".equals(loginType)) {
            success = memberControl.loginTotalAdmin(loginId, loginPw);
        } else {
            System.out.println("올바른 로그인 유형을 선택하세요.");
            return;
        }

        if (success) {
            System.out.println("로그인 성공");
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
        System.out.println("<Q(q): 프로그램 종료>");
        System.out.println("============================================================");
        System.out.println("1, 2, 3, Q(q) 중 입력하세요"); // TODO: 1 입력하면 joinToMember(), 2 입력하면 login()
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
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    inDashboard = false;
                    break;
                case "1":
                    System.out.println("마이페이지 기능은 추후 구현 예정입니다.");
                    break;
                case "2":
                    System.out.println("입고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "3":
                    System.out.println("출고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "4":
                    System.out.println("재무관리 기능은 추후 구현 예정입니다.");
                    break;
                case "5":
                    System.out.println("고객센터 기능은 추후 구현 예정입니다.");
                    break;
                case "6":
                    System.out.println("창고현황리스트 조회 기능은 추후 구현 예정입니다.");
                    break;
                case "7":
                    System.out.println("재고조회 기능은 추후 구현 예정입니다.");
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
        System.out.println("WMS 프로젝트_4조                       <일반회원화면>                   0. 로그아웃     1. 마이페이지");
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
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    inDashboard = false;
                    break;
                case "1":
                    System.out.println("마이페이지 기능은 추후 구현 예정입니다.");
                    break;
                case "2":
                    System.out.println("창고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "3":
                    System.out.println("재고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "4":
                    System.out.println("재무관리 기능은 추후 구현 예정입니다.");
                    break;
                case "5":
                    System.out.println("고객센터 기능은 추후 구현 예정입니다.");
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
        System.out.println("WMS 프로젝트_4조                    <창고관리자화면>                  0. 로그아웃     1. 마이페이지");
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
                    System.out.println("로그아웃합니다.");
                    currentUserType = null;
                    inDashboard = false;
                    break;
                case "1":
                    System.out.println("마이페이지 기능은 추후 구현 예정입니다.");
                    break;
                case "2":
                    System.out.println("회원관리 기능은 추후 구현 예정입니다.");
                    break;
                case "3":
                    System.out.println("재무관리 기능은 추후 구현 예정입니다.");
                    break;
                case "4":
                    System.out.println("입고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "5":
                    System.out.println("출고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "6":
                    System.out.println("창고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "7":
                    System.out.println("재고관리 기능은 추후 구현 예정입니다.");
                    break;
                case "8":
                    System.out.println("고객센터 기능은 추후 구현 예정입니다.");
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
        System.out.println("WMS 프로젝트_4조                    <총관리자 화면>                    0. 로그아웃     1. 마이페이지");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("2. 회원관리");
        System.out.println("3. 재무관리");
        System.out.println("4. 입고관리");
        System.out.println("5. 출고관리");
        System.out.println("6. 창고관리");
        System.out.println("7. 재고관리 - 재고실사등록/재고실사수정 제외");
        System.out.println("8. 고객센터");
        System.out.println();
        System.out.println("============================================================");
        System.out.print("선택: ");
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
