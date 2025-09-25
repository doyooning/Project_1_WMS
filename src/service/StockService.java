package service;

import dao.StockDao;
import domain.Stock;

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
}
