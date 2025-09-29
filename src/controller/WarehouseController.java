package controller;

import exception.ExceptionManager;

public interface WarehouseController {

    /**
     * 창고 메뉴 화면 표시
     * - 총관리자용
     */
    void showWarehouseMenu();

    /**
     * 창고 조회 메뉴 화면 표시
     * - 총관리자, 창고관리자용
     */
    void showWarehouseSearchMenu();

    /**
     * 일반회원은 showAllWarehouseList만 가능함
     */
    void showAllWarehouseList();
}