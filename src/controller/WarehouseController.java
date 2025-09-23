package controller;

import domain.Warehouse;
import service.WarehouseService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class WarehouseController {
    private WarehouseService warehouseService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 싱클톤 패턴 적용
     */
    private static WarehouseController warehouseController;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private WarehouseController() {
    }

    public static WarehouseController getInstance() {
        if (warehouseController == null) {
            warehouseController = new WarehouseController();
        }
        return warehouseController;
    }

    public void showWarehouseMenu() {
        //총관리자인 경우
        System.out.println(
                """
                        ============================================================
                        ========================창고 전체 메뉴=========================
                        이용할 서비스를 선택해주세요.
                        1. 창고 등록
                        2. 창고 조회
                        ------------------------------------------------------------
                        """
        );
        selectWarehouseMenu();
        //총관리자가 아닌 경우 바로 showWarehouseSearchMenu부터 시작
    }

    private void selectWarehouseMenu() {
        //총관리자인 경우에만 실행가능한 메소드
        while (true) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                if (!input.trim().matches("[12]")) {
                    System.out.println("1-2까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                } else if (input.trim().toLowerCase().equals("exit")) return;

                switch (input) {
                    case "1" -> getWarehouse();
                    case "2" -> showWarehouseSearchMenu();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getWarehouse() {
        //  총관리자만 가능
    }


    private void showWarehouseSearchMenu() {
        //총관리자, 창고관리자인 경우
        System.out.println(
                """
                        ============================================================
                        ==========================창고 조회===========================
                        조회할 창고 유형을 선택해주세요.
                        [창고 유형]
                        1. 전체 현황 리스트 조회
                        2. 소재지별 조회
                        3. 고유 번호별 조회
                        4. 창고 종류별 조회
                        ------------------------------------------------------------
                        """
        );

        //배송기사, 일반회원, 비회원인 경우
        System.out.println(
                """
                        ============================================================
                        ==========================창고 조회===========================
                        조회할 창고 유형을 선택해주세요.
                        [창고 유형]
                        1. 전체 현황 리스트 조회
                        ------------------------------------------------------------
                        """
        );
        selectWarehouseSearchMenu();
    }

    private void selectWarehouseSearchMenu() {
        while (true) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                //총관리자 && 창고관리자인지 확인 후 실행
                if (!input.trim().matches("[1234]")) {
                    System.out.println("'1-4까지의 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                } else if (input.trim().toLowerCase().equals("exit")) return;

                //이후 switch로 case 구분


                //회원, 비회원, 배송기사 확인 후 실행
                if (!input.trim().matches("[1]")) {
                    System.out.println("1만 입력할 수 있습니다. 다시 입력해주세요.");
                } else if (input.trim().toLowerCase().equals("exit")) return;

                //이후 switch로 case 구분

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showAllWarehouseList() {
        //모두가 조회 가능
        System.out.println(
                """
                        ============================================================
                        ======================전체 현황 리스트 조회=====================
                        """
        );
        String menu = String.format("%-8s%-7s%-7s%-11s%-10s", "창고번호", "창고이름", "창고주소", "창고타입", "등록일");
        System.out.println(menu);
        System.out.println("------------------------------------------------------------");

        List<Warehouse> warehouseList = warehouseService.getWarehouseList();

        //목록이 많은 경우 한번에 20개까지만 보기
        int page = 2, index = 0;
        while(index < warehouseList.size()){
            if(index != 0 && index%20==0){
                boolean result = showPageOfList(page);
                if(!result) break;
                page++;
            }
            String format = String.format("%-8s%-7s%-7s%-11s%-10s",
                    warehouseList.get(index).getWIdx(), warehouseList.get(index).getWName(), warehouseList.get(index).getDoName(),
                    warehouseList.get(index).getWtName(),sdf.format(warehouseList.get(index).getCreatedAt()));
            System.out.println(format);
            index++;
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

    private String trimToLen(String s, int len) {
        if (s == null) return "";
        if (s.length() <= len) return s;
        return s.substring(0, len - 1) + "…";
    }
}


