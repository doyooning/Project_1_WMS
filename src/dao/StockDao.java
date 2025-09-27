package dao;

import Util.DBUtil;
import domain.CheckLog;
import domain.EntityStatus;
import domain.Stock;
import domain.Warehouse;

import java.util.*;
import java.sql.*;
import java.util.concurrent.Callable;

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
        String sql = "{call getAllStockList()}";
        List<Stock> stockList = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()){
            conn.setAutoCommit(false);
            // select s.pIdx, p.pName, s.wIdx, (s.sWhole-s.sSafe-s.sNotAvail) as sAvail, s.sNotAvail, s.sSafe, pc.cName, s.updatedAt
            while(rs.next()){
                Stock stock = new Stock();
                stock.setPIdx(rs.getString(1));
                stock.setPName(rs.getString(2));
                stock.setWIdx(rs.getInt(3));
                stock.setSAvail(rs.getInt(4));
                stock.setSNotAvail(rs.getInt(5));
                stock.setSSafe(rs.getInt(6));
                stock.setPCategory(rs.getString(7));
                stock.setUpdatedAt(Timestamp.valueOf(rs.getString(8)));

                stockList.add(stock);
            }

            return stockList;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
        }
        return null;    //query문 작성할 때 바코드번호로 order by
    }

    //카테고리는 procedure 작성할 때 해당 카테고리가 존재하는지 확인해줘야함!!!!
    public List<Stock> getPrimaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();

        int result = checkCategoryExist(cName, 2);
        if(result == 0) return null;

        String sql = "{call getPrimaryCategoryStockList(?)}";
        List<Stock> stockList = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(sql)){
            conn.setAutoCommit(false);
            cs.setString(1, cName);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Stock stock = new Stock();
                    stock.setPIdx(rs.getString(1));
                    stock.setPName(rs.getString(2));
                    stock.setWIdx(rs.getInt(3));
                    stock.setSAvail(rs.getInt(4));
                    stock.setSNotAvail(rs.getInt(5));
                    stock.setSSafe(rs.getInt(6));
                    stock.setSCategory(rs.getString(7));
                    stock.setUpdatedAt(Timestamp.valueOf(rs.getString(8)));

                    stockList.add(stock);
                }

                return stockList;
            }catch(SQLException e){
                try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
        }
        return null;
    }

    public List<Stock> getSecondaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();

        int result = checkCategoryExist(cName, 3);
        if(result == 0) return null;

        String sql = "{call getSecondaryCategoryStockList(?)}";
        List<Stock> stockList = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(sql)){
            conn.setAutoCommit(false);
            cs.setString(1, cName);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Stock stock = new Stock();
                    stock.setPIdx(rs.getString(1));
                    stock.setPName(rs.getString(2));
                    stock.setWIdx(rs.getInt(3));
                    stock.setSAvail(rs.getInt(4));
                    stock.setSNotAvail(rs.getInt(5));
                    stock.setSSafe(rs.getInt(6));
                    stock.setTCategory(rs.getString(7));
                    stock.setUpdatedAt(Timestamp.valueOf(rs.getString(8)));

                    stockList.add(stock);
                }

                return stockList;
            }catch(SQLException e){
                try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
        }
        return null;
    }

    public List<Stock> getTertiaryCategoryStockList(String cName){
        conn = DBUtil.getConnection();

        int result = checkCategoryExist(cName, 4);
        if(result == 0) return null;

        String sql = "{call getTertiaryCategoryStockList(?)}";
        List<Stock> stockList = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(sql)){
            conn.setAutoCommit(false);
            cs.setString(1, cName);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Stock stock = new Stock();
                    stock.setPIdx(rs.getString(1));
                    stock.setPName(rs.getString(2));
                    stock.setWIdx(rs.getInt(3));
                    stock.setSAvail(rs.getInt(4));
                    stock.setSNotAvail(rs.getInt(5));
                    stock.setSSafe(rs.getInt(6));
                    stock.setTCategory(rs.getString(7));
                    stock.setUpdatedAt(Timestamp.valueOf(rs.getString(8)));

                    stockList.add(stock);
                }

                return stockList;
            }catch(SQLException e){
                try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
        }
        return null;
    }

    public List<Stock> getProductStockList(String pIdx){
        conn = DBUtil.getConnection();

        String sql = "{call getProductStockList(?)}";
        List<Stock> stockList = new ArrayList<>();

        try(CallableStatement cs = conn.prepareCall(sql)){
            conn.setAutoCommit(false);
            cs.setString(1, pIdx);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Stock stock = new Stock();
                    stock.setPIdx(rs.getString(1));
                    stock.setPName(rs.getString(2));
                    stock.setWIdx(rs.getInt(3));
                    stock.setSAvail(rs.getInt(4));
                    stock.setSNotAvail(rs.getInt(5));
                    stock.setSSafe(rs.getInt(6));
                    stock.setTCategory(rs.getString(7));
                    stock.setUpdatedAt(Timestamp.valueOf(rs.getString(8)));

                    stockList.add(stock);
                }

                return stockList;
                //만약 stockList.size()가 0이면 해당 바코드번호가 없음
            }catch(SQLException e){
                try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e.printStackTrace();}
        }
        return null;
    }

    private int checkCategoryExist(String cName, int num){
        conn = DBUtil.getConnection();
        String sql = "{call checkCategoryExist(?,?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, cName);
            cs.setInt(2, num);

            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(3);

            return rtn;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int addCheckLog(int wIdx){
        conn = DBUtil.getConnection();

        String sql = "{call addCheckLog(?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, wIdx);

            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(2);

            return rtn;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public CheckLog getNewCheckLog(){
        conn = DBUtil.getConnection();

        //select cl.clIdx, w.wUniqueNum, w.wName, cl.clCorrect, cl.createdAt
        String sql = "{call getNewCheckLog()}";

        CheckLog checkLog = new CheckLog();
        try(CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()){
            conn.setAutoCommit(false);
            if(rs.next()){
                checkLog.setClIdx(rs.getInt(1));
                checkLog.setWUniqueNum(rs.getString(2));
                checkLog.setWName(rs.getString(3));
                checkLog.setClCorrect(EntityStatus.valueOf(rs.getString(4)));
                checkLog.setCreatedAt(Timestamp.valueOf(rs.getString(5)));
            }

            return checkLog;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public int removeCheckLog(int clIdx){
        conn = DBUtil.getConnection();

        int result = checkCheckLogExist(clIdx);
        if(result == 0) return -1; //존재하지 않음

        String sql = "{call removeCheckLog(?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, clIdx);

            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(2);

            return rtn;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return -1; //에러
    }

    private int checkCheckLogExist(int clIdx){
        conn = DBUtil.getConnection();

        String sql = "{call checkCheckLogExist(?,?)}";
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, clIdx);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(2);
            return rtn;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return 0;
    }

    public List<CheckLog> getCheckLogList(int check, int wIdx){
        conn = DBUtil.getConnection();

        List<CheckLog> checkLogList = new ArrayList<>();
        String sql = "{call getCheckLogList(?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, check);
            cs.setInt(2, wIdx);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    CheckLog checkLog = new CheckLog();
                    checkLog.setClIdx(rs.getInt(1));
                    checkLog.setWUniqueNum(rs.getString(2));
                    checkLog.setWsName(rs.getString(3));
                    checkLog.setClCorrect(EntityStatus.valueOf(rs.getString(4)));
                    checkLog.setCreatedAt(Timestamp.valueOf(rs.getString(5)));

                    checkLogList.add(checkLog);
                }
                return checkLogList;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;//에러발생
    }

    public int checkWarehouseIsStorage(String wUniqueNum){
        conn = DBUtil.getConnection();

        String sql = "{call checkWarehouseISStorage(?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, wUniqueNum);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(2);
            return rtn; //0이면 마이크로 1이면 보관형
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return -1; //에러발생
    }

    public List<CheckLog> getSectionCheckLoglist(String wUniqueNum, String wsName){
        conn = DBUtil.getConnection();

        String sql = "{call getSectionCheckLoglist(?,?)}";
        List<CheckLog> checkLogList = new ArrayList<>();
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, wUniqueNum);
            cs.setString(2, wsName);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    CheckLog checkLog = new CheckLog();
                    checkLog.setClIdx(rs.getInt(1));
                    checkLog.setWUniqueNum(rs.getString(2));
                    checkLog.setWsName(rs.getString(3));
                    checkLog.setClCorrect(EntityStatus.valueOf(rs.getString(4)));
                    checkLog.setCreatedAt(Timestamp.valueOf(rs.getString(5)));

                    checkLogList.add(checkLog);
                }
                return checkLogList;
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public List<CheckLog> getWarehouseCheckLogList(String wUniqueNum){
        conn = DBUtil.getConnection();

        String sql = "{call getWarehouseCheckLoglist(?)}";
        List<CheckLog> checkLogList = new ArrayList<>();
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, wUniqueNum);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    CheckLog checkLog = new CheckLog();
                    checkLog.setClIdx(rs.getInt(1));
                    checkLog.setWUniqueNum(rs.getString(2));
                    checkLog.setWsName(rs.getString(3));
                    checkLog.setClCorrect(EntityStatus.valueOf(rs.getString(4)));
                    checkLog.setCreatedAt(Timestamp.valueOf(rs.getString(5)));

                    checkLogList.add(checkLog);
                }
                return checkLogList;
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public Warehouse getWarehouseInfo(int wIdx){
        conn = DBUtil.getConnection();

        String sql = "{call getWarehouseInfo(?)}";
        Warehouse warehouse = new Warehouse();
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, wIdx);

            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    warehouse.setWtIdx(rs.getInt(1));
                }

                return warehouse;
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public boolean updateCheckLog(int clIdx){
        conn = DBUtil.getConnection();

        String sql = "{call updateCheckLog(?)";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, clIdx);

            cs.execute();

            return true;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return false;
    }

    public boolean checkUpdateCondition(int clIdx, int wIdx){
        conn = DBUtil.getConnection();

        String sql = "{call checkUpdateCondition(?,?,?)}";
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, clIdx);
            cs.setInt(2, wIdx);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();
            int rtn = cs.getInt(3);
            if(rtn==1) return true;
            return false;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return false;
    }

    public boolean checkWsNameExist(String wsName){
        conn = DBUtil.getConnection();

        String sql = "{call checkWsNameExist(?,?)}";
        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, wsName);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            int rtn = cs.getInt(2);
            if(rtn==1) return true;
            return false;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return false;
    }

    public boolean checkWarehouseAdminCondition(int clIdx, int wIdx){
        conn = DBUtil.getConnection();

        String sql = "{call checkWarehouseAdminCondition(?,?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, clIdx);
            cs.setInt(2, wIdx);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            int rtn = cs.getInt(3);
            if(rtn==1) return true;
            return false;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return false;
    }
}
