package controller;

import service.OutboundService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 메뉴 첫 접근
 * : BoardController/showBoardMenu(), selectBoardMenu()
 * [출고 관리]
 * */
public class OutboundControllerImpl implements InOutboundController{
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

    @Override
    public void showMenu(int authNum) {
        // 권한 구분 임의 구현..
        if (authNum == 1) {
            System.out.print(
                    """
                    ============================================================
                    1. 출고 요청 승인		  2. 출고 요청 수정		 3. 출고 요청 취소
                    4. 출고리스트 조회	      5. 출고 현황 조회		 6. 나가기
                    ============================================================
                    메뉴를 고르세요 :\s"""
            );

        } else if (authNum == 2) {
            System.out.print(
                    """
                    ============================================================
                    1. 출고 요청 		  2. 출고 요청 수정		 3. 출고 요청 취소
                    4. 출고리스트 조회	      5. 출고 현황 조회		 6. 나가기
                    ============================================================
                    메뉴를 고르세요 :\s"""
            );
        }
        try {
            // 메뉴 번호 입력받음
            int menuNum = Integer.parseInt(br.readLine());
            selectMenu(authNum, menuNum);

        } catch (IOException | NumberFormatException e) {
            Errors.INVALID_INPUT_ERROR.getText();
            showMenu(authNum);

        } catch (Exception e) {
            Errors.UNEXPECTED_ERROR.getText();
            e.printStackTrace();
            showMenu(authNum);
        }
    }

    @Override
    public int selectMenu(int authNum, int menuNum) {
        switch (menuNum) {
            case 1 -> {
                int status = 0;
                if(authNum == 1) {
                    System.out.println("1. 출고 요청 승인");
                    // 미승인된 입고요청 목록 출력
                    status = outboundService.approveRequest();

                } else if(authNum == 2) {
                    // 1. 출고 요청
                    status = InputRequestData();

                }
                if (status == -1) {
                    Errors.DATA_INPUT_ERROR.getText();
                } else {
                    System.out.printf(
                            """
                            ============================================================
                            출고 요청이 등록되었습니다. 회원님의 요청 번호는 [%d] 입니다.
                            ============================================================
                            """, status
                    );
                }
            }
            case 2 -> {
                // 2. 출고 요청 수정
                showUpdateMenu();

            }
            case 3 -> {
                int status = 0;
                // 3. 출고 요청 취소
                cancelRequest();
                if (status == -1) {
                    Errors.DATA_INPUT_ERROR.getText();
                } else {
                    System.out.print(
                            """
                            ============================================================
                            출고 요청 취소가 완료되었습니다.
                            ============================================================
                            """
                    );
                }

            }
            case 4 -> {
                System.out.println("4. 출고리스트 조회");
//                outboundService.showRequestInfo();

            }

            case 5 -> {
                System.out.println("5. 출고 현황 조회");
                outboundService.getBoundInfo();

            }

            case 6 -> {
                System.out.println("6. 나가기");
                System.exit(0);
            }
        }

        return 0;
    }

