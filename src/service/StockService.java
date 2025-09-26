package service;

import dao.StockDao;
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
        if(stockList.isEmpty() || stockList == null) {} //예외처리
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
        if(stockList == null || stockList.isEmpty()) {} // 예외처리
        return stockList;
    }

    public List<Stock> getProductStockList(String pIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<Stock> stockList = stockDao.getProductStockList(pIdx);
        if(stockList.isEmpty() || stockList == null) {} //예외처리
        return stockList;
    }

    public CheckLog addCheckLog(){
        if(stockDao == null) stockDao = StockDao.getInstance();

        int result = stockDao.addCheckLog();
        if(result != 1) return null;

        CheckLog newCheckLog = stockDao.getNewCheckLog();
        return newCheckLog;
    }

    public int removeCheckLog(int clIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        int result = stockDao.removeCheckLog(clIdx);
        return result;
    }

    public List<CheckLog> getCheckLogList(int wIdx){
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<CheckLog> checkLogList = null;
        //총관리자인 경우
        checkLogList = stockDao.getCheckLogList(1, 0);
        //창고관리자인 경우
        checkLogList = stockDao.getCheckLogList(2, wIdx);

        return checkLogList;
    }

    public boolean checkWarehouseIsStorage(String wUniqueNum){
        if(stockDao == null) stockDao = StockDao.getInstance();

        int result = stockDao.checkWarehouseIsStorage(wUniqueNum);
        if(result == 0) return false;
        return true;
    }

    public List<CheckLog> getSectionCheckLogList(String wUniqueNum, String wsName) {
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<CheckLog> checkLogList = stockDao.getSectionCheckLoglist(wUniqueNum, wsName);
        return checkLogList;
    }

    public List<CheckLog> getWarehouseCheckLogList(String wUniqueNum) {
        if(stockDao == null) stockDao = StockDao.getInstance();

        List<CheckLog> checkLogList = stockDao.getWarehouseCheckLogList(wUniqueNum);
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
