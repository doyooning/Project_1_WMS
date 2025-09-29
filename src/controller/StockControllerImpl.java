package controller;

import common.Errors;
import common.Menu;
import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;
import domain.WarehouseAdmin;
import exception.ExceptionManager;
import service.StockServiceImpl;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class StockControllerImpl implements StockController {
    private StockServiceImpl stockService = StockServiceImpl.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private String currentUser;
    private WarehouseAdmin warehouseAdmin;
    /**
     * 싱글톤 패턴 적용
     */
    private static StockControllerImpl stockControllerImpl;

    private StockControllerImpl() {}
    public static StockControllerImpl getInstance() {
        if (stockControllerImpl == null) stockControllerImpl = new StockControllerImpl();

        return stockControllerImpl;
    }

    public void setCurrentUser(String currentUser, WarehouseAdmin warehouseAdmin) {
        this.currentUser = currentUser;
        this.warehouseAdmin = warehouseAdmin; //만약에 창고관리자나 일반회원이 접속하면 그냥 null이 들어감
    }

    public void showStockMenu(){
        //총관리자, 창고관리자만 볼 수 있음
        //회원은 바로 재고 조회 메뉴로 넘어가게 구성
        while(true){
            System.out.println(Menu.STOCKTOPMENU.getText());
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

                if(input.trim().toLowerCase().equals("exit")) return false;

                if(!input.trim().matches("[12]")) {
                    System.out.println(Errors.INVALID_INPUT_12.getText());
                    continue;
                }

                stockService = StockServiceImpl.getInstance();
                switch(input.trim()){
                    case "1" -> showStockSearchMenu();
                    case "2" -> showStockChecklogMenu();
                }

                break;
            } catch(ExceptionManager e){
                System.out.println(e.getMessage());
            }catch(IOException e){
                System.out.println(Errors.STOCK_TOPMENU_SELECT_IOEXCEPION.getText());
            }
        }
        return true;
    }

    public void showStockSearchMenu(){
        //총관리자, 창고관리자, 회원
        String menu = Menu.STOCKSEARCHMENU.getText();

        if(currentUser.equals("user")){//회원은 이 화면부터 시작하므로..
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

    private boolean selectStockSearchMenu(){
        while(true){
           try{
               System.out.print("> ");
               String input = reader.readLine();

               if(input.trim().toLowerCase().equals("exit")) return false;

               if(!input.trim().matches("[1-5]")) {
                   System.out.println(Errors.INVALID_INPUT_15.getText());
               }

               switch(input.trim()){
                   case "1" -> showAllStockList();
                   case "2" -> showCategoryStockList(2);
                   case "3" -> showCategoryStockList(3);
                   case "4" -> showCategoryStockList(4);
                   case "5" -> showProductStockList();
               }
               break;
           }catch(ExceptionManager e){
               System.err.println(e.getMessage());
           }catch(IOException e){
               throw new ExceptionManager(Errors.STOCK_SEARCHMENU_SELECT_IOEXCEPTION.getText());
           }
        }
        return true;
    }

    private void showAllStockList(){
        try {
            //총관리자, 창고관리자, 회원 조회 가능
            //바코드별로 한 페이지 차지 하게끔 구현
            System.out.println(Menu.STOCKALLSEARCHMENU.getText());
            stockService  = StockServiceImpl.getInstance();
            List<Stock> stockList = stockService.getAllStockList();
            if(stockList == null) return;

            printStockList(stockList, 1);
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private void showCategoryStockList(int num){
        try {
            //총관리자, 창고관리자, 회원 조회 가능
            String menu = null;
            switch(num){
                case 2 -> menu = "대분류";
                case 3 -> menu = "중분류";
                case 4 -> menu = "소분류";
            }

            System.out.printf(Menu.STOCKCATEGORYSEARCHMENU.getText(), menu);
            System.out.printf("조회하실 %s를 입력해주세요.\n", menu);
            String category = getCategory();
            if(category.equals("exit")) return;

            stockService  = StockServiceImpl.getInstance();
            List<Stock> stockList = stockService.getCategoryStockList(num, category);
            if(stockList == null) return;

            printStockList(stockList, num);
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private String getCategory(){
        String input;
        while(true){
            try{
                System.out.print("> ");
                input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return "exit";

                if(!input.trim().matches("^[가-힣\\s]+$")){
                    System.out.println(Errors.INVALID_INPUT_CATEGORY.getText());
                    continue;
                }
                break;
            }catch(IOException e){
                throw new ExceptionManager(Errors.STOCK_CATEGORY_GET_IOEXCEPTION.getText());
            }
        }
        return input;
    }

    private void showProductStockList() {
        try {
            //총관리자, 창고관리자, 회원 조회 가능
            System.out.println(Menu.STOCKPRODUCTSEARCHMENU.getText());
            stockService  = StockServiceImpl.getInstance();
            String pIdx = getPIdx();
            if(pIdx.equals("exit")) return;
            List<Stock> stockList = stockService.getProductStockList(pIdx);
            if(stockList == null) return;

            printStockList(stockList, 5);
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private String getPIdx(){
        System.out.println("조회할 바코드 번호를 입력해주세요.");
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return "exit";

                if(!input.trim().matches("^[1-9][0-9]{12}$")){
                    System.out.println(Errors.INVALID_INPUT_PIDX.getText());
                    continue;
                }

                return input.trim();
            }catch(IOException e){
                throw new ExceptionManager(Errors.STOCK_PIDX_GET_IOEXCEPTION.getText());
            }
        }
    }


    /**
     * 재고 실사
     */

    private void showStockChecklogMenu(){
        //재고실사는 총관리자와 창고관리자만 메뉴에 접근할 수 있지만, 보여지는 메뉴가 다름
        String menu = null;
        if(currentUser.equals("total_admin")) {
            //총관리자
            menu = Menu.CHECKLOG_TOTALADMIN_MENU.getText() ;
        }
        else if(currentUser.equals("warehouse_admin")) {
            menu = Menu.CHECKLOG_WAREHOUSEADMIN_MENU.getText() ;
        }
        System.out.println(menu);
        selecStockChecklogMenu();
    }

    private void selecStockChecklogMenu() throws ExceptionManager{
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                switch(currentUser){
                    case "total_admin" -> {
                        //총관리자일 때는 메뉴가 2개까지 밖에 없으니

                        if(input.trim().toLowerCase().equals("exit")) return;
                        if(!input.trim().matches("[12]")) {
                            System.out.println(Errors.INVALID_INPUT_12.getText());
                            continue;
                        }

                        stockService = StockServiceImpl.getInstance();
                        switch(input.trim()){
                            case "1" -> showDeleteChecklogMenu();//재고실사 삭제메뉴
                            case "2" -> showChecklogSearchMenu(); //재고 실사 조회 메뉴
                        }
                    }

                    case "warehouse_admin"-> {
                        //창고관리자 일때는 메뉴가 4개임
                        if(input.trim().toLowerCase().equals("exit")) return;
                        if(!input.trim().matches("[1-4]")){
                            System.out.println(Errors.INVALID_INPUT_14.getText());
                            continue;
                        }

                        stockService = StockServiceImpl.getInstance();
                        switch(input.trim()){
                            case "1" -> showAddChecklogMenu(); //재고실사등록
                            case "2" -> showDeleteChecklogMenu();//재고실사삭제
                            case "3" -> showChecklogSearchMenu();//재고실사조회
                            case "4" -> showUpdateChecklogMenu();//재고실사수정
                        }
                    }
                }
                break;
            }catch(ExceptionManager e){
                System.out.println(e.getMessage());
            }catch(IOException e) {
                System.out.println(Errors.CHECKLOG_TOPMENU_SELECT_IOEXCEPTION.getText());
            }
        }
    }

    private void showAddChecklogMenu() {
        try {
            //창고관리자만
            if(warehouseAdmin == null) throw new ExceptionManager(Errors.WA_HAVE_NO_WIDX.getText());

            System.out.println(Menu.CHECKLOG_ADD_MENU.getText());
            //창고관리자가 관리하는 창고에 대해서만 재고실사를 등록하므로 입력받을 필요 없음
            System.out.println("재고 실사 등록 중....\n");
            stockService = StockServiceImpl.getInstance();
            CheckLog checkLog = stockService.addCheckLog(warehouseAdmin.getWIdx());
            if(checkLog == null) return;


            System.out.println("[등록된 재고 실사]");
            String menu = String.format("%-8s%-8s%-10s%-10s%-10s", "실사로그번호", "창고번호", "창고명", "일치여부", "등록일");
            System.out.println(menu);
            System.out.println("------------------------------------------------------------");
            String list = String.format("%-11d%-10s%-16s%-10s%-10s",
                                        checkLog.getClIdx(), checkLog.getWUniqueNum(), checkLog.getWName(), checkLog.getClCorrect(), sdf.format(checkLog.getCreatedAt()));
            System.out.println(list);
            System.out.println();
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private void showDeleteChecklogMenu(){
        try {
            //총관리자, 창고관리자
            //창고관리자인 경우, 자신이 관리하는 창고에 대한 재고실사만 지울 수 있음

            System.out.println(Menu.CHECKLOG_DELETE_MENU.getText());
            System.out.println("삭제할 재고 실사로그번호를 입력해주세요.");
            int clIdx = getChecklogIdx();
            if(clIdx == -1) return; //exit

            int result;
            stockService = StockServiceImpl.getInstance();
            if(currentUser.equals("total_admin")) { //총관리자는 모든 실사로그 지울 수 있음
                result = stockService.removeCheckLog(clIdx, 0, false);
            }else{
                //창고관리자는 자신의 창고에 대해서만..
                if(warehouseAdmin == null) throw new ExceptionManager(Errors.WA_HAVE_NO_WIDX.getText());
                result = stockService.removeCheckLog(clIdx, warehouseAdmin.getWIdx(), true);
            }

            if(result == -1) return;

            System.out.println("성공적으로 삭제되었습니다.");
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private int getChecklogIdx(){
        //삭제와 수정에서 동시에 쓰이는 메소드
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return -1;

                if(!input.trim().matches("\\d+")){
                    System.out.println(Errors.INVALID_INPUT_CHECKLOG.getText());
                    continue;
                }
                return Integer.parseInt(input);
            }catch(IOException e){
                throw new ExceptionManager(Errors.CHECKLOG_GET_IOEXCEPTION.getText());
            }
        }
    }

    private void showChecklogSearchMenu(){
        //총관리자와 창고관리자만 가능함
        System.out.println(Menu.CHECKLOG_SEARCH_MENU.getText());
        selectChecklogSearchMenu();
    }

    private void selectChecklogSearchMenu() {
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return;

                if(!input.trim().matches("[1-3]")){
                    System.out.println(Errors.INVALID_INPUT_13.getText());
                    continue;
                }

                switch(input.trim()){
                    case "1" -> showChecklogList(1);//전체 재고 실사 조회
                    case "2" -> showChecklogList(2);//섹션별 재고 실사 조회
                    case "3" -> showChecklogList(3);//창고별 재고 실사 조회
                }
                break;
            } catch(ExceptionManager e){
                System.out.println(e.getMessage());
            }catch (IOException e) {
                System.out.println(Errors.CHECKLOG_SEARCHMENU_SELECT_IOEXCEPTION.getText());
            }
        }
    }

    private void showChecklogList(int num) {
        //enum으로 뺄거임
        stockService = StockServiceImpl.getInstance();
        List<CheckLog> checkLogList = null;
        switch(num){
            case 1 -> {
                try {
                    System.out.println(Menu.CHECKLOG_ALL_SEARCH_MENU.getText());

                    switch(currentUser){
                        //총관리자의 경우
                        case "total_admin" -> {checkLogList = stockService.getCheckLogList(0);}
                        case "warehouse_admin" -> {
                            if(warehouseAdmin == null) throw new ExceptionManager(Errors.WA_HAVE_NO_WIDX.getText());
                            checkLogList = stockService.getCheckLogList(warehouseAdmin.getWIdx());}
                    }

                    if(checkLogList == null) return;

                    printCheckLogList(checkLogList);
                } catch (ExceptionManager e) {
                    System.out.println(e.getMessage());
                }
            }

            case 2 -> {
                try {
                    System.out.println(Menu.CHECKLOG_SECTION_SEARCH_MENU.getText());

                    if(currentUser.equals("total_admin")){
                        //총관리자인 경우 (창고번호 입력 + 섹션 입력)
                        System.out.println("조회할 창고번호를 입력해주세요.");
                        String wUniqueNum = getWarehouseUniqueNum();
                        if(wUniqueNum.equals("exit")) return;

                        System.out.println("창고의 조회할 섹션을 입력해주세요.");
                        String wsName = getWarehouseSectionName();
                        if(wsName.equals("exit")) return;

                        checkLogList = stockService.getSectionCheckLogList(wUniqueNum, wsName);
                        if(checkLogList == null) return;

                    }else{

                        //창고관리자인 경우 (창고관리자가 관리하는 창고에 대해서만... + 섹션입력)
                        //먼저 창고관리자가 관리하는 창고가 보관형인지 확인
                        if(warehouseAdmin == null) throw new ExceptionManager(Errors.WA_HAVE_NO_WIDX.getText());
                        Warehouse warehouse = stockService.getWarehouseInfo(warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기
                        if(warehouse.getWtIdx() != 1){
                            throw new ExceptionManager(Errors.NO_STORAGE_WAREHOUSETYPE.getText());
                        }

                        System.out.println("창고의 조회할 섹션을 입력해주세요.");
                        String wsName2 = getWarehouseSectionName();
                        if(wsName2.equals("exit")) return;
                        checkLogList = stockService.getSectionCheckLogList(warehouse.getWUniqueNum(), wsName2);

                        if(checkLogList == null) return;
                    }
                    printCheckLogList(checkLogList);
                } catch (ExceptionManager e) {
                    System.out.println(e.getMessage());
                }
            }

            case 3 -> {
                try {
                    System.out.println(Menu.CHECKLOG_WAREHOUSE_SEARCH_MENU.getText());
                    switch(currentUser){
                        case "total_admin" -> {//총관리자인 경우 (창고번호 입력받기)
                            System.out.println("조회할 창고번호를 입력해주세요.");
                            String wUniqueNum = getWarehouseUniqueNum();
                            if(wUniqueNum.equals("exit")) return;
                            checkLogList = stockService.getWarehouseCheckLogList(wUniqueNum);
                            if(checkLogList == null) return;

                        }
                        case "warehouse_admin" -> {
                            //창고관리자인 경우 (창고관리자가 관리하는 창고에 대해서 바로 출력) -> wUniqueNum 받아야함
                            if(warehouseAdmin == null) throw new ExceptionManager(Errors.WA_HAVE_NO_WIDX.getText());
                            Warehouse warehouse = stockService.getWarehouseInfo(warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기
                            checkLogList = stockService.getWarehouseCheckLogList(warehouse.getWUniqueNum());
                            if(checkLogList == null) return;
                        }
                    }
                    printCheckLogList(checkLogList);
                } catch (ExceptionManager e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private String getWarehouseUniqueNum(){
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return "exit";

                if(!input.trim().replaceAll("\\s+", "").startsWith("ware")) {
                    System.out.println(Errors.WAREHOUSE_UNIQUENUM_FORMAT_ERROR.getText());
                    continue;
                }

                return input;
            }catch(IOException e){
                throw new ExceptionManager(Errors.WAREHOUSEIDX_GET_IOEXCEPTION.getText());
            }
        }
    }

    private String getWarehouseSectionName(){
        //섹션은 섹션~~형태로 포맷이 고정되어있음
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return "exit";

                if(!input.trim().replaceAll("\\s+", "").startsWith("섹션")){
                    System.out.println();
                    continue;
                }

                return input.trim().replaceAll("\\s+", "");
            } catch (IOException e) {
                throw new ExceptionManager(Errors.INVALID_INPUT_SECTIONNAME.getText());
            }
        }
    }

    private void showUpdateChecklogMenu(){
        try {
            //창고관리자만
            stockService = StockServiceImpl.getInstance();
            System.out.println(Menu. CHECKLOG_UPDATE_MENU.getText());
            System.out.println("수정하고자 하는 실사로그번호를 입력해주세요.");
            int clIdx = getChecklogIdx();
            if(clIdx == -1) return;

            stockService.checkUpdateCondition(clIdx, warehouseAdmin.getWIdx()); //창고관리자 wIdx 넣기

            boolean updateResult = stockService.updateCheckLog(clIdx);
            if(!updateResult) return;

            System.out.println("성공적으로 수정되었습니다.");
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
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

            while(index < stockList.size() && stockList.get(index).getPIdx().equals(currentPIdx)){
                String format = String.format("%-11s%-9d%-9d%-9d%-10s%-10s",
                        stockList.get(index).getWUniqueNum(), stockList.get(index).getSAvail(), stockList.get(index).getSNotAvail(), stockList.get(index).getSSafe(),
                        data, sdf.format(stockList.get(index).getUpdatedAt()));
                System.out.println(format);

                index++;
            }
            System.out.println();
            if(index == stockList.size()) return; //리스트 끝에 도달하면 페이지 넘기기 출력할 필요없음

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
            String format = String.format("%-11d%-9s%-12s%-12s%-10s",
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
            throw new ExceptionManager(Errors.INVALID_INPUT_PAGE.getText());
        }
    }

}
