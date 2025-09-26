package controller;

import domain.*;
import service.BoardService;
import service.BoardServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class BoardControllerImpl implements BoardController {

    private User user;
    private WarehouseAdmin whAdmin;
    private TotalAdmin totalAdmin;
    private int authority = 0;
    //private boolean loop = true;
    //Service 객체
    private BoardServiceImpl board;
    //사용자 입력
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    // 싱글톤 패턴 적용
    private static BoardControllerImpl instance;
    private BoardControllerImpl() {
        this.board = BoardServiceImpl.getInstance();
    }
    public static BoardControllerImpl getInstance() {
        if (instance == null) instance = new BoardControllerImpl();
        return instance;
    }

    @Override
    public void setLoggedInUser(Object user) {
        if (user instanceof TotalAdmin) {
            this.totalAdmin = (TotalAdmin) user;
            this.authority = 1;
        } else if (user instanceof WarehouseAdmin) {
            this.whAdmin = (WarehouseAdmin) user;
            this.authority = 2;
        } else if (user instanceof User) {
            this.user = (User) user;
            this.authority = 3;
        }
    }

    //메인 화면 출력 메서드, 권한에 따라 다른 메서드로 화면 출력
    @Override
    public Boolean showBoardMenu() {
        while(true) {
            String choice;
            switch (authority) {
                case 0:
                    showNonUserMenu();
                    choice = selectNonUserMenu();
                    if ("mainMenu".equals(choice)) return true;
                    break;
                case 1:
                    showUserMenu();
                    choice = selectTotalAdminMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) return false;
                    break;
                case 2:
                    showUserMenu();
                    choice = selectWhAdminMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) return false;
                    break;
                case 3:
                    showUserMenu();
                    choice = selectUserMenu();
                    if ("mainMenu".equals(choice)) return true;
                    if ("logout".equals(choice)) return false;
                    break;
                default:
                    System.out.println("접속 불가! 권한이 존재하지 않습니다.");
            }
        }
    }

    private void showNonUserMenu() {
        //비회원 화면
        System.out.println("""
                ============================================================
                                         고객센터
                ============================================================
                 1. 문의글 조회  |  2. 문의글 작성  |  3. 메인 메뉴
                ============================================================
                >   """);
    }
    private void showUserMenu() {
        //총관리자, 창고관리자. 일반회원 화면
        System.out.print("""
                ============================================================
                                          고객센터
                ============================================================
                 1. 공지사항  |  2. 문의글  |  3. 메인 메뉴  |  4. 로그아웃
                ============================================================
                >  """);
    }
    private void showAnnouncementMenu(){
        if(totalAdmin != null){
            System.out.println("""
                    ============================================================
                      1. 공지사항 조회   |   2. 공지사항 작성  |  3. 메인
                    ============================================================
                    """);
            List<Announcement> list = getAnnouncementList();
            printAnnouncementList(list);
            System.out.print(">  ");
        } else {
            System.out.println("""
                ============================================================
                 1. 공지사항 조회   |   2. 고객센터 메뉴
                ============================================================
                """);
            List<Announcement> list = getAnnouncementList();
            printAnnouncementList(list);
            System.out.print(">  ");
        }
    }


    //권한별 메뉴선택 및 메서드 호출
    private String selectNonUserMenu(){
        try {
            List<Announcement> list = getAnnouncementList();
            printAnnouncementList(list);
            String num  = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetAnnouncementDetail();
                case "2" -> handleAddAnnouncement();
                case "3" -> {return "mainMenu";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return "continue";
    }
    private String selectTotalAdminMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> showAnnouncementMenu();
                case "2" -> System.out.println("문의글");
                case "3" -> {return "mainMenu";}
                case "4" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "continue";
    }
    private String selectWhAdminMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> showAnnouncementMenu();
                case "2" -> System.out.println("문의글");
                case "3" -> {return "mainMenu";}
                case "4" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "continue";
    }
    private String selectUserMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> showAnnouncementMenu();
                case "2" -> System.out.println("문의글");
                case "3" -> {return "mainMenu";}
                case "4" -> {
                    System.out.println("Logout");
                    return "logout";}
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "continue";
    }

    @Override
    public List<Announcement> getAnnouncementList() {
        return board.getAnnouncementList();
    }
    @Override
    public Boolean addAnnouncement(Announcement announcement) {
        return board.addAnnouncement(announcement);
    }
    @Override
    public Announcement getAnnouncement(int anIdx) {
        return board.getAnnouncement(anIdx);
    }


    private void handleGetAnnouncementDetail() {
        int anIdx = getAnIdx();

        Announcement announcement = getAnnouncement(anIdx);
        printAnnouncementDetail(announcement);
    }
    private void handleAddAnnouncement() {
        int taIdx = totalAdmin.getTaIdx();
        String title = getTitle();
        String content = getContent();

        Announcement announcement = new Announcement();
        announcement.setTaIdx(taIdx);
        announcement.setAnTitle(title);
        announcement.setAnContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = addAnnouncement(announcement);
            if(result == true) System.out.println("공지사항이 등록되었습니다.");
        } catch (Exception e) {
            System.out.println("공지사항 등록에 실패했습니다: " + e.getMessage());
        }
    }

    private void printAnnouncementList(List<Announcement> list){
        System.out.println("[공지사항 목록]");
        System.out.printf(" %10s | %20s | %5s | %10s \n", "공지사항 번호", "글제목", "작성자", "작성일");
        System.out.println("-".repeat(60));
        for (Announcement a : list) {
            System.out.printf(" %5d | %20s | %5d | %10s \n", a.getAnIdx(), a.getAnTitle(), a.getTaIdx(), a.getUpdatedAt());
        }
        System.out.println("=".repeat(60));
    }
    private void printAnnouncementDetail(Announcement announcement) {
        System.out.printf(" %10s | %-40s \n", "제목", announcement.getAnTitle());
        System.out.println("-".repeat(60));
        System.out.printf(" %10s | %-40s \n", "작성자", announcement.getTaIdx());
        System.out.printf(" %10s | %-40s \n", "작성일", announcement.getUpdatedAt());
        System.out.println("-".repeat(60));
        System.out.println(" 내용");
        System.out.println( announcement.getAnContent());
    }

    private int getAnIdx(){
        while(true) {
            try {
                int anIdx = Integer.parseInt(inputNum("공지사항번호> "));
                System.out.println("=".repeat(60));
                return anIdx;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String getTitle(){
        return inputNum("title>  ");
    }
    private String getContent(){
        StringBuffer text = new StringBuffer();
        System.out.println("[q 입력 시 작성 완료] content>  ");
        while(true){
            String content = inputNum(">  ");
            if(content.toLowerCase().equals("q")) break;
            text.append(content).append("\n");
        }
        return text.toString();
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


//    private String getWriter(){
//        return inputNum("작성자> ");
//    }
//    private String getPassword(){
//        return inputNum("비밀번호> ");
//    }

    private String inputNum(String msg){
        System.out.print(msg);
        try {
            return input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
