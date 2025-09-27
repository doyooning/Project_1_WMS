package controller;

import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;
import domain.WarehouseAdmin;
import service.StockService;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class StockController {
    private StockService stockService = StockService.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String currentUser;
    private WarehouseAdmin warehouseAdmin;
    /**
     * 싱글톤 패턴 적용
     */
    private static StockController stockController;

    private StockController() {}
    public static StockController getInstance() {
        if (stockController == null) stockController = new StockController();

        return stockController;
    }

    public void setCurrentUser(String currentUser, WarehouseAdmin warehouseAdmin) {
        this.currentUser = currentUser;
        this.warehouseAdmin = warehouseAdmin; //만약에 창고관리자나 일반회원이 접속하면 그냥 null이 들어감
    }

    public void showStockMenu(){
        //총관리자, 창고관리자만 볼 수 있음
        //회원은 바로 재고 조회 메뉴로 넘어가게 구성
        while(true){
            System.out.println(
                    """
                    ============================================================
                    ========================재고 전체 메뉴=========================
                    이용할 서비스를 선택해주세요.
                    1. 재고 조회
                    2. 재고 실사
                    ------------------------------------------------------------
                    """
            );
            boolean flag = selectStockMenu();
            if(!flag) break;
        }
    }

    public boolean selectStockMenu() {
        //총관리자, 창고관리자만 실행가능한 메소드
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().matches("[12]")) {
                    System.out.println("1-2까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요. ");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return false;

                stockService = StockService.getInstance();
                switch(input.trim()){
                    case "1" -> showStockSearchMenu();
                    case "2" -> showStockChecklogMenu();
                }

                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    public void showStockSearchMenu(){
        //총관리자, 창고관리자, 회원
        String menu = """
                ============================================================
                ==========================재고 조회==========================
                재고 조회 메뉴를 선택해주세요.
                1. 전체 재고 조회
                2. 대분류별 재고 조회
                3. 중분류별 재고 조회
                4. 소분류별 재고 조회
                5. 품목 현황 조회
                ------------------------------------------------------------
                """;

        if(currentUser.equals("User")){//회원은 이 화면부터 시작하므로..
            while(true){
                System.out.println(menu);

                boolean flag  = selectStockSearchMenu();
                if(!flag) break;
            }
        }else{
            System.out.println(menu);
            selectStockSearchMenu();
        }
    }

    private boolean selectStockSearchMenu() {
        while(true){
           try{
               System.out.print("> ");
               String input = reader.readLine();

               if(!input.trim().matches("[1-5]")) {
                   System.out.println("1-5까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
               }else if(input.trim().toLowerCase().equals("exit")) return false;

               switch(input.trim()){
                   case "1" -> showAllStockList();
                   case "2" -> showCategoryStockList(2);
                   case "3" -> showCategoryStockList(3);
                   case "4" -> showCategoryStockList(4);
                   case "5" -> showProductStockList();
               }
               break;
           }catch(IOException e){
               e.printStackTrace();
           }
        }
        return true;
    }

    private void showAllStockList() {
        //총관리자, 창고관리자, 회원 조회 가능
        //바코드별로 한 페이지 차지 하게끔 구현
        System.out.println(
                """
                ============================================================
                ========================전체 재고 조회=======================
                """
        );
        List<Stock> stockList = stockService.getAllStockList();
        if(stockList == null) {
            System.out.println(" 전체 재고 조회를 실패했습니다."); return;
        }

        printStockList(stockList, 1);
    }

    private void showCategoryStockList(int num){
        //총관리자, 창고관리자, 회원 조회 가능
        String menu = null;
        switch(num){
            case 2 -> menu = "대분류";
            case 3 -> menu = "중분류";
            case 4 -> menu = "소분류";
        }

        System.out.printf("""
                ============================================================
                =======================%s별 재고 조회=====================
                """, menu
        );
        System.out.printf("조회하실 %s를 입력해주세요.\n", menu);
        String category = getCategory();
        if(category.equals("exit")) return;

        List<Stock> stockList = stockService.getCategoryStockList(num, category);
        if(stockList == null) {
            System.out.println("카테고리별 재고 조회를 실패했습니다."); return;
        }

        printStockList(stockList, num);
    }

    private String getCategory(){
        String input;
        while(true){
            try{
                System.out.print("> ");
                input = reader.readLine();

                if(!input.trim().matches("^[가-힣\\s]+$")){
                    System.out.println("카테고리 이름 형식에 맞지 않습니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return "exit";
                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return input;
    }

    private void showProductStockList() {
        //총관리자, 창고관리자, 회원 조회 가능
        System.out.println(
                """
                ============================================================
                =====================품목별 재고현황 조회=====================
                """
        );

        String pIdx = getPIdx();
        if(pIdx.equals("exit")) return;
        List<Stock> stockList = stockService.getProductStockList(pIdx);
        if(stockList == null) {
            System.out.println("품목별 재고현황 조회에 실패했습니다."); return;
        }
        printStockList(stockList, 5);
    }

    private String getPIdx(){
        System.out.println("조회할 바코드 번호를 입력해주세요.");
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().matches("^[1-9][0-9]{12}$")){
                    System.out.println("바코드 번호 형식에 맞지 않습니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return "exit";

                return input;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 재고 실사
     */

    private void showStockChecklogMenu(){
        //재고실사는 총관리자와 창고관리자만 메뉴에 접근할 수 있지만, 보여지는 메뉴가 다름
        String menu = null;
        if(currentUser.equals("TotalAdmin")) {
            //총관리자
            menu =  """
                ============================================================
                ==========================재고 실사==========================
                재고 실사 메뉴를 선택해주세요.
                1. 재고 실사 삭제
                2. 재고 실사 조회
                ------------------------------------------------------------
                """ ;
        }
        else if(currentUser.equals("WarehouseAdmin")) {
            menu = """
                ============================================================
                ==========================재고 실사==========================
                재고 실사 메뉴를 선택해주세요.
                1. 재고 실사 등록
                2. 재고 실사 삭제
                3. 재고 실사 조회
                4. 재고 실사 수정
                ------------------------------------------------------------
                """ ;
        }

        //메뉴 출력
        System.out.println(menu);
        selecStockChecklogMenu();
    }

    private void selecStockChecklogMenu(){
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                switch(currentUser){
                    case "TotalAdmin" -> {
                        //총관리자일 때는 메뉴가 2개까지 밖에 없으니
                        if(!input.trim().matches("[12]")) {
                            System.out.println("1-2까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요. ");
                            continue;
                        }else if(input.trim().toLowerCase().equals("exit")) return;

                        stockService = StockService.getInstance();
                        switch(input.trim()){
                            case "1" -> showDeleteChecklogMenu();//재고실사 삭제메뉴
                            case "2" -> showChecklogSearchMenu(); //재고 실사 조회 메뉴
                        }
                    }

                    case "WarehouseAdmin" -> {
                        //창고관리자 일때는 메뉴가 4개임
                        if(!input.trim().matches("[1-4]")){
                            System.out.println("1-4까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요. ");
                            continue;
                        }else if(input.trim().toLowerCase().equals("exit")) return;

                        stockService = StockService.getInstance();
                        switch(input.trim()){
                            case "1" -> showAddChecklogMenu(); //재고실사등록
                            case "2" -> showDeleteChecklogMenu();//재고실사삭제
                            case "3" -> showChecklogSearchMenu();//재고실사조회
                            case "4" -> showUpdateChecklogMenu();//재고실사수정
                        }
                    }
                }
                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void showAddChecklogMenu(){
        //창고관리자만
        System.out.println(
                """
                ============================================================
                ========================재고 실사 등록========================
                """
        );
        //창고관리자가 관리하는 창고에 대해서만 재고실사를 등록하므로 입력받을 필요 없음
        System.out.println("재고 실사 등록 중....\n");
        CheckLog checkLog = stockService.addCheckLog(warehouseAdmin.getWIdx());
        if(checkLog == null){
            System.out.println("재고실사가 등록되지 않았습니다."); return;
        }

        System.out.println("[등록된 재고 실사]");
        String menu = String.format("%-8s%-8s%-10s%-10s%-10s", "실사로그번호", "창고번호", "창고명", "일치여부", "등록일");
        System.out.println(menu);
        System.out.println("------------------------------------------------------------");
        String list = String.format("%-8d%-8s%-10s%-8s%-10s",
                                    checkLog.getClIdx(), checkLog.getWUniqueNum(), checkLog.getWName(), checkLog.getClCorrect(), sdf.format(checkLog.getCreatedAt()));
        System.out.println(list);
        System.out.println();
    }

    private void showDeleteChecklogMenu(){
        //총관리자, 창고관리자
        //창고관리자인 경우, 자신이 관리하는 창고에 대한 재고실사만 지울 수 있음

        System.out.println(
                """
                ============================================================
                ========================재고 실사 삭제========================
                """
        );
        System.out.println("삭제할 재고 실사로그번호를 입력해주세요.");
        int clIdx = getChecklogIdx();
        if(clIdx == -1) return; //exit

        int result;
        if(currentUser.equals("TotalAdmin")) { //총관리자는 모든 실사로그 지울 수 있음
            result = stockService.removeCheckLog(clIdx, 0, false);
        }else{ //창고관리자는 자신의 창고에 대해서만..
            result = stockService.removeCheckLog(clIdx, warehouseAdmin.getWIdx(), true);
        }

        if(result == -1) { System.out.println("재고 실사 삭제 도중 오류가 발생했습니다."); return;}
        else if(result == 0){
            System.out.println("해당 실사로그가 존재하지 않습니다.");
            return;
        }

        System.out.println("성공적으로 삭제되었습니다.");
    }

    private int getChecklogIdx(){
        //삭제와 수정에서 동시에 쓰이는 메소드
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().matches("\\d+")){
                    System.out.println("실사로그번호는 숫자만 입력 가능합니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return -1;

                return Integer.parseInt(input);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void showChecklogSearchMenu(){
        //총관리자와 창고관리자만 가능함
        System.out.println(
                """
                 ============================================================
                 ==========================창고 조회===========================
                 재고 실사 조회 메뉴를 선택해주세요. 
                 1. 전체 재고 실사 조회
                 2. 색션별 재고 실사 조회
                 3. 창고별 재고 실사 조회
                 ------------------------------------------------------------
                 """
        );
        selectChecklogSearchMenu();
    }

    private void selectChecklogSearchMenu(){
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().matches("[1-3]")){
                    System.out.println("1-3까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return;

                switch(input.trim()){
                    case "1" -> showChecklogList(1);//전체 재고 실사 조회
                    case "2" -> showChecklogList(2);//섹션별 재고 실사 조회
                    case "3" -> showChecklogList(3);//창고별 재고 실사 조회
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showChecklogList(int num){
        //enum으로 뺄거임
        List<CheckLog> checkLogList = null;
        switch(num){
            case 1 -> {
                System.out.println( """
                ============================================================
                =======================전체 재고 실사 조회=====================
                """);

                switch(currentUser){
                    //총관리자의 경우
                    case "TotalAdmin" -> {checkLogList = stockService.getCheckLogList(0);}
                    case "WarehouseAdmin" -> {checkLogList = stockService.getCheckLogList(warehouseAdmin.getWIdx());}
                }

                if(checkLogList == null){
                    System.out.println(" 재고 실사 조회에 실패하였습니다."); return;
                }

                printCheckLogList(checkLogList);
            }

            case 2 -> {
                System.out.println( """
                ============================================================
                ======================섹션별 재고 실사 조회====================
                """);

                if(currentUser.equals("TotalAdmin")){
                    //총관리자인 경우 (창고번호 입력 + 섹션 입력)
                    System.out.println("조회할 창고번호를 입력해주세요.");
                    String wUniqueNum = getWarehouseUniqueNum();
                    if(wUniqueNum.equals("exit")) return;

                    System.out.println("창고의 조회할 섹션을 입력해주세요.");
                    String wsName = getWarehouseSectionName();
                    if(wsName.equals("exit")) return;

                    checkLogList = stockService.getSectionCheckLogList(wUniqueNum, wsName);
                    if(checkLogList == null){
                        System.out.println("섹션 별 재고 실사 조회에 실패하였습니다. ");
                        return;
                    }
                }else{

                    //창고관리자인 경우 (창고관리자가 관리하는 창고에 대해서만... + 섹션입력)
                    //먼저 창고관리자가 관리하는 창고가 보관형인지 확인
                    Warehouse warehouse = stockService.getWarehouseInfo(warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기
                    if(warehouse.getWtIdx() != 1){
                        System.out.println("현재 관리하는 창고는 보관형 창고가 아닙니다.");
                        return;
                    }

                    System.out.println("창고의 조회할 섹션을 입력해주세요.");
                    String wsName2 = getWarehouseSectionName();
                    if(wsName2.equals("exit")) return;
                    checkLogList = stockService.getSectionCheckLogList(warehouse.getWUniqueNum(), wsName2);
                    if(checkLogList == null){
                        System.out.println("섹션 별 재고 실사 조회에 실패하였습니다. "); return;
                    }
                }
                printCheckLogList(checkLogList);
            }

            case 3 -> {
                System.out.println( """
                ============================================================
                ======================창고별 재고 실사 조회====================
                """);

                switch(currentUser){
                    case "TotalAdmin" -> {//총관리자인 경우 (창고번호 입력받기)
                        System.out.println("조회할 창고번호를 입력해주세요.");
                        String wUniqueNum = getWarehouseUniqueNum();
                        if(wUniqueNum.equals("exit")) return;
                        checkLogList = stockService.getWarehouseCheckLogList(wUniqueNum);
                        if(checkLogList == null){
                            System.out.println(" 창고별 재고 실사 조회에 실패하였습니다."); return;
                        }
                    }
                    case "WarehouseAdmin" -> {
                        //창고관리자인 경우 (창고관리자가 관리하는 창고에 대해서 바로 출력) -> wUniqueNum 받아야함
                        Warehouse warehouse = stockService.getWarehouseInfo(warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기
                        checkLogList = stockService.getWarehouseCheckLogList(warehouse.getWUniqueNum());
                        if(checkLogList == null){
                            System.out.println(" 창고별 재고 실사 조회에 실패하였습니다."); return;
                        }
                    }
                }
                printCheckLogList(checkLogList);
            }
        }
    }

    private String getWarehouseUniqueNum(){
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().replaceAll("\\s+", "").startsWith("ware")) {
                    System.out.println("창고번호 양식에 맞지 않습니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return "exit";

                return input;
            }catch(IOException e){
                e.printStackTrace();;
            }
        }
    }

    private String getWarehouseSectionName(){
        //섹션은 섹션~~형태로 포맷이 고정되어있음
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().replaceAll("\\s+", "").startsWith("섹션")){
                    System.out.println("섹션명의 양식이 맞지 않습니다. 다시 입력해주세요.");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return "exit";

                return input.trim().replaceAll("\\s+", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateChecklogMenu(){
        //창고관리자만
        System.out.println( """
                ============================================================
                ========================재고 실사 수정=======================
                """);
        System.out.println("수정하고자 하는 실사로그번호를 입력해주세요.");
        int clIdx = getChecklogIdx();
        if(clIdx == -1) return;

        boolean result = stockService.checkUpdateCondition(clIdx, warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기
        if(!result){
            System.out.println("실사로그 수정 조건에 맞지 않습니다."); return;
        }

        boolean updateResult = stockService.updateCheckLog(clIdx);
        if(!updateResult){
            System.out.println("실사로그 수정에 실패했습니다."); return;
        }
        System.out.println("성공적으로 수정되었습니다.");
    }

    private void printStockList(List<Stock> stockList, int menu) {
        int page = 2, index = 0;

        while(index < stockList.size()){
            String type = null;
            String data = null;
            switch(menu){
                case 1 -> {
                    type = "대분류"; data = stockList.get(index).getPCategory();
                } //전체목록조회
                case 2 -> {
                    type = "중분류"; data = stockList.get(index).getSCategory();
                }//대분류별 재고 조회
                default -> {
                    type = "소분류"; data = stockList.get(index).getTCategory();
                }//이외의 조회
            }
            String currentPIdx = stockList.get(index).getPIdx();

            String menu1 = String.format("%-20s%-20s", "바코드번호", "물품이름");
            System.out.println(menu1);
            System.out.println("------------------------------------------------------------");
            System.out.println(String.format("%-20s%-20s", currentPIdx, stockList.get(index).getPName()));
            System.out.println("------------------------------------------------------------");
            String menu2 = String.format("%-8s%-6s%-6s%-6s%-7s%-10s", "창고번호", "가용재고", "불량재고", "안전재고",type,"수정일");
            System.out.println(menu2);
            System.out.println("------------------------------------------------------------");

            while(stockList.get(index).getPIdx().equals(currentPIdx)){
                String format = String.format("%-8s%-6d%-6d%-6d%-7s%-10s",
                        stockList.get(index).getWUniqueNum(), stockList.get(index).getSAvail(), stockList.get(index).getSNotAvail(), stockList.get(index).getSSafe(),
                        data, sdf.format(stockList.get(index).getUpdatedAt()));
                System.out.println(format);

                index++;
            }
            if(index == stockList.size()) break; //리스트 끝에 도달하면 페이지 넘기기 출력할 필요없음

            boolean result = showPageOfList(page);
            if(!result) return;
            page++;
        }
    }

    private void printCheckLogList(List<CheckLog> checkLogList) {
        String menu = String.format("%-8s%-8s%-10s%-10s%-10s", "실사로그번호", "창고번호", "섹션명" ,"일치여부", "등록일");

        System.out.println(menu);
        System.out.println("------------------------------------------------------------");

        int page = 2, index = 0;
        while(index < checkLogList.size()){
            if(index != 0 && index%20==0){
                boolean result = showPageOfList(page);
                if(!result) break;
                System.out.println("------------------------------------------------------------");
                page++;
            }
            CheckLog current = checkLogList.get(index);
            String format = String.format("%-8d%-8s%-10s%-10s%-10s",
                                            current.getClIdx(), current.getWUniqueNum(), current.getWsName(), current.getClCorrect(), sdf.format(current.getCreatedAt()));
            System.out.println(format);
            index++;
        }
        System.out.println();
    }

    private boolean showPageOfList(int page){
        System.out.printf("%d번째 페이지로 넘기시겠습니까? [Y|N]\n", page);
        try {
            String input = reader.readLine();

            if(input.trim().toUpperCase().equals("Y")) {
                return true;
            }else if(input.trim().toUpperCase().equals("N")) {
                return false;
            }else {
                System.out.println("잘못된 입력입니다.");
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
