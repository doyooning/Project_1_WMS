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
            System.out.println(
                    """
                    ============================================================
                    1. 출고 요청 승인		  2. 출고 요청 수정		 3. 출고 요청 취소
                    4. 출고리스트 조회	      5. 출고 현황 조회		 6. 나가기
                    ============================================================
                    메뉴를 고르세요 :\s"""
            );

        } else if (authNum == 2) {
            System.out.println(
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
                    System.out.println("1. 출고 요청 승인");
                    // 미승인된 입고요청 목록 출력
                    status = outboundService.approveRequest();

                } else if(authNum == 2) {
                    System.out.println("1. 출고 요청");
                    status = getInputRequestData();

                }
                if (status == -1) {
                    System.out.println(".");
                    System.out.println(".");
                    System.out.println(".");
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
                int status = 0;
                System.out.println("2. 출고 요청 수정");
//                outboundService.updateRequest(, );
                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 성공");
                }

            }
            case 3 -> {
                int status = 0;
                System.out.println("3. 출고 요청 취소");
                outboundService.cancelRequest();
                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 섣공");
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

    }

    @Override
    public int selectUpdateMenu(int menuNum) {
        return 0;
    }

    @Override
    public void showInfoMenu() {

    }

    @Override
    public int selectInfoMenu(int menuNum) {
        return 0;
    }

    public int getInputRequestData() {
        int rtn = 0;
        try {
            System.out.println(
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
                System.out.println(
                        """
                        ============================================================
                        데이터 입력 중 오류가 발생하였습니다. 다시 실행해 주십시오.
                        ============================================================
                        """
                );
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
                    System.out.println(
                            """
                            ============================================================
                            데이터 입력 중 오류가 발생하였습니다. 다시 실행해 주십시오.
                            ============================================================
                            """
                    );
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
            getInputRequestData();
        }
        return rtn;
    }
}
