package controller;

import common.Errors;
import common.Menu;
import domain.Warehouse;
import exception.ExceptionManager;
import service.WarehouseService;
import service.WarehouseServiceImpl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

//이 class에서는 User 저장이 의미없음. 어짜피 각 유저마다 시작하는 메소드가 정해져있음(Main에서 처리)
public class WarehouseControllerImpl implements WarehouseController {
    private WarehouseService warehouseService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 싱클톤 패턴 적용
     */
    private static WarehouseControllerImpl warehouseControllerImpl;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private WarehouseControllerImpl() {}

    public static WarehouseControllerImpl getInstance() {
        if (warehouseControllerImpl == null) {
            warehouseControllerImpl = new WarehouseControllerImpl();
        }
        return warehouseControllerImpl;
    }


    public void showWarehouseMenu() {
        //총관리자인 경우
        boolean exitFlag = false;
        while(!exitFlag){
            // 메뉴 출력 위치를 루프 시작 시가 아닌, 예외 처리 후 혹은 입력 직전에 출력
            System.out.println(Menu.WAREHOUSETOPMENU.getText());
            exitFlag = !selectWarehouseMenu();
        }
        //총관리자가 아닌 경우 바로 showWarehouseSearchMenu부터 시작
    }

    private boolean selectWarehouseMenu() {
        //총관리자인 경우에만 실행가능한 메소드

        while (true) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                if (input.trim().toLowerCase().equals("exit")) return false;
                if (!input.trim().matches("[12]")) {
                    System.out.println(Errors.INVALID_INPUT_12.getText());
                    continue;
                }

                switch (input) {
                    case "1" ->showWarehouseAddition();
                    case "2" -> showWarehouseSearchMenu();
                }
                break;
            } catch(ExceptionManager e){
                System.out.println(e.getMessage());
                return true;
            } catch (IOException e){
                System.out.println(e.getMessage());
                return true;
            }
        }
        return true;
    }

    private void showWarehouseAddition(){
        try {
            //총관리자만 가능
            if(warehouseService == null) warehouseService = WarehouseServiceImpl.getInstance();
            System.out.println(Menu.WAREHOUSEADDMENU.getText());
            Warehouse newWarehouse = getWarehouse();
            if(newWarehouse == null) return;
            boolean result = warehouseService.addWarehouse(newWarehouse);

            if(result) System.out.println("창고 등록에 성공하였습니다.");
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
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

            if(!wIdx.trim().startsWith("ware")){
                throw new ExceptionManager(Errors.WAREHOUSE_UNIQUENUM_ERROR.getText());
            }

            if (!wtName.trim().matches("보관형\\s*창고|마이크로\\s*풀필먼트")) {
                throw new ExceptionManager(Errors.WAREHOUSE_TYPE_ERROR.getText());
            }

            System.out.print("최대 수용 용량: "); // 박스단위: 보관형은 800-1000, 마이크로풀필먼트 80-100
            String wMaxAmount = reader.readLine();

            if (!wMaxAmount.trim().matches("\\d+")) {
                throw new ExceptionManager(Errors.WAREHOUSE_MAXAMOUNT_ERROR.getText());
            }

            //나머지 예외처리는 service에서 할 예정
            Warehouse temp = new Warehouse();
            temp.setWUniqueNum(wIdx);
            temp.setWName(wName);
            temp.setWAddr(wAddr);
            temp.setWtName(wtName.trim().replaceAll("\\s", ""));

            if (wtName.equals("보관형창고")) {
                if (Integer.parseInt(wMaxAmount) < 800 || Integer.parseInt(wMaxAmount) > 1000) {
                    throw new ExceptionManager(Errors.WAREHOUSE_STORAGE_MAXAMOUNT_ERROR.getText());
                }
            } else if (wtName.equals("마이크로풀필먼트")) {
                if (Integer.parseInt(wMaxAmount) < 100 || Integer.parseInt(wMaxAmount) > 300) {
                    throw new ExceptionManager(Errors.WAREHOUSE_MF_MAXAMOUNT_ERROR.getText());
                }
            }

            temp.setWMaxAmount(Integer.parseInt(wMaxAmount));
            return temp;

        }catch(ExceptionManager e){
            System.out.println(e.getMessage());
            return null;
        }catch (IOException e) {
            System.out.println(Errors.WAREHOUSE_ADD_IOEXCEPTION.getText());
            return null;
        }
    }


    public void showWarehouseSearchMenu(){
        //총관리자, 창고관리자인 경우
        System.out.println(Menu.WAREHOUSESEARCHMENU.getText());
        selectWarehouseSearchMenu();
    }

    public void selectWarehouseSearchMenu(){
        while (true) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                if (input.trim().toLowerCase().equals("exit")) return;
                //총관리자 && 창고관리자인지 확인 후 실행
                if (!input.trim().matches("[1234]")) {
                    System.out.println(Errors.INVALID_INPUT_14.getText());
                }

                //이후 switch로 case 구분
                switch (Integer.parseInt(input.trim())) {
                    case 1 -> showAllWarehouseList();
                    case 2 -> showWIdxWarehouseList();
                    case 3 -> showAddrWarehouseList();
                    case 4 -> showTypeWarehouseList();
                }
                break;
            }catch(ExceptionManager e){
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(Errors.WAREHOUSE_SEARCH_IOEXCEPTION.getText());
            }
        }
    }

    public void showAllWarehouseList(){
        if(warehouseService == null) warehouseService = WarehouseServiceImpl.getInstance();
        try {
            //모두가 조회 가능
            System.out.println(Menu.WAREHOUSEALLLISTMENU.getText());
            String menu = String.format("%-8s%-10s%-7s%-11s%-10s", "창고번호", "창고이름", "창고주소", "창고타입", "등록일");
            System.out.println(menu);
            System.out.println("------------------------------------------------------------");

            List<Warehouse> warehouseList = warehouseService.getWarehouseList();
            if(warehouseList == null) return;
            //목록이 많은 경우 한번에 20개까지만 보기
            printList(warehouseList, 1);
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private void showWIdxWarehouseList() throws ExceptionManager{
        try {
            //총관리자와 창고관리자만 조회 가능
            if(warehouseService == null) warehouseService = WarehouseServiceImpl.getInstance();
            System.out.println(Menu.WAREHOUSEWUNIQUELISTMENU.getText());
            String wUniqueNum = getWarehouseIdx();
            if(wUniqueNum == null || wUniqueNum.equals("exit")) return;

            Warehouse temp = warehouseService.getWarehouse(wUniqueNum);
            if(temp == null) return;

            System.out.println("------------------------------------------------------------");

            String format = String.format(
                    """
                    창고 번호| %s
                    창고 이름| %s
                    창고 임대료| %d
                    창고별 재고| %d
                    최대수용용량| %d
                    창고 타입| %s
                    도주소| %s
                    """, temp.getWUniqueNum(), temp.getWName(), temp.getWRent(), temp.getWStock(), temp.getWMaxAmount(), temp.getWtName(), temp.getDoName());
            System.out.println(format+"\n");
        } catch (ExceptionManager e) {
            System.out.println(e.getMessage());
        }
    }

    private String getWarehouseIdx(){
        System.out.println("조회할 창고 고유 번호를 입력해주세요. (창고고유번호는 ware로 시작합니다)");
        while(true){
            System.out.print("> ");
            try{
                String wUniqueNum = reader.readLine();

                if(wUniqueNum.trim().toLowerCase().equals("exit")) return "exit";
                else if(!wUniqueNum.trim().toLowerCase().startsWith("ware")) {
                    System.out.println(Errors.WAREHOUSE_UNIQUENUM_FORMAT_ERROR.getText());
                    continue;
                }
                return wUniqueNum.trim();

            }catch(IOException e){
                System.out.println(Errors.WAREHOUSEIDX_GET_IOEXCEPTION.getText());
                return null;
            }
        }
    }

    private void showAddrWarehouseList() throws ExceptionManager{
        //총관리자, 창고관리자
        if(warehouseService == null) warehouseService = WarehouseServiceImpl.getInstance();
        System.out.println(Menu.WAREHOUSEADDRLISTMENU.getText());

        int menuNum = getAddrNum();
        if(menuNum == -1) return;
        List<Warehouse> warehouseList = warehouseService.getAddressWarehouse(menuNum);

        if(warehouseList == null) return; //db예외

        String menu = String.format("%-8s%-10s%-7s%-7s%-17s", "창고번호", "창고이름", "창고임대료", "창고별재고", "상세주소");
        System.out.println(menu);
        System.out.println(Menu.MENULINE.getText());

        //너무 길면 페이지 단위로 출력하기
        printList(warehouseList, 3);
    }

    private int getAddrNum(){
        System.out.println(Menu.ADDRLISTMENU.getText());
        System.out.println("조회할 소재지를 선택해주세요.");

        while(true){
            System.out.print("> ");
            try{
                String input = reader.readLine();
                if(input.trim().toLowerCase().equals("exit")) return -1;

                if(!input.trim().matches("[1-9]")) System.out.println(Errors.INVALID_INPUT_19.getText());
                else return Integer.parseInt(input.trim());
            }catch(IOException e){
                System.out.println(Errors.WAREHOUSEADDR_GET_IOEXCEPTION.getText());
            }
        }
    }

    private void showTypeWarehouseList() {
        //총관리자, 창고관리자
        if(warehouseService == null) warehouseService = WarehouseServiceImpl.getInstance();
        System.out.println(Menu.WAREHOUSETYPELISTMENU.getText());
        int result = getWarehouseType();
        if(result == -1) return; //exit
        List<Warehouse> warehouseList = warehouseService.getTypeWarehouse(result);

        if(warehouseList == null) return; //db예외

        String menu = String.format("%-8s%-10s%-8s%-8s%-9s", "창고번호", "창고이름", "창고임대료", "창고별재고", "도주소");
        System.out.println(menu);
        System.out.println(Menu.MENULINE.getText());

        //페이지 단위로 출력하기
        printList(warehouseList, 4);
    }

    private int getWarehouseType(){
        System.out.println(Menu.TYPELISTMENU.getText());
        System.out.println("조회할 창고 종류를 선택해주세요.");

        while(true){
            System.out.print("> ");
            try{
                String input = reader.readLine();

                if(input.trim().toLowerCase().equals("exit")) return -1;

                if(!input.trim().matches("[1-2]")) System.out.println(Errors.INVALID_INPUT_12.getText());
                else return Integer.parseInt(input.trim());
            }catch(IOException e){
                System.out.println(Errors.WAREHOUSETYPE_GET_IOEXCEPTION.getText());
            }
        }
    }

    private void printList(List<Warehouse> warehouseList, int num){//num=1이면 전체창고조회, num=3이면 소재지별 창고조회, num=4이면 창고종류별 창고조회
        int page = 2, index = 0;
        while(index < warehouseList.size()){
            if(index != 0 && index%20==0){
                boolean result = showPageOfList(page);
                if(!result) break;
                System.out.println(Menu.MENULINE.getText());
                page++;
            }

            String format = null;
            switch(num){
                case 1 -> format = String.format("%-8s%-14s%-7s%-11s%-10s",
                        warehouseList.get(index).getWUniqueNum(), warehouseList.get(index).getWName(), warehouseList.get(index).getDoName(),
                        warehouseList.get(index).getWtName(),sdf.format(warehouseList.get(index).getCreatedAt())); //전체 창고현황 출력
                case 3 -> format = String.format("%-8s%-14s%-7d%-7d%-17s",
                        warehouseList.get(index).getWUniqueNum(), warehouseList.get(index).getWName(), warehouseList.get(index).getWRent(),
                        warehouseList.get(index).getWStock(),trimToLen(warehouseList.get(index).getWAddr(), 10));
                case 4 -> format = String.format("%-8s%-14s%-8d%-8d%-9s",
                        warehouseList.get(index).getWUniqueNum(), warehouseList.get(index).getWName(), warehouseList.get(index).getWRent(),
                        warehouseList.get(index).getWStock(), warehouseList.get(index).getDoName()
                );
            }
            System.out.println(format);
            index++;
        }
        System.out.println();
    }

    private boolean showPageOfList(int page){
        System.out.printf("%d번째 페이지로 넘기시겠습니까\n? [Y|N]", page);
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
            System.out.println(Errors.INVALID_INPUT_PAGE.getText());
            return false;
        }
    }

    private String trimToLen(String s, int len) {
        if (s == null) return "";
        if (s.length() <= len) return s;
        return s.substring(0, len - 1) + "…";
    }
}


