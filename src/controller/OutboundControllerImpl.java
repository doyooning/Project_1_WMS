package controller;

import domain.TotalAdmin;
import domain.User;
import service.OutboundService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * 메뉴 첫 접근
 * : BoardController/showBoardMenu(), selectBoardMenu()
 * [출고 관리]
 * */
public class OutboundControllerImpl implements InOutboundController{
    private User user;
    private TotalAdmin totalAdmin;
    private int authority = 0;

    // statics
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");

    // 싱글턴 패턴을 위한 인스턴스 생성
    private static OutboundControllerImpl outboundControllerImpl;
    private OutboundControllerImpl() {}

    // getInstance는 1회만
    public static OutboundControllerImpl getInstance() {
        if (outboundControllerImpl == null) {
            outboundControllerImpl = new OutboundControllerImpl();
        }
        return outboundControllerImpl;
    }

    private OutboundService outboundService = OutboundService.getInstance();

    public void setLoggedInUser(Object user) {
        if (user instanceof TotalAdmin) {
            this.totalAdmin = (TotalAdmin) user;
            this.authority = 1;
        } else if (user instanceof User) {
            this.user = (User) user;
            this.authority = 3;
        }
    }

    public void logoutUser() {
        this.user = null;
        this.totalAdmin = null;
        this.authority = 0;
    }

