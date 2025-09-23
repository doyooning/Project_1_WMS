package controller;

import dao.InboundDao;

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

    private InboundDao inboundDao;

    @Override
    public void showMenu() {
        System.out.print(
                """
                ============================================================
                1. 입고 요청 승인		  2. 입고 요청 수정		 3. 입고 요청 취소
                4. 입고 고지서 출력	  5. 입고 현황 조회		 6. 나가기
                ============================================================
                메뉴를 고르세요 :\s
                """
        );
        try {
            int menuNum = Integer.parseInt(br.readLine());
            selectMenu(menuNum);
        } catch (IOException e) {
            System.out.println("IOException");
            showMenu();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException");
            showMenu();
        }

    }

    @Override
    public int selectMenu(int menuNum) {
        switch (menuNum) {
            case 1 -> {
                System.out.println("1. 입고 요청 승인");

            }
            case 2 -> {

            }
            case 3 -> {

            }
            case 4 -> {

            }

            case 5 -> {

            }

            case 6 -> {

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
