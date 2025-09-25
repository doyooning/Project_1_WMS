package controller;

import domain.Stock;
import service.StockService;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class StockController {
    private StockService stockService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    /**
     * 싱글톤 패턴 적용
     */
    private static StockController stockController;

    private StockController() {}
    public static StockController getInstance() {
        if (stockController == null) stockController = new StockController();

        return stockController;
    }

    public void showStockMenu(){
        //총관리자, 창고관리자만 볼 수 있음
        //회원과 비회원은 바로 재고 조회 메뉴로 넘어가게 구성
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
        selectStockMenu();
    }

    public void selectStockMenu() {
        //총관리자, 창고관리자만 실행가능한 메소드
        while(true){
            try{
                System.out.print("> ");
                String input = reader.readLine();

                if(!input.trim().matches("[12]")) {
                    System.out.println("1-2까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요. ");
                    continue;
                }else if(input.trim().toLowerCase().equals("exit")) return;

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
    }

    public void showStockSearchMenu(){
        //총관리자, 창고관리자, 회원, 배송기사
        System.out.println(
                """
                ============================================================
                ==========================재고 조회==========================
                재고 조회 메뉴를 선택해주세요.
                1. 전체 재고 조회
                2. 대분류별 재고 조회
                3. 중분류별 재고 조회
                4. 소분류별 재고 조회
                5. 품목 현황 조회
                ------------------------------------------------------------
                """
        );
        selectStockSearchMenu();
    }

    private void selectStockSearchMenu() {
        while(true){
           try{
               System.out.print("> ");
               String input = reader.readLine();

               if(!input.trim().matches("[1-5]")) {
                   System.out.println("1-5까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                   return;
               }else if(input.trim().toLowerCase().equals("exit")) return;

               switch(input.trim()){
                   case "1" -> showAllStockList();
                   case "2" -> showCategoryStockList(2);
                   case "3" -> showCategoryStockList(3);
                   case "4" -> showCategoryStockList(4);
                   //case 5
               }
               break;
           }catch(IOException e){
               e.printStackTrace();
           }
        }
    }

    private void showAllStockList() {
        //총관리자, 창고관리자, 회원, 배송기사 조회 가능
        //바코드별로 한 페이지 차지 하게끔 구현
        System.out.println(
                """
                ============================================================
                ========================전체 재고 조회=======================
                """
        );
        List<Stock> stockList = stockService.getAllStockList();

        printList(stockList, 1);
    }

    private void showCategoryStockList(int num){
        //총관리자, 창고관리자, 회원, 배송기사 조회 가능
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

        List<Stock> stockList = stockService.getCategoryStockList(num, category);
        printList(stockList, num);
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
                }
                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return input;
    }

    private void printList(List<Stock> stockList, int menu) {
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
            boolean result = showPageOfList(page);
            if(!result) return;
            page++;
        }
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
