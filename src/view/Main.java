package view;

import controller.MemberController;
import controller.MemberControllerImpl;

import java.util.Scanner;

public class Main {
    private MemberController memberControl;
    private Scanner scan;

    public Main() {
        memberControl = MemberControllerImpl.getInstance();
        scan = new Scanner(System.in);
    }
    void mainPage(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("WMS 프로젝트_4조                             1. 로그인          ");
        System.out.println("============================================================");
        System.out.println("1. 메뉴1         3. 메뉴2           4.  메뉴3           5. 메뉴4");
        System.out.println("============================================================");
        System.out.print("입력: ");
    }

    void loginPage(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<1: 회원가입>");
        System.out.println("<2: 로그인>");
        System.out.println("============================================================");
        System.out.println("1, 2 중 입력하세요"); // TODO: 1 입력하면 joinToMember(), 2 입력하면 login()
        System.out.println("============================================================");
        System.out.print("입력: ");
    }

    void joinToMember(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("<회원가입>");
        System.out.println("============================================================");
        System.out.println("1: 일반회원가입 / 2: 창고관리자 회원가입");
        System.out.println("선택: ");
        System.out.println("ID: ");
        System.out.println("PW: ");
        System.out.println("============================================================");
        System.out.print("입력: ");
    }

    void login(){
        System.out.println();
        System.out.println("============================================================");
        System.out.println("1: 일반회원 로그인 / 2: 창고관리자 로그인");
        System.out.println("선택: ");
        System.out.println("ID: ");
        System.out.println("PW: ");
        System.out.println("============================================================");
    }

    void failLogin(){
        System.out.println();
        System.out.println("============================================================");
        // 아이디 없는 경우 : System.out.println("<존재하지 않는 아이디입니다!>"); //출력 후 loginPage() 로 돌아가기
        // 아이디는 존재하는데 비밀번호가 틀린 경우 : System.out.println("<비밀번호를 확인해 주세요>"); 출력 후 login() 돌아가기
        System.out.println("============================================================");
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

    public static void main(String[] args) {
        info();
        Main app = new Main();

        boolean running = true;
        while (running) {
            app.loginPage();
            String input = app.scan.nextLine().trim();
            switch (input) {
                case "1":
                    // 회원가입 플로우 (일반회원만 우선 구현)
                    System.out.println();
                    System.out.println("============================================================");
                    System.out.print("회원 유형 선택 (1: 일반회원 / 2: 창고관리자): ");
                    String type = app.scan.nextLine().trim();
                    System.out.print("ID: ");
                    String id = app.scan.nextLine().trim();
                    System.out.print("PW: ");
                    String pw = app.scan.nextLine().trim();
                    System.out.print("이름: ");
                    String name = app.scan.nextLine().trim();
                    System.out.print("전화번호: ");
                    String phone = app.scan.nextLine().trim();
                    System.out.print("이메일: ");
                    String email = app.scan.nextLine().trim();

                    // TotalAdmin 지정(데모: 1 고정)
                    int taIdx = 1;

                    if ("1".equals(type)) {
                        domain.User user = new domain.User();
                        user.setUId(id);
                        user.setUserPw(pw);
                        user.setUName(name);
                        user.setUPhone(phone);
                        user.setUEmail(email);

                        try {
                            app.memberControl.requestSignup(user, taIdx);
                            System.out.println("회원형가입 신청이 접수되었습니다. (관리자 승인 대기)");
                        } catch (RuntimeException e) {
                            System.out.println("회원가입 신청 중 오류가 발생했습니다: " + e.getMessage());
                        }
                    } else if ("2".equals(type)) {
                        // 창고관리자 회원가입 입력 (wIdx, taIdx는 추후 로직에서 설정)
                        domain.WarehouseAdmin admin = new domain.WarehouseAdmin();
                        admin.setWaId(id);
                        admin.setWaPw(pw);
                        admin.setWaName(name);
                        admin.setWaPhone(phone);
                        admin.setWaEmail(email);
                        // admin.setWIdx(null);
                        // admin.setTaIdx(null);

                        try {
                            app.memberControl.requestSignupWarehouse(admin, taIdx);
                            System.out.println("창고관리자 회원가입 신청이 접수되었습니다. (관리자 승인 대기)");
                        } catch (RuntimeException e) {
                            System.out.println("회원가입 신청 중 오류가 발생했습니다: " + e.getMessage());
                        }
                    } else {
                        System.out.println("올바른 회원 유형을 선택하세요.");
                    }
                    break;
                case "2":
                    // 로그인 플로우 자리표시자
                    System.out.println();
                    System.out.println("============================================================");
                    System.out.println("로그인은 추후 구현 예정입니다.");
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
        System.out.println("WMS 앱 종료");
    }
}
