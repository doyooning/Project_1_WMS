package controller;

public interface InOutboundController {

    // 메뉴 표시
    void showMenu();

    // 메뉴 선택
    int selectMenu();

    // 수정 메뉴 표시
    void showUpdateMenu();

    // 수정 메뉴 선택
    int selectUpdateMenu();

    // 현황 조회 메뉴 표시
    void showInfoMenu();

    // 현황 조회 메뉴 선택
    int selectInfoMenu();


}
