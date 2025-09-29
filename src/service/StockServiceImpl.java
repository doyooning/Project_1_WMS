package service;

import common.Errors;
import dao.StockDao;
import dao.WarehouseDao;
import domain.CheckLog;
import domain.Stock;
import domain.Warehouse;
import exception.DaoException;
import exception.ExceptionManager;

import java.util.*;

public class StockServiceImpl implements StockService {
    private static StockServiceImpl stockServiceImpl;
    private StockDao stockDao;

    private StockServiceImpl() {}
    public static StockServiceImpl getInstance() {
        if (stockServiceImpl == null) stockServiceImpl = new StockServiceImpl();
        return stockServiceImpl;
    }

    public List<Stock> getAllStockList(){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = stockDao.getAllStockList();
            if(stockList.isEmpty()){
                throw new ExceptionManager(Errors.NO_STOCK.getText());
            }
            return stockList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Stock> getCategoryStockList(int num, String cName){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = null;
            switch(num){
                case 2 -> stockList = stockDao.getPrimaryCategoryStockList(cName.trim());
                case 3 -> stockList = stockDao.getSecondaryCategoryStockList(cName.trim());
                case 4 -> stockList = stockDao.getTertiaryCategoryStockList(cName.trim());
            }
            if(stockList == null){
                throw new ExceptionManager(Errors.NO_CATEGORY.getText());
            }else if(stockList.isEmpty()){
                throw new ExceptionManager(Errors.NO_STOCK.getText());
            }

            return stockList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Stock> getProductStockList(String pIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            List<Stock> stockList = stockDao.getProductStockList(pIdx);
            if(stockList.isEmpty()){
                throw new ExceptionManager(Errors.NO_STOCK.getText());
            }
            return stockList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public CheckLog addCheckLog(int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            int result = stockDao.addCheckLog(wIdx);
            if(result != 1) {
                throw new ExceptionManager(Errors.NO_CHECKLOG_ADD.getText());
            }

            CheckLog newCheckLog = stockDao.getNewCheckLog();
            return newCheckLog;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int removeCheckLog(int clIdx, int wIdx, boolean status){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            if(status){
                boolean check = stockDao.checkWarehouseAdminCondition(clIdx, wIdx);
                if(!check) throw new ExceptionManager(Errors.NO_ADMISSION_WAREHOUSE.getText()); //창고관리자가 관리하는 창고가 아님!
            }

            int result = stockDao.removeCheckLog(clIdx);
            if(result == -2) throw new ExceptionManager(Errors.CHECKLOG_ALREADY_DELETE.getText());
            else if(result == 0) throw new ExceptionManager(Errors.NO_ADMISSION_WAREHOUSE.getText());
            else if(result == -1) throw new ExceptionManager(Errors.NO_CHECKLOG_EXIST.getText());

            return result;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
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
                throw new ExceptionManager(Errors.NO_CHECKLOG_ADD.getText());
            }

            return checkLogList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<CheckLog> getSectionCheckLogList(String wUniqueNum, String wsName) {
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            boolean result = stockDao.checkWsNameExist(wsName);
            if(!result) {
                throw new ExceptionManager(Errors.NO_WAREHOUSESECTION.getText());
            }

            int result2 = stockDao.checkWarehouseIsStorage(wUniqueNum);

            if(result2 == 0){
                throw new ExceptionManager(Errors.CURRENT_WAREHOUSE_IS_MF.getText());
            }

            List<CheckLog> checkLogList = stockDao.getSectionCheckLoglist(wUniqueNum, wsName);

            if(checkLogList.isEmpty()){
                throw new ExceptionManager(Errors.NO_CHECKLOG_ADD.getText());
            }
            return checkLogList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<CheckLog> getWarehouseCheckLogList(String wUniqueNum) {
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            WarehouseDao temp = WarehouseDao.getInstance();
            int result = temp.checkWarehouseExist(wUniqueNum);
            if(result == 0) {
                throw new ExceptionManager(Errors.NO_WAREHOUSE.getText());
            }

            List<CheckLog> checkLogList = stockDao.getWarehouseCheckLogList(wUniqueNum);
            if(checkLogList.isEmpty()){
                throw new ExceptionManager(Errors.NO_CHECKLOG_ADD.getText());
            }
            return checkLogList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Warehouse getWarehouseInfo(int wIdx){
        //현재 WarehouseController에서는 창고관리자가 배당받은 wIdx만 알 수 있기 때문에 정보를 불러와서, 해당 창고타입을 체크하기 위함
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            Warehouse warehouse = stockDao.getWarehouseInfo(wIdx);
            return warehouse;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean updateCheckLog(int clIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            int result = stockDao.updateCheckLog(clIdx);
            if(result == -1) throw new ExceptionManager(Errors.NO_CHECKLOG_UPDATE.getText());
            else if(result == 0) throw new ExceptionManager(Errors.CHECKLOG_UPDATE_WRONG.getText());
            return true;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean checkUpdateCondition(int clIdx, int wIdx){
        try {
            if(stockDao == null) stockDao = StockDao.getInstance();

            boolean result = stockDao.checkUpdateCondition(clIdx, wIdx);
            if(!result) throw new ExceptionManager(Errors. NO_ADMISSION_UPDATE_CHECKLOG.getText());
            return result;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