    @Override
    public void showMenu() {
        int status = 0;
        // 권한 구분 임의 구현..
        if (authority == 1) {
            System.out.print(
                    Messages.ADMIN_MAIN_MENU_OUT.getText()
            );

        } else if (authority == 3) {
            System.out.print(
                    Messages.USER_MAIN_MENU_OUT.getText()
            );
        }
        try {
            // 메뉴 번호 입력받음
            int menuNum = Integer.parseInt(br.readLine());
            status = selectMenu(menuNum);
            if (status == 1) {
                showMenu();
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println(Errors.INVALID_INPUT_ERROR.getText());
            showMenu();

        } catch (Exception e) {
            System.out.println(Errors.UNEXPECTED_ERROR.getText());
            e.printStackTrace();
            showMenu();
        }
    }

    @Override
    public int selectMenu(int menuNum) {
        switch (menuNum) {
            case 1 -> {
                int status = 0;
                if(authority == 1) {
                    // 1. 출고 요청 승인
                    // 미승인된 출고요청 목록 출력

                    status = approveRequest();
                    if (status == -1) {
                        System.out.println(Errors.DATA_INPUT_ERROR.getText());

                    } else {
                        System.out.printf(
                                Messages.REQUEST_APPROVED_OUT.getText(), status
                        );
                    }

                } else if(authority == 3) {
                    // 1. 출고 요청
                    status = InputRequestData(user.getUIdx());
                    if (status == -1) {
                        System.out.println(Errors.DATA_INPUT_ERROR.getText());

                    } else {
                        System.out.printf(
                                Messages.REQUEST_REGISTERED_OUT.getText(), status
                        );
                    }

                }
            }
            case 2 -> {
                // 2. 출고 요청 수정
                showUpdateMenu();

            }
            case 3 -> {
                int status = 0;
                // 3. 출고 요청 취소
                status = cancelRequest();
                if (status == -1) {
                    System.out.println(Errors.DATA_INPUT_ERROR.getText());
                } else {
                    System.out.print(
                            Messages.REQUEST_CANCELED_OUT.getText()
                    );
                }

            }
            case 4 -> {
                int status = 0;
                // 4. 출고고지서 출력
                status = showRequestInfo();
                if (status == -1) {
                    System.out.println(Errors.DATA_INPUT_ERROR.getText());
                } else {
                    System.out.print(
                            Messages.DISPLAY_ADJUST.getText()
                    );
                }
            }

            case 5 -> {
                // 5. 출고 현황 조회
                if(authority == 1) {
                    showAdminInfoMenu();
                } else if(authority == 3) {
                    showInfoMenu(user.getUIdx());
                }
            }

            case 6 -> {
                // 6. 나가기
                return 0;
            }

            default -> {
                System.out.print(
                        Errors.INVALID_INPUT_ERROR.getText()
                );
            }
        }

        return 1;
    }

    @Override
    public void showUpdateMenu() {
        int status = 0;
        System.out.print(
                Messages.UPDATE_MENU_OUT.getText()
        );
        try {
            int menuNum = Integer.parseInt(br.readLine());
            status = selectUpdateMenu(menuNum);
            if (status == 1) {
                showUpdateMenu();
            } else if (status == -1) {
                System.out.println(Errors.DATA_INPUT_ERROR.getText());
                showUpdateMenu();
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            showUpdateMenu();

        } catch (Exception e) {
            System.out.print(
                    Errors.UNEXPECTED_ERROR.getText()
            );
            showUpdateMenu();
        }

    }

    @Override
    public int selectUpdateMenu(int menuNum) {
        int status = 0;
        try {
            switch (menuNum) {
                // 요청 정보 수정
                case 1 -> {
                    // 요청 번호 입력
                    System.out.print(
                            Messages.ENTER_REQUEST_ID_UPDATE_OUT.getText()
                    );
                    int requestId = Integer.parseInt(br.readLine());
                    boolean accessStatus = isAccessibleRequest(requestId);
                    if (accessStatus == false) {
                        System.out.print(
                                Errors.INACCESSIBLE_REQUEST_ERROR.getText()
                        );
                        return -1;
                    }

                    status = InputRequestDataUpdate(requestId);
                    if (status == -1) {
                        return -1;
                    }
                }
                // 물품 정보 수정
                case 2 -> {
                    // 요청 번호 입력
                    System.out.print(
                            Messages.ENTER_REQUEST_ID_UPDATE_OUT.getText()
                    );
                    int requestId = Integer.parseInt(br.readLine());
                    boolean accessStatus = isAccessibleRequest(requestId);
                    if (accessStatus == false) {
                        System.out.print(
                                Errors.INACCESSIBLE_REQUEST_ERROR.getText()
                        );
                        return -1;
                    }

                    status = InputRequestItemUpdate(requestId);
                    if (status == -1) {
                        return -1;
                    }

                }
                case 3 -> {
                    // 뒤로가기
                    return 0;
                }
            }
        } catch (IOException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
        }
        return 1;
    }

    @Override
    public void showInfoMenu(int uId) {
        int status = 0;
        System.out.print(
                Messages.USER_INFO_MENU_OUT.getText()
        );
        try {
            int menuNum = Integer.parseInt(br.readLine());
            status = selectInfoMenu(menuNum, uId);
            if (status == 1) {
                System.out.print(
                        Messages.DISPLAY_ADJUST.getText()
                );
                showInfoMenu(uId);
            } else if (status == -1) {
                System.out.println(Errors.DATA_INPUT_ERROR.getText());
                showInfoMenu(uId);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            showInfoMenu(uId);

        } catch (Exception e) {
            System.out.print(
                    Errors.UNEXPECTED_ERROR.getText()
            );
            showInfoMenu(uId);
        }
    }

    public void showAdminInfoMenu() {
        int status = 0;
        System.out.print(
                Messages.ADMIN_INFO_MENU_OUT.getText()
        );
        try {
            int menuNum = Integer.parseInt(br.readLine());
            status = selectAdminInfoMenu(menuNum);
            if (status == 1) {
                System.out.print(
                        Messages.DISPLAY_ADJUST.getText()
                );
                showAdminInfoMenu();
            } else if (status == -1) {
                System.out.println(Errors.DATA_INPUT_ERROR.getText());
                showAdminInfoMenu();
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            showAdminInfoMenu();

        } catch (Exception e) {
            System.out.print(
                    Errors.UNEXPECTED_ERROR.getText()
            );
            showAdminInfoMenu();
        }
    }

    @Override
    public int selectInfoMenu(int menuNum, int uId) {
        int status = 1;
        try {
            switch (menuNum) {
                // 입고 요청 조회
                case 1 -> {
                    List<List<String>> requestList = outboundService.getBoundInfo(uId);
                    if (requestList == null) {
                        return -1;
                    }
                    printRequestList(requestList);
                    System.out.print(
                            Messages.PRESS_ANY_KEY.getText()
                    );
                    String input = br.readLine();

                }
                // 요청 상품 리스트
                case 2 -> {
                    List<List<String>> requestItemList = outboundService.getBoundItemInfo(uId);
                    if (requestItemList == null) {
                        return -1;
                    }
                    printRequestItemList(requestItemList);
                    System.out.print(
                            Messages.PRESS_ANY_KEY.getText()
                    );
                    String input = br.readLine();

                }
                case 3 -> {
                    // 뒤로가기
                    return 0;
                }
            }
        } catch (Exception e) {
            System.out.print(
                    Errors.UNEXPECTED_ERROR.getText()
            );
        }
        return status;
    }

    public int selectAdminInfoMenu(int menuNum) {
        int status = 1;
        try {
            switch (menuNum) {
                // 미승인 요청 조회
                case 1 -> {
                    List<List<String>> pRequestList = outboundService.getPendingRequestList();
                    if (pRequestList == null) {
                        return -1;
                    }
                    printPendingRequest(pRequestList);
                    System.out.print(
                            Messages.PRESS_ANY_KEY.getText()
                    );
                    String input = br.readLine();
                }
                // 기간별 출고 현황
                case 2 -> {
                    System.out.print(
                            Messages.ENTER_START_DATE_OUT.getText()
                    );
                    String startDate = br.readLine();
                    if (startDate.isBlank() || startDate.length() != 8) {
                        throw new IOException();
                    }
                    Date date = informat.parse(startDate);
                    String newStartDate = outformat.format(date);

                    System.out.print(
                            Messages.ENTER_END_DATE.getText()
                    );
                    String endDate = br.readLine();
                    if (endDate.isBlank() || endDate.length() != 8) {
                        throw new IOException();
                    }
                    date = informat.parse(endDate);
                    String newEndDate = outformat.format(date);

                    List<List<String>> requestList = outboundService.getRequestListByPeriod(newStartDate, newEndDate);
                    if (requestList == null) {
                        return -1;
                    }
                    printRequestByPeriod(requestList);
                    System.out.print(
                            Messages.PRESS_ANY_KEY.getText()
                    );
                    String input = br.readLine();

                }
                case 3 -> {
                    // 뒤로가기
                    return 0;
                }
            }
        } catch (IOException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
        } catch (Exception e) {
            System.out.print(
                    Errors.UNEXPECTED_ERROR.getText()
            );
        }
        return status;
    }


    public int InputRequestData(int uId) {
        int rtn = 0;
        try {
            System.out.print(
                    Messages.ENTER_REQUEST_ID_OUT.getText()
            );
            int wId = Integer.parseInt(br.readLine());

            System.out.print(
                    Messages.ENTER_DUE_DATE_OUT.getText()
            );
            String dueDate = br.readLine();
            if (dueDate.isBlank() || dueDate.length() != 8) {
                throw new IOException();
            }
            Date date = informat.parse(dueDate);
            String newDueDate = outformat.format(date);
            // 요청 정보 전송
            int requestStatus = outboundService.addRequest(uId, wId, newDueDate);

            // 실행 결과 오류 검증
            if (requestStatus == -1) {
                return -1;
            }

            while (true) {
                System.out.print(
                        Messages.ENTER_ITEM_ID.getText()
                );
                String productId = br.readLine();

                System.out.print(
                        Messages.ENTER_ITEM_QUANTITY.getText()
                );
                int productQuantity = Integer.parseInt(br.readLine());
                // 물품 정보 전송
                int itemStatus = outboundService.addRequest(uId, productId, productQuantity);

                // 실행 결과 오류 검증
                if (itemStatus == -1) {
                    return -1;
                }

                System.out.print(
                        Messages.ITEM_REGISTERED.getText()
                );
                String select = br.readLine().toUpperCase();
                if (select.charAt(0) == 'Q') {
                    rtn = requestStatus;
                    break;
                }
            }

        } catch (IOException e) {
            // 입력오류
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            InputRequestData(uId);
        } catch (ParseException e) {
            System.out.print(
                    Errors.DATE_INPUT_ERROR.getText()
            );
            InputRequestData(uId);
        }
        return rtn;
    }

    // 요청 정보 수정 입력
    public int InputRequestDataUpdate(int requestId) {
        int rtn = 0;

        try {
            System.out.print(
                    Messages.ENTER_WARE_ID.getText()
            );
            int wId = Integer.parseInt(br.readLine());

            System.out.print(
                    Messages.ENTER_DUE_DATE_OUT.getText()
            );
            String dueDate = br.readLine();
            if (dueDate.isBlank() || dueDate.length() != 8) {
                throw new IOException();
            }
            Date date = informat.parse(dueDate);
            String newDueDate = outformat.format(date);

            // 요청 정보 전송
            int requestStatus = outboundService.updateRequest(requestId, wId, newDueDate);
            if (requestStatus == -1) {
                rtn = -1;
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            InputRequestDataUpdate(requestId);
        } catch (ParseException e) {
            System.out.print(
                    Errors.DATE_INPUT_ERROR.getText()
            );
            InputRequestDataUpdate(requestId);
        }
        return rtn;
    }

    // 요청 물품 수정 입력
    public int InputRequestItemUpdate(int requestId) {
        int rtn = 0;

        try {
            System.out.print(
                    Messages.ENTER_ITEM_NUM.getText()
            );
            int itemId = Integer.parseInt(br.readLine());

            System.out.print(
                    Messages.ENTER_ITEM_ID.getText()
            );
            String productId = br.readLine();

            System.out.print(
                    Messages.ENTER_ITEM_QUANTITY.getText()
            );
            int productQuantity = Integer.parseInt(br.readLine());

            // 요청 정보 전송
            int requestStatus = outboundService.updateItem(requestId, itemId, productId, productQuantity);
            if (requestStatus == -1) {
                rtn = -1;
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            InputRequestItemUpdate(requestId);
        }

        return rtn;
    }

    public int cancelRequest() {
        int rtn = 0;
        try {
            System.out.print(
                    Messages.ENTER_CANCEL_REQUEST_ID_OUT.getText()
            );
            int requestId = Integer.parseInt(br.readLine());

            // 취소 확인
            System.out.print(
                    Messages.ENTER_CANCEL_CONFIRM_OUT.getText()
            );
            String select = br.readLine().toUpperCase();
            if (select.charAt(0) == 'N') {
                System.out.print(Messages.RETURN_MENU.getText());
            } else if (select.charAt(0) == 'Y') {
                // 요청 정보 전송
                int requestStatus = outboundService.cancelRequest(requestId);
                if (requestStatus == -1) {
                    rtn = -1;
                }
            } else {
                throw new IOException();
            }

        } catch (IOException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            cancelRequest();
        }
        return rtn;
    }

    public int showRequestInfo() {
        int rtn = 0;
        try {
            System.out.print(
                    Messages.ENTER_PRINT_REQUEST_ID_OUT.getText()
            );
            int requestId = Integer.parseInt(br.readLine());

            // 출력 확인
            System.out.print(
                    Messages.ENTER_PRINT_CONFIRM_OUT.getText()
            );
            String select = br.readLine().toUpperCase();
            if (select.charAt(0) == 'N') {
                System.out.print(Messages.RETURN_MENU.getText());
            } else if (select.charAt(0) == 'Y') {
                // 요청 정보 전송
                List<String> reqBillData = outboundService.showReqBillData(requestId);
                List<List<String>> itemBillData = outboundService.showItemBillData(requestId);

                if ((reqBillData == null) || (itemBillData == null)) {
                    System.out.print(Errors.VO_LOAD_ERROR.getText());
                    rtn = -1;
                } else {
                    printBill(reqBillData, itemBillData);
                    System.out.print(Messages.PRESS_ANY_KEY.getText());
                    String input = br.readLine();

                }
            } else {
                throw new IOException();
            }

        } catch (IOException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            showRequestInfo();
        }
        return rtn;
    }

    // 출고고지서 양식대로 출력
    public void printBill(List<String> reqList, List<List<String>> itemList) {

        System.out.printf(
                Messages.PRINT_BILL_REQUEST_OUT.getText(), reqList.get(0), reqList.get(1).split(" ")[0], reqList.get(2), reqList.get(3)
        );
        int totalPrice = 0;

        for (List<String> item : itemList) {
            System.out.printf(Messages.PRINT_BILL_ITEM.getText(), item.get(0), item.get(1), item.get(2), item.get(3)
            );
            totalPrice += (Integer.parseInt(item.get(2)) * Integer.parseInt(item.get(3)));
        }

        System.out.printf(Messages.PRINT_BILL_TOTAL.getText(), totalPrice);
    }

    public int approveRequest() {
        int rtn = 0;
        try {
            System.out.print(
                    Messages.ENTER_APPROVE_REQUEST_OUT.getText()
            );
            int requestId = Integer.parseInt(br.readLine());

            // 승인 확인
            System.out.print(
                    Messages.ENTER_APPROVE_CONFIRM_OUT.getText()
            );
            String select = br.readLine().toUpperCase();
            if (select.charAt(0) == 'N') {
                System.out.print(Messages.RETURN_MENU.getText());
            } else if (select.charAt(0) == 'Y') {
                // 승인 정보 전송
                int approveStatus = outboundService.approveRequest(requestId);

                if (approveStatus == -1) {
                    System.out.print(Errors.VO_LOAD_ERROR.getText());
                    rtn = -1;
                } else {
                    rtn = approveStatus;
                }
            }

        } catch (IOException e) {
            System.out.print(
                    Errors.INVALID_INPUT_ERROR.getText()
            );
            approveRequest();
        }
        return rtn;
    }

    public void printRequestList(List<List<String>> list) {
        for (List<String> requests : list) {
            System.out.printf(
                    Messages.PRINT_REQUEST_LIST_OUT.getText(),  requests.get(0), requests.get(1), requests.get(2),
                    requests.get(3),  requests.get(4), requests.get(5)
            );
        }
    }

    public void printRequestItemList(List<List<String>> list) {
        for (List<String> items : list) {
            System.out.printf(
                    Messages.PRINT_REQUEST_ITEM_LIST.getText(),  items.get(0), items.get(1), items.get(2),
                    items.get(3),  items.get(4), items.get(5)
            );
        }
    }

    public void printPendingRequest(List<List<String>> list) {
        for (List<String> requests : list) {
            System.out.printf(
                    Messages.PRINT_PENDING_LIST_OUT.getText(),  requests.get(0), requests.get(2), requests.get(1),
                    requests.get(3),  requests.get(4), requests.get(5)
            );
        }
    }

    public void printRequestByPeriod(List<List<String>> list) {
        int count = 0;
        for (List<String> requests : list) {
            count++;
            System.out.printf(
                    Messages.PRINT_REQUEST_LIST_PERIOD_OUT.getText(), count, requests.get(0), requests.get(2), requests.get(1),
                    requests.get(3), requests.get(4)
            );
        }
    }
    // 자신의 요청건에만 접근 가능하게 확인
    public boolean isAccessibleRequest(int requestId) {
        int status = 0;
        status = outboundService.isAccessibleRequest(requestId, user.getUIdx());
        if (status <= 0) {
            return false;
        }
        return true;
    }
}
