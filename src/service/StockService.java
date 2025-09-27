package service;

import dao.StockDao;
import dao.WarehouseDao;
import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;

import java.util.*;

public class StockService {
    private static StockService stockService;
    private StockDao stockDao;

    private StockService() {}
    public static StockService getInstance() {
        if (stockService == null) stockService = new StockService();
        return stockService;
    }

    public List<Stock> getAllStockList(){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<Stock> stockList = stockDao.getAllStockList();
        if(stockList == null){
            System.out.print("전체 재고 조회 도중 예외가 발생했습니다. ");
            return null;
        }else if(stockList.isEmpty()){
            System.out.print("현재 재고가 존재하지 않습니다. ");
            return null;
        }
        return stockList;
    }

    public List<Stock> getCategoryStockList(int num, String cName){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<Stock> stockList = null;
        switch(num){
            case 2 -> stockList = stockDao.getPrimaryCategoryStockList(cName);
            case 3 -> stockList = stockDao.getSecondaryCategoryStockList(cName);
            case 4 -> stockList = stockDao.getTertiaryCategoryStockList(cName);
        }
        if(stockList == null){
            System.out.print("해당 카테고리가 존재하지 않거나 조회 도중 예외가 발생했습니다. ");
            return null;
        }else if(stockList.isEmpty()){
            System.out.print("현재 재고가 존재하지 않습니다. ");
            return null;
        }

        return stockList;
    }

    public List<Stock> getProductStockList(String pIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<Stock> stockList = stockDao.getProductStockList(pIdx);
        if(stockList == null){
            System.out.print("바코드 번호에 대한 재고 조회 도중 예외가 발생했습니다. ");
            return null;
        }else if(stockList.isEmpty()){
            System.out.print("현재 재고가 존재하지 않습니다. ");
            return null;
        }
        return stockList;
    }

    public CheckLog addCheckLog(int wIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        int result = stockDao.addCheckLog(wIdx);
        if(result != 1) return null;

        CheckLog newCheckLog = stockDao.getNewCheckLog();
        return newCheckLog;
    }

    public int removeCheckLog(int clIdx, int wIdx, boolean status){
        if(stockDao == null) stockDao = StockDao.getInstance();

        if(status){
            boolean check = stockDao.checkWarehouseAdminCondition(clIdx, wIdx);
            if(!check) return -1; //창고관리자가 관리하는 창고가 아님!
        }

        int result = stockDao.removeCheckLog(clIdx);
        return result;
    }

    public List<CheckLog> getCheckLogList(int wIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<CheckLog> checkLogList = null;
        if(wIdx == 0){//총관리자인 경우
            checkLogList = stockDao.getCheckLogList(1, 0);
        }else{
            //창고관리자인 경우
            checkLogList = stockDao.getCheckLogList(2, wIdx);
        }

        if(checkLogList == null) {
            System.out.print("재고 실사 로그 조회 중 예외가 발생했습니다."); return null;
        }else if(checkLogList.isEmpty()) {
            System.out.print("재고 실사 로그가 기록되지 않았습니다."); return null;
        }

        return checkLogList;
    }

    public List<CheckLog> getSectionCheckLogList(String wUniqueNum, String wsName) {
        if(stockDao == null) stockDao = StockDao.getInstance();

        boolean result = stockDao.checkWsNameExist(wsName);
        if(!result) {
            System.out.print("입력하신 창고 섹션은 존재하지 않습니다. ");return null;
        }

        int result2 = stockDao.checkWarehouseIsStorage(wUniqueNum);

        if(result2 == 0){
            System.out.print("해당 창고는 마이크로풀필먼트입니다. ");
            return null;
        }

        List<CheckLog> checkLogList = stockDao.getSectionCheckLoglist(wUniqueNum, wsName);

        if(checkLogList == null) {
            System.out.print("창고 조회 중 예외가 발생했습니다. "); return null;
        }else if(checkLogList.isEmpty()){
            System.out.print("해당 창고 섹션이 존재하지 않습니다. "); return null;
        }
        return checkLogList;
    }

    public List<CheckLog> getWarehouseCheckLogList(String wUniqueNum) {
        if(stockDao == null) stockDao = StockDao.getInstance();

        WarehouseDao temp = WarehouseDao.getInstance();
        int result = temp.checkWarehouseExist(wUniqueNum);
        if(result == 0) {
            System.out.println("해당 창고는 존재하지 않습니다.");
            return null;
        }

        List<CheckLog> checkLogList = stockDao.getWarehouseCheckLogList(wUniqueNum);
        if(checkLogList == null) {
            System.out.print("창고번호별 재고 실사 조회 중 예외가 발생했습니다. ");
            return null;
        }
        return checkLogList;
    }

    public Warehouse getWarehouseInfo(int wIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        Warehouse warehouse = stockDao.getWarehouseInfo(wIdx);
        return warehouse;
    }

    public boolean updateCheckLog(int clIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        boolean result = stockDao.updateCheckLog(clIdx);
        return result;
    }

    public boolean checkUpdateCondition(int clIdx, int wIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        boolean result = stockDao.checkUpdateCondition(clIdx, wIdx);
        return result;
    }
}
