package controller;

public interface InOutboundController {

    // 메뉴 표시
    void showMenu(int authNum);

    // 메뉴 선택
    int selectMenu(int authNum, int menuNum);

    // 수정 메뉴 표시
    void showUpdateMenu();

    // 수정 메뉴 선택
    int selectUpdateMenu(int menuNum);

    // 현황 조회 메뉴 표시
    void showInfoMenu();

    // 현황 조회 메뉴 선택
    int selectInfoMenu(int menuNum);


}
