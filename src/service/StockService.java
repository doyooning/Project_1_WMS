package service;

import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;

import java.util.List;

public interface StockService {

    // 전체 재고 목록 조회
    List<Stock> getAllStockList();

    // 카테고리별 재고 목록 조회
    List<Stock> getCategoryStockList(int num, String cName);

    // 특정 상품 재고 목록 조회
    List<Stock> getProductStockList(String pIdx);

    // 재고 실사 로그 추가
    CheckLog addCheckLog(int wIdx);

    // 재고 실사 로그 삭제
    int removeCheckLog(int clIdx, int wIdx, boolean status);

    // 실사 로그 목록 조회 (총관리자/창고관리자 구분)
    List<CheckLog> getCheckLogList(int wIdx);

    // 섹션별 실사 로그 목록 조회
    List<CheckLog> getSectionCheckLogList(String wUniqueNum, String wsName);

    // 창고별 실사 로그 목록 조회
    List<CheckLog> getWarehouseCheckLogList(String wUniqueNum);

    // 창고 정보 조회
    Warehouse getWarehouseInfo(int wIdx);

    // 실사 로그 수정
    boolean updateCheckLog(int clIdx);

    // 실사 로그 수정 가능 여부 확인
    boolean checkUpdateCondition(int clIdx, int wIdx);
}
