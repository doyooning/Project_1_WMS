package controller;

import service.InboundService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
* 메뉴 첫 접근
* : BoardController/showBoardMenu(), selectBoardMenu()
* [입고 관리]
* */

public class InboundControllerImpl implements InOutboundController{
    // statics
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
            selectMenu(authNum, menuNum);

        } catch (IOException e) {
            System.out.println("IOException");
            showMenu(authNum);

        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException");
            showMenu(authNum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int selectMenu(int authNum, int menuNum) {
        switch (menuNum) {
            case 1 -> {
                int status = 0;
                if(authNum == 1) {
                    System.out.println("1. 입고 요청 승인");


                    status = inboundService.approveRequest();

                } else if(authNum == 2) {
                    System.out.println("1. 입고 요청");
                    inboundService.addRequest();
                }
                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 성공");
                }
            }
            case 2 -> {
                int status = 0;
                System.out.println("2. 입고 요청 수정");
                inboundService.updateRequest();
                if (status == -1) {
                    System.out.println("오류 발생");
                } else {
                    System.out.println("실행 섣공");
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
}
