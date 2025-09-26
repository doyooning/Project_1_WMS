package dao;

import Util.DBUtil;
import domain.CheckLog;
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

    //카테고리는 procedure 작성할 때 해당 카테고리가 존재하는지 확인해줘야함!!!!
    public List<Stock> getPrimaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();
    }
    public List<Stock> getSecondaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();
    }
    public List<Stock> getTertiaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();
    }

    public List<Stock> getProductStockList(String pIdx){
        conn = DBUtil.getConnection();
    }

    public int checkWarehouseAdmin(String wUniqueNum){
        conn = DBUtil.getConnection();
        // 이거 구현 의논해야함
    }

    public int addCheckLog(){
        conn = DBUtil.getConnection();

        //등록 성공시 1, 실패시 0 -> count로 비교
    }

    public CheckLog getNewCheckLog(){
        conn = DBUtil.getConnection();
    }

    public int removeCheckLog(int clIdx){
        conn = DBUtil.getConnection();

        int result = checkCheckLogExist(clIdx);
        if(result == 0) return -1;
    }

    private int checkCheckLogExist(int clIdx){
        return 0;
    }
}