    @Override
    public void showUpdateMenu() {
        int status = 0;
        System.out.print(
                """
                ============================================================
                ####################### 출고 요청 수정 #######################
                ============================================================
                1. 요청 정보 수정 	    2. 물품 정보 수정           3. 뒤로가기
                ============================================================
                메뉴를 고르세요 :\s"""
        );
        try {
            int menuNum = Integer.parseInt(br.readLine());
            status = selectUpdateMenu(menuNum);
            if (status == 1) {
                showUpdateMenu();
            } else if (status == -1) {
                Errors.DATA_INPUT_ERROR.getText();
                showUpdateMenu();
            }

        } catch (IOException | NumberFormatException e) {
            Errors.INVALID_INPUT_ERROR.getText();
            showUpdateMenu();

        } catch (Exception e) {
            Errors.UNEXPECTED_ERROR.getText();
            e.printStackTrace();
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
                            """
                            ============================================================
                            수정할 입고요청 번호를 입력해주세요 :\s"""
                    );
                    int requestId = Integer.parseInt(br.readLine());
                    status = InputRequestDataUpdate(requestId);
                    if (status == -1) {
                        return -1;
                    }
                }
                // 물품 정보 수정
                case 2 -> {
                    // 요청 번호 입력
                    System.out.print(
                            """
                            ============================================================
                            수정할 입고요청 번호를 입력해주세요 :\s"""
                    );
                    int requestId = Integer.parseInt(br.readLine());
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
            Errors.INVALID_INPUT_ERROR.getText();
        }
        return 1;
    }

    @Override
    public void showInfoMenu() {

    }

    @Override
    public int selectInfoMenu(int menuNum) {
        return 0;
    }

    public int InputRequestData() {
        int rtn = 0;
        try {
            System.out.print(
                    """
                    ============================================================
                    ######################### 출고 요청 #########################
                    ============================================================
                    요청 정보를 작성해주세요.
                    출고위치(창고번호) :\s"""
            );
            int wId = Integer.parseInt(br.readLine());

            System.out.print(
                    """
                    ============================================================
                    출고기한(8자리 숫자로 입력) :\s"""
            );
            String dueDate = br.readLine();
            Date date = informat.parse(dueDate);
            String newDueDate = outformat.format(date);
            // 요청 정보 전송
            int requestStatus = outboundService.addRequest(wId, newDueDate);

            // 실행 결과 오류 검증
            if (requestStatus == -1) {
                Errors.DATA_INPUT_ERROR.getText();
                return -1;
            }

            while (true) {
                System.out.print(
                        """
                        ============================================================
                        물품 정보를 작성해주세요.
                        물품번호 :\s"""
                );
                String productId = br.readLine();

                System.out.print(
                        """
                        ============================================================
                        물품개수 :\s"""
                );
                int productQuantity = Integer.parseInt(br.readLine());
                // 물품 정보 전송
                int itemStatus = outboundService.addRequest(productId, productQuantity);

                // 실행 결과 오류 검증
                if (itemStatus == -1) {
                    Errors.DATA_INPUT_ERROR.getText();
                    return -1;
                }

                System.out.print(
                        """
                        ============================================================
                        물품 정보가 정상적으로 입력되었습니다.
                        ============================================================
                        Q를 입력하면 메뉴 화면으로 이동하며,
                        물품 정보를 추가로 입력하려면 Q를 제외한 아무 키나 입력하십시오.
                        :\s"""
                );
                String select = br.readLine();
                if (select.charAt(0) == 'Q' || select.charAt(0) == 'q') {
                    rtn = requestStatus;
                    break;
                }
            }

        } catch (IOException e) {
            // 입력오류
            Errors.INVALID_INPUT_ERROR.getText();
        } catch (ParseException e) {
            e.printStackTrace();
            InputRequestData();
        }
        return rtn;
    }

    // 요청 정보 수정 입력
    public int InputRequestDataUpdate(int requestId) {
        int rtn = 0;

        try {
            System.out.print(
                    """
                    ============================================================
                    수정할 내용을 입력해주세요.
                    창고번호 :\s"""
            );
            int wId = Integer.parseInt(br.readLine());

            System.out.print(
                    """
                    ============================================================
                    입고기한(8자리 숫자로 입력) :\s"""
            );
            String dueDate = br.readLine();
            Date date = informat.parse(dueDate);
            String newDueDate = outformat.format(date);

            // 요청 정보 전송
            int requestStatus = outboundService.updateRequest(requestId, wId, newDueDate);
            if (requestStatus == -1) {
                rtn = -1;
            }

        } catch (IOException | NumberFormatException | ParseException e) {
            return -1;
        }
        return rtn;
    }

    // 요청 물품 수정 입력
    public int InputRequestItemUpdate(int requestId) {
        int rtn = 0;

        try {
            System.out.print(
                    """
                    ============================================================
                    물품 순번을 입력해주세요.
                    순번 :\s"""
            );
            int itemId = Integer.parseInt(br.readLine());

            System.out.print(
                    """
                    ============================================================
                    물품 정보를 작성해주세요.
                    물품번호 :\s"""
            );
            String productId = br.readLine();

            System.out.print(
                    """
                    ============================================================
                    물품개수 :\s"""
            );
            int productQuantity = Integer.parseInt(br.readLine());

            // 요청 정보 전송
            int requestStatus = outboundService.updateItem(requestId, itemId, productId, productQuantity);
            if (requestStatus == -1) {
                rtn = -1;
            }

        } catch (IOException | NumberFormatException e) {
            return -1;
        }

        return rtn;
    }

    public int cancelRequest() {
        int rtn = 0;
        try {
            System.out.print(
                    """
                    ============================================================
                    취소할 입고요청 번호를 입력해주세요 :\s"""
            );
            int requestId = Integer.parseInt(br.readLine());

            // 취소 확인
            System.out.print(
                    """
                    ============================================================
                    출고 요청을 취소하시겠습니까?
                    (Y/N 입력) :\s"""
            );
            String select = br.readLine().toUpperCase();
            if (select.charAt(0) == 'N') {
                System.out.print("""
                    ============================================================
                    메뉴 화면으로 이동합니다.
                    ============================================================
                    """);
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
            System.out.println(Errors.INVALID_INPUT_ERROR.getText());
            return -1;
        }
        return rtn;
    }
}
