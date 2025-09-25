package dao;

import Util.DBUtil;
import domain.Stock;
import java.util.*;
import java.sql.*;

public class StockDao {
    private Connection conn;

    private static StockDao stockDao;
    private StockDao() {}

    public static StockDao getInstance() {
        if(stockDao == null) stockDao = new StockDao();
        return stockDao;
    }

    public List<Stock> getAllStockList(){
        conn = DBUtil.getConnection();
        //query문 작성할 때 바코드번호로 order by
    }
}
