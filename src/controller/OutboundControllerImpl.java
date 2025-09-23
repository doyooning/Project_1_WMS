package controller;

import dao.OutboundDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * 메뉴 첫 접근
 * : BoardController/showBoardMenu(), selectBoardMenu()
 * [출고 관리]
 * */
public class OutboundControllerImpl implements InOutboundController{
    // statics
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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

    private OutboundDao outboundDao;

    @Override
    public void showMenu() {

    }

    @Override
    public int selectMenu(int menuNum) {
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
