package controller;

public interface InOutboundController {

    // 메뉴 표시
    void showMenu(int[] userData);

    // 메뉴 선택
    int selectMenu(int[] userData, int menuNum);

    // 수정 메뉴 표시
    void showUpdateMenu();

    // 수정 메뉴 선택
    int selectUpdateMenu(int menuNum);

    // 현황 조회 메뉴 표시
    void showInfoMenu(int userNum);

    // 현황 조회 메뉴 선택
    int selectInfoMenu(int menuNum, int uId);


}
