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

    private void showWarehouseAddition(){
        //총관리자만 가능
        System.out.println(
                """
                        ============================================================
                        ===========================창고 등록==========================
                        """
        );
        Warehouse newWarehouse = getWarehouse();
        if(newWarehouse == null) return;
        boolean result = warehouseService.addWarehouse(newWarehouse);
        if(result) System.out.println("창고 등록에 성공하였습니다.");
        else System.out.println("창고 등록에 실패하였습니다.");
    }

    private Warehouse getWarehouse() {
        //  총관리자만 가능
        try {
            System.out.print("창고번호: ");
            String wIdx = reader.readLine();
            System.out.print("창고이름: ");
            String wName = reader.readLine();
            System.out.print("창고 상세 주소: ");
            String wAddr = reader.readLine();
            System.out.print("창고 종류: ");
            String wtName = reader.readLine();

            if(!wtName.trim().matches("보관형\\s*창고|마이크로\\s*풀필먼트")){
                System.out.println("창고타입은 보관형창고와 마이크로풀필먼트만 존재합니다. 창고 등록에 실패하였습니다.");
                return null;
            }

            System.out.print("최대 수용 용량: "); // 박스단위: 보관형은 800-1000, 마이크로풀필먼트 80-100
            String wMaxAmount = reader.readLine();

            if(!wMaxAmount.trim().matches("\\d+")){
                System.out.println("최대 수용 용량은 숫자만 입력 가능합니다. 창고 등록에 실패하였습니다.");
                return null;
            }

            //나머지 예외처리는 service에서 할 예정
            Warehouse temp = new Warehouse();
            temp.setWIdx(wIdx);
            temp.setWName(wName);
            temp.setWAddr(wAddr);
            temp.setWtName(wtName.trim().replaceAll("\\s", ""));

            if (wtName.equals("보관형창고")) {
                if (Integer.parseInt(wMaxAmount) < 800 || Integer.parseInt(wMaxAmount) > 1000) {
                    System.out.println("보관형 창고의 최대 수용 용량은 800~1000 박스여야 합니다. 창고 등록에 실패하였습니다.");
                    return null;
                }
            } else if (wtName.equals("마이크로풀필먼트")) {
                if (Integer.parseInt(wMaxAmount) < 80 || Integer.parseInt(wMaxAmount) > 100) {
                    System.out.println("마이크로풀필먼트의 최대 수용 용량은 80~100 박스여야 합니다. 창고 등록에 실패하였습니다.");
                    return null;
                }
            }

            temp.setWMaxAmount(Integer.parseInt(wMaxAmount));
            return temp;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


