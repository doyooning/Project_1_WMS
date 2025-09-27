package service;

import dao.StockDao;
import dao.WarehouseDao;
import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;
import exception.DaoException;
import exception.ExceptionManager;

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
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = stockDao.getAllStockList();
            if(stockList.isEmpty()){
                throw new ExceptionManager("현재 재고가 존재하지 않습니다.");
            }
            return stockList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Stock> getCategoryStockList(int num, String cName){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = null;
            switch(num){
                case 2 -> stockList = stockDao.getPrimaryCategoryStockList(cName);
                case 3 -> stockList = stockDao.getSecondaryCategoryStockList(cName);
                case 4 -> stockList = stockDao.getTertiaryCategoryStockList(cName);
            }
            if(stockList == null){
                throw new ExceptionManager("해당 카테고리가 존재하지 않습니다.");
            }else if(stockList.isEmpty()){
                throw new ExceptionManager("현재 재고가 존재하지 않습니다.");
            }

            return stockList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Stock> getProductStockList(String pIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = stockDao.getProductStockList(pIdx);
            if(stockList.isEmpty()){
                throw new ExceptionManager("현재 재고가 존재하지 않습니다.");
            }
            return stockList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public CheckLog addCheckLog(int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            int result = stockDao.addCheckLog(wIdx);
            if(result != 1) {
                throw new ExceptionManager("재고 실사가 등록되지 않았습니다.");
            }

            CheckLog newCheckLog = stockDao.getNewCheckLog();
            return newCheckLog;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public int removeCheckLog(int clIdx, int wIdx, boolean status){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            if(status){
                boolean check = stockDao.checkWarehouseAdminCondition(clIdx, wIdx);
                if(!check) return -2; //창고관리자가 관리하는 창고가 아님!
            }

            int result = stockDao.removeCheckLog(clIdx);
            return result;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public List<CheckLog> getCheckLogList(int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<CheckLog> checkLogList = null;
            if(wIdx == 0){//총관리자인 경우
                checkLogList = stockDao.getCheckLogList(1, 0);
            }else{
                //창고관리자인 경우
                checkLogList = stockDao.getCheckLogList(2, wIdx);
            }

            if(checkLogList.isEmpty()) {
                throw new ExceptionManager("재고 실사로그가 등록되지 않았습니다.");
            }

            return checkLogList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<CheckLog> getSectionCheckLogList(String wUniqueNum, String wsName) {
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            boolean result = stockDao.checkWsNameExist(wsName);
            if(!result) {
                throw new ExceptionManager("입력하신 창고 섹션은 존재하지 않습니다.");
            }

            int result2 = stockDao.checkWarehouseIsStorage(wUniqueNum);

            if(result2 == 0){
                throw new ExceptionManager("해당 창고는 마이크로풀필먼트입니다.");
            }

            List<CheckLog> checkLogList = stockDao.getSectionCheckLoglist(wUniqueNum, wsName);

            if(checkLogList.isEmpty()){
                System.out.println("해당 창고 섹션이 존재하지 않습니다.");
            }
            return checkLogList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<CheckLog> getWarehouseCheckLogList(String wUniqueNum) {
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            WarehouseDao temp = WarehouseDao.getInstance();
            int result = temp.checkWarehouseExist(wUniqueNum);
            if(result == 0) {
                throw new ExceptionManager("해당 창고는 존재하지 않습니다.");
            }

            List<CheckLog> checkLogList = stockDao.getWarehouseCheckLogList(wUniqueNum);
            if(checkLogList.isEmpty()){
                throw new ExceptionManager("해당 창고번호가 존재하지 않습니다. ");
            }
            return checkLogList;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Warehouse getWarehouseInfo(int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            Warehouse warehouse = stockDao.getWarehouseInfo(wIdx);
            return warehouse;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateCheckLog(int clIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            boolean result = stockDao.updateCheckLog(clIdx);
            return result;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean checkUpdateCondition(int clIdx, int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            boolean result = stockDao.checkUpdateCondition(clIdx, wIdx);
            return result;
        } catch (DaoException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
