package controller;

import service.InboundService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 메뉴 첫 접근
* : BoardController/showBoardMenu(), selectBoardMenu()
* -> 유저번호(uIdx)를 파라미터로 넘겨야 요청 등록에 사용 가능
* [입고 관리]
* */

public class InboundControllerImpl implements InOutboundController{
    // statics
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat outformat = new SimpleDateFormat("yyyy-MM-dd");



    // 싱글턴 패턴을 위한 인스턴스 생성
    private static InboundControllerImpl inboundControllerImpl;
    private InboundControllerImpl() {}

    // getInstance는 1회만
    public static InboundControllerImpl getInstance() {
        if (inboundControllerImpl == null) {
            inboundControllerImpl = new InboundControllerImpl();
        }
        return inboundControllerImpl;
    }

    private InboundService inboundService = InboundService.getInstance();

    @Override
    public void showMenu(int authNum) {
        int status = 0;
        // 권한 구분 임의 구현..
        if (authNum == 1) {
            System.out.print(
                    """
                    ============================================================
                    1. 입고 요청 승인		  2. 입고 요청 수정		 3. 입고 요청 취소
                    4. 입고 고지서 출력	  5. 입고 현황 조회		 6. 나가기
                    ============================================================
                    메뉴를 고르세요 :\s"""
            );

        } else if (authNum == 2) {
            System.out.print(
                    """
                    ============================================================
                    1. 입고 요청 		  2. 입고 요청 수정		 3. 입고 요청 취소
                    4. 입고 고지서 출력	  5. 입고 현황 조회		 6. 나가기
                    ============================================================
                    메뉴를 고르세요 :\s"""
            );
        }
        try {
            // 메뉴 번호 입력받음
            int menuNum = Integer.parseInt(br.readLine());
            status = selectMenu(authNum, menuNum);
            if (status == 1) {
                showMenu(authNum);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    """
                    ============================================================
                    [오류] 유효하지 않은 입력입니다. 다시 입력해 주십시오.
                    """
            );
            showMenu(authNum);

        } catch (Exception e) {
            System.out.print(
                    """
                    ============================================================
                    [오류] 예기치 못한 오류가 발생하였습니다. 다시 입력해 주십시오.
                    """
            );
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
                    System.out.println("1. 입고 요청 승인");
                    // 미승인된 입고요청 목록 출력

                    status = inboundService.approveRequest();

                } else if(authNum == 2) {
                    // 1. 입고 요청
                    status = InputRequestData();

                }
                if (status == -1) {
                    System.out.println(
                            """
                            ============================================================
                            데이터 입력 중 오류가 발생하였습니다.
                            ============================================================
                            """
                    );
                } else {
                    System.out.printf(
                            """
                            ============================================================
                            입고 요청이 등록되었습니다. 회원님의 요청 번호는 [%d] 입니다.
                            ============================================================
                            """, status
                    );
                }
            }
            case 2 -> {
                int status = 0;
                System.out.println("2. 입고 요청 수정");
                status = InputRequestData();

                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 성공");
                }

            }
            case 3 -> {
                int status = 0;
                System.out.println("3. 입고 요청 취소");
                inboundService.cancelRequest();
                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 섣공");
                }

            }
            case 4 -> {
                System.out.println("4. 입고고지서 출력");
                inboundService.showRequestInfo();

            }

            case 5 -> {
                System.out.println("5. 입고 현황 조회");
                inboundService.getBoundInfo();

            }

            case 6 -> {
                System.out.println("6. 나가기");
                return 0;
            }
        }

        return 1;
    }

    @Override
    public void showUpdateMenu() {
        int status = 0;
        System.out.print(
                """
                ============================================================
                ####################### 입고 요청 수정 #######################
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
                System.out.print(
                        """
                        ============================================================
                        [오류] 오류가 발생했습니다. 다시 입력해 주십시오.
                        """
                );
                showUpdateMenu();
            }

        } catch (IOException | NumberFormatException e) {
            System.out.print(
                    """
                    ============================================================
                    [오류] 유효하지 않은 입력입니다. 다시 입력해 주십시오.
                    """
            );
            showUpdateMenu();

        } catch (Exception e) {
            System.out.print(
                    """
                    ============================================================
                    [오류] 예기치 못한 오류가 발생하였습니다. 다시 입력해 주십시오.
                    """
            );
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
                    System.out.println(
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
                    System.out.println(
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
            System.out.print(
                    """
                    ============================================================
                    [오류] 유효하지 않은 입력입니다. 다시 입력해 주십시오.
                    """
            );
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


    /*
    * ---------------------서브 메서드----------------------
    * */

    // 입고 요청 정보를 서비스에 보내기
    // 창고번호 int, 입고기한 date -> 물품번호 , 물품개수
    public int InputRequestData() {
        int rtn = 0;
        try {
            System.out.print(
                    """
                    ============================================================
                    ######################### 입고 요청  #########################
                    ============================================================
                    요청 정보를 작성해주세요.
                    입고위치(창고번호) :\s"""
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
            int requestStatus = inboundService.addRequest(wId, newDueDate);

            // 실행 결과 오류 검증
            if (requestStatus == -1) {
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
                int itemStatus = inboundService.addRequest(productId, productQuantity);

                // 실행 결과 오류 검증
                if (itemStatus == -1) {
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
                String selectYn = br.readLine();
                if (selectYn.charAt(0) == 'Q' || selectYn.charAt(0) == 'q') {
                    rtn = requestStatus;
                    break;
                }
            }

        } catch (IOException e) {
            // 입력오류
            System.out.println(
                    """
                    ============================================================
                    해당하는 항목이 없거나, 잘못 입력하셨습니다.
                    ============================================================
                    """
            );
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
                    입고위치(8자리 숫자로 입력) :\s"""
            );
            String dueDate = br.readLine();
            Date date = informat.parse(dueDate);
            String newDueDate = outformat.format(date);

            // 요청 정보 전송
            int requestStatus = inboundService.updateRequest(requestId, wId, newDueDate);
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
            int requestStatus = inboundService.updateItem(requestId, itemId, productId, productQuantity);
            if (requestStatus == -1) {
                rtn = -1;
            }

        } catch (IOException | NumberFormatException e) {
            return -1;
        }

        return rtn;
    }

}
