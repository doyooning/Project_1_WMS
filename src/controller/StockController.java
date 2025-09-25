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
                   //메소드 구현하면서 추가
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

        int page = 2, index = 0;
        while(index < stockList.size()){
            String currentPIdx = stockList.get(index).getPIdx();

            String menu1 = String.format("%-20s%-20s", "바코드번호", "물품이름");
            System.out.println(menu1);
            System.out.println("------------------------------------------------------------");
            System.out.println(String.format("%-20s%-20s", currentPIdx, stockList.get(index).getPName()));
            System.out.println("------------------------------------------------------------");
            String menu2 = String.format("%-8s%-6s%-6s%-6s%-7s%-10s", "창고번호", "가용재고", "불량재고", "안전재고","대분류","등록일");
            System.out.println(menu2);
            System.out.println("------------------------------------------------------------");

            while(stockList.get(index).getPIdx().equals(currentPIdx)){
                String format = String.format("%-8s%-6d%-6d%-6d%-7s%-10s",
                        stockList.get(index).getWUniqueNum(), stockList.get(index).getSAvail(), stockList.get(index).getSNotAvail(), stockList.get(index).getSSafe(),
                        stockList.get(index).getPCategory(), sdf.format(stockList.get(index).getUpdatedAt()));
                System.out.println(format);

                index++;
            }
            boolean result = showPageOfList(page);
            if(!result) return;
        }
    }

    private boolean showPageOfList(int page){
        System.out.printf("%d번째 페이지로 넘기시겠습니까? [Y|N]", page);
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
