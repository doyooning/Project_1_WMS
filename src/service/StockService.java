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
}
