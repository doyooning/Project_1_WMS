package controller;

import domain.*;
import service.BoardService;
import service.BoardServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class BoardControllerImpl implements BoardController {
    private Object userInfo;
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
        userInfo = user;
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
                >\t""");
    }
    private void showUserMenu() {
        //총관리자, 창고관리자. 일반회원 화면
        System.out.print("""
                ============================================================
                                          고객센터
                ============================================================
                 1. 공지사항  |  2. 문의글  |  3. 메인 메뉴  |  4. 로그아웃
                ============================================================
                >\t""");
    }
    private void showAnnouncementMenu(){
        List<Announcement> list = getAnnouncementList();
        printAnnouncementList(list);
        if(totalAdmin != null){
            System.out.print("""
                    ============================================================
                      1. 공지사항 상세 조회 | 2. 공지사항 작성 | 3. 고객센터 메뉴
                    ============================================================
                    >\t""");
            selectAnTaMenu();
        } else {
            System.out.print("""
                ============================================================
                 1. 공지사항 상세 조회   |   2. 고객센터 메뉴
                ============================================================
                >\t""");
            selectAnMenu();
        }
    }
    private void showAnManagementMenu(){
        System.out.print("""
                ============================================================
                 1. 공지사항 수정  |  2. 공지사항 삭제  |  3. 고객센터 메뉴
                ============================================================
                >\t""");
        selectAnMgMenu();
    }
    private void showIqMenu(){
        List<Inquiry> list = getInquiryList();
        printInquiryList(list);
        if(user != null){
            System.out.print("""
                    ============================================================
                      1. 문의글 상세 조회 | 2. 문의글 작성 | 3. 고객센터 메뉴
                    ============================================================
                    >\t""");
            selectIqUserMenu();
        } else {
            System.out.print("""
                ============================================================
                 1. 공지사항 상세 조회   |   2. 고객센터 메뉴
                ============================================================
                >\t""");
            selectIqMenu();
        }
    }
    private void showIqMgMenu(Inquiry inquiry){
        System.out.print("""
                    ============================================================
                      1. 문의글 수정 | 2. 문의글 삭제 | 3. 고객센터 메뉴
                    ============================================================
                    >\t""");
        selectIqMgMenu(inquiry);
    }
    private void showRsMgMenu(Inquiry inquiry){
        if(inquiry.getResponse() != null){
            System.out.print("""
                    ============================================================
                      1. 답변 수정 | 2. 답변 삭제 | 3. 고객센터 메뉴
                    ============================================================
                    >\t""");
            selectRsMgMenu(inquiry);
        } else{
            System.out.print("""
                    ============================================================
                      1. 답변 작성 | 2. 고객센터 메뉴
                    ============================================================
                    >\t""");
            selectRsMgMenu(inquiry);
        }
    }


    //권한별 메뉴선택 및 메서드 호출
    private String selectNonUserMenu(){
        try {
            List<Announcement> list = getAnnouncementList();
            printAnnouncementList(list);
            String num  = input.readLine().trim();
            switch (num) {
                case "1" -> System.out.println("비회원 문의글 조회");
                case "2" -> System.out.println("비회원 문의글 작성");
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
                case "2" -> showIqMenu();
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
                case "2" -> showIqMenu();
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
                case "2" -> showIqMenu();
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
    private void selectAnTaMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetAnnouncementDetail();
                case "2" -> handleAddAnnouncement();
                case "3" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectAnMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetAnnouncementDetail();
                case "2" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectAnMgMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleModifyAnnouncement();
                case "2" -> handleRemoveAnnouncement();
                case "3" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectIqUserMenu(){
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetInquiryDetail();
                case "2" -> handleAddInquiry();
                case "3" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectIqMenu() {
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleGetInquiryDetail();
                case "2" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectIqMgMenu(Inquiry inquiry) {
        try {
            String num = input.readLine().trim();
            switch (num) {
                case "1" -> handleModifyInquiry(inquiry.getIqIdx());
                case "2" -> handleRemoveInquiry(inquiry.getIqIdx());
                case "3" -> System.out.println();
                default -> System.out.println("번호를 잘못 입력했습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void selectRsMgMenu(Inquiry inquiry) {
        if(inquiry.getResponse() == null) {
            try {
                String num = input.readLine().trim();
                switch (num) {
                    case "1" -> handleAddResponse(inquiry.getIqIdx());
                    case "2" -> System.out.println();
                    default -> System.out.println("번호를 잘못 입력했습니다.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            try {
                String num = input.readLine().trim();
                switch (num) {
                    case "1" -> handleModifyResponse(inquiry.getIqIdx());
                    case "2" -> handleRemoveResponse(inquiry.getIqIdx());
                    case "3" -> System.out.println();
                    default -> System.out.println("번호를 잘못 입력했습니다.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
    @Override
    public Boolean modifyAnnouncement(Announcement announcement) {
        return board.modifyAnnouncement(announcement);
    }
    @Override
    public Boolean removeAnnouncement(int anIdx) {
        return board.removeAnnouncement(anIdx);
    }
    @Override
    public List<Inquiry> getInquiryList() {
        return board.getInquiryList();
    }
    @Override
    public Inquiry getInquiry(Object userInfo, int iqIdx) {
        return board.getInquiry(userInfo, iqIdx);
    }
    @Override
    public Boolean addInquiry(Inquiry inquiry) {
        return board.addInquiry(inquiry);
    }
    @Override
    public Boolean modifyInquiry(Inquiry inquiry) {
        return board.modifyInquiry(inquiry);
    }
    @Override
    public Boolean removeInquiry(int iqIdx) {
        return board.removeInquiry(iqIdx);
    }
    @Override
    public Boolean addResponse(Response response) {
        return board.addResponse(response);
    }
    @Override
    public Boolean modifyResponse(Response response) {
        return board.modifyResponse(response);
    }



    private void handleGetAnnouncementDetail() {
        int anIdx = getAnIdx();

        Announcement announcement = getAnnouncement(anIdx);
        printAnnouncementDetail(announcement);

        // 수정 삭제
        if(totalAdmin == null || announcement == null) {
        }
        else showAnManagementMenu();
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
    private void handleModifyAnnouncement() {
        int anIdx = getAnIdx();
        int taIdx = totalAdmin.getTaIdx();
        String title = getTitle();
        String content = getContent();

        Announcement announcement = new Announcement();
        announcement.setAnIdx(anIdx);
        announcement.setTaIdx(taIdx);
        announcement.setAnTitle(title);
        announcement.setAnContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = modifyAnnouncement(announcement);
            if(result == true) System.out.println("공지사항이 수정되었습니다.");
        } catch (Exception e) {
            System.out.println("공지사항 수정에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleRemoveAnnouncement() {
        int anIdx = getAnIdx();

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = removeAnnouncement(anIdx);
            if(result == true) System.out.println("공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("공지사항 삭제에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleGetInquiryDetail() {
        int ipIdx = getiqIdx();
        Inquiry inquiry = getInquiry(userInfo, ipIdx);
        if (inquiry != null) {
            System.out.println("찾으신 문의글이 존재하지 않습니다.");
        } else if (ipIdx == 0) {
            System.out.println("1:1 문의글입니다.");
        } else {
            printInquiryDetail(inquiry);
            if(totalAdmin != null){
                showRsMgMenu(inquiry);
            } else if(user != null && inquiry.getUIdx() == user.getUIdx()){
                showIqMgMenu(inquiry);
            } else {
                System.out.println();
            }
        }
    }
    private void handleModifyInquiry(int iqIdx){
        Inquiry inquiry = new Inquiry();
        char type = getType();
        String title = getTitle();
        String content = getContent();

        inquiry.setIqIdx(iqIdx);
        inquiry.setIqType(type);
        inquiry.setIqTitle(title);
        inquiry.setIqContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = modifyInquiry(inquiry);
            if(result == true) System.out.println("문의글이 수정되었습니다.");
        } catch (Exception e) {
            System.out.println("문의글 수정에 실패했습니다: " + e.getMessage());
        }

    }
    private void handleRemoveInquiry(int iqIdx){
        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = removeInquiry(iqIdx);
            if(result == true) System.out.println("문의글이 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("문의글 삭제에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleAddInquiry(){
        Inquiry inquiry = new Inquiry();
        inquiry.setUIdx(user.getUIdx());

        char type = getType();
        String title = getTitle();
        String content = getContent();

        inquiry.setIqType(type);
        inquiry.setIqTitle(title);
        inquiry.setIqContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = addInquiry(inquiry);
            if(result == true) System.out.println("문의글이 등록되었습니다.");
        } catch (Exception e) {
            System.out.println("문의글 등록에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleAddResponse(int iqIdx){
        Response response = new Response();
        response.setIqIdx(iqIdx);
        response.setTaIdx(totalAdmin.getTaIdx());

        String content = getContent();
        response.setRContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = addResponse(response);
            if(result == true) System.out.println("문의 답변이 등록되었습니다.");
        } catch (Exception e) {
            System.out.println("문의 답변 등록에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleModifyResponse(int iqIdx){
        Response response = new Response();
        response.setIqIdx(iqIdx);

        String content = getContent();
        response.setRContent(content);

        Boolean tf = getConfirm();
        if(tf==false) return;
        try {
            // API 메서드 호출
            Boolean result = modifyResponse(response);
            if(result == true) System.out.println("문의 답변이 수정되었습니다.");
        } catch (Exception e) {
            System.out.println("문의 답변 수정에 실패했습니다: " + e.getMessage());
        }
    }
    private void handleRemoveResponse(int iqIdx){}



    private void printAnnouncementList(List<Announcement> list){
        System.out.println("[공지사항 목록]");
        System.out.printf(" %10s | %20s | %5s | %10s \n", "공지사항 번호", "글제목", "작성자", "작성일");
        System.out.println("-".repeat(60));
        for (Announcement a : list) {
            System.out.printf(" %5d | %20s | %5d | %10s \n", a.getAnIdx(), a.getAnTitle(), a.getTaIdx(), a.getUpdatedAt());
        }
        System.out.println("-".repeat(60));
    }
    private void printAnnouncementDetail(Announcement announcement) {
        if (announcement == null) {
            System.out.println("해당 공지사항이 존재하지 않습니다.");
            return;
        }
        System.out.printf(" %10s | %-40s \n", "제목", announcement.getAnTitle());
        System.out.println("-".repeat(60));
        System.out.printf(" %10s | %-40s \n", "작성자", announcement.getTaIdx());
        System.out.printf(" %10s | %-40s \n", "작성일", announcement.getUpdatedAt());
        System.out.println("-".repeat(60));
        System.out.println(" 내용");
        System.out.println( announcement.getAnContent());
    }
    private void printInquiryList(List<Inquiry> list){
        System.out.println("[문의글 목록]");
        System.out.printf(" %6s | %15s | %5s | %10s | %5s \n", "문의글 번호", "글제목", "작성자", "작성일", "타입");
        System.out.println("-".repeat(60));
        for (Inquiry i : list) {
            String type = i.getIqType() == '0' ? "일반" : "1:1";
            System.out.printf(" %6s | %15s | %5s | %10s | %5s \n", i.getIqIdx(), i.getIqTitle(), i.getUIdx(), i.getUpdatedAt(), type);
        }
        System.out.println("-".repeat(60));
    }
    private void printInquiryDetail(Inquiry inquiry) {
        if (inquiry == null) {
            System.out.println("해당 문의글이 존재하지 않습니다.");
            return;
        }
        System.out.printf(" %10s | %-40s \n", "제목", inquiry.getIqTitle());
        System.out.println("-".repeat(60));
        System.out.printf(" %10s | %-40s \n", "작성자", inquiry.getUIdx());
        System.out.printf(" %10s | %-40s \n", "작성일", inquiry.getUpdatedAt());
        System.out.println("-".repeat(60));
        System.out.println(" 내용");
        System.out.println( inquiry.getIqContent());
        if(inquiry.getResponse() != null){
            printResponseDetail(inquiry.getResponse());
        }
    }
    private void printResponseDetail(Response response) {
        if (response == null) {
            System.out.println("해당 답변은 존재하지 않습니다.");
            return;
        }
        System.out.println("-".repeat(60));
        System.out.println("[답변]");
        System.out.printf(" %10s | %-40s \n", "작성자", response.getTaIdx());
        System.out.printf(" %10s | %-40s \n", "작성일", response.getUpdatedAt());
        System.out.println("-".repeat(60));
        System.out.println(" 내용");
        System.out.println( response.getRContent());
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
    private int getiqIdx(){
        while(true) {
            try {
                int anIdx = Integer.parseInt(inputNum("문의글번호> "));
                System.out.println("=".repeat(60));
                return anIdx;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private char getType(){
        while(true) {
            String type = inputNum("""
                    ============================================================
                      1. 1:1 문의 | 2. 일반 문의
                    ============================================================
                    >\t""");
            switch (type) {
                case "1" -> {
                    return '0';
                }
                case "2" -> {
                    return '1';
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
