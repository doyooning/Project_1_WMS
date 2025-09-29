package controller;

import exception.ExceptionManager;

public interface StockController {

    /**
     * 현재 사용자와 창고관리자 정보 설정
     */
    void setCurrentUser(String currentUser, domain.WarehouseAdmin warehouseAdmin);


    /**
     * 재고 메뉴 화면 표시
     */
    void showStockMenu();

    /**
     * 재고 조회 메뉴 화면 표시
     */
    void showStockSearchMenu();

}
