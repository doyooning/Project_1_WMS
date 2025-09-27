package dao;

import Util.DBUtil;
import domain.Warehouse;
import java.util.*;
import java.sql.*;
import java.util.concurrent.Callable;

public class WarehouseDao {
    private static WarehouseDao warehouseDao;
    private List<Warehouse> warehouseList;
    private Connection conn;

    private WarehouseDao(){}
    public static WarehouseDao getInstance() {
        if (warehouseDao == null) {warehouseDao = new WarehouseDao();}
        return warehouseDao;
    }

    public List<Warehouse> getWarehouseList() {
        warehouseList = new ArrayList<>();
        conn = DBUtil.getConnection();
        //"창고번호", "창고이름", "창고주소", "창고타입", "등록일"
        String sql = "{call getWarehouseList()}";

        try(CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()){
            while(rs.next()){
                Warehouse warehouse = new Warehouse();
                warehouse.setWUniqueNum(rs.getString(1));
                warehouse.setWName(rs.getString(2));
                warehouse.setDoName(rs.getString(3));
                warehouse.setWtName(rs.getString(4));
                warehouse.setCreatedAt(Timestamp.valueOf(rs.getString(5)));

                warehouseList.add(warehouse);
            }

            return warehouseList; //warehouseList가 0이면 창고없음
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;//에러
    }

    public boolean addWarehouse(Warehouse warehouse) {
        //임대료는 처음에 0으로 넣기
        conn = DBUtil.getConnection();
        String sql = "{call addWarehouse(?,?,?,?,?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, warehouse.getWName());
            cs.setString(2, warehouse.getWAddr());
            cs.setInt(3, warehouse.getDoIdx());
            cs.setInt(4, warehouse.getWMaxAmount());
            cs.setInt(5, warehouse.getWtIdx());
            cs.setString(6, warehouse.getWUniqueNum());

            boolean result = cs.execute();
            return result;
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return false;
    }

    public int getDoIdx(String doName){
        conn = DBUtil.getConnection();
        String sql = "{call getDoIdx(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, doName);

            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
            return 0; //해당 doName이 존재하지 않음
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return -1; //에러발생
    }

    public int getWtIdx(String wtName){
        conn = DBUtil.getConnection();
        String sql = "{call getWtIdx(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1,wtName);

            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
            return 0; //해당 doName이 존재하지 않음
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return -1; //에러발생
    }

    public Warehouse getWarehouse(String wUniqueNum) {
        Warehouse warehouse = new Warehouse();
        conn = DBUtil.getConnection();
        //w.wUniqueNum, w.wName, w.wRent, w.wStock, w.wMaxAmount, wt.wtName, d.doName
        String sql = "{call getWarehouse(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1, wUniqueNum);

            try(ResultSet rs = cs.executeQuery()){
                if(rs.next()){
                    warehouse.setWUniqueNum(rs.getString(1));
                    warehouse.setWName(rs.getString(2));
                    warehouse.setWRent(rs.getInt(3));
                    warehouse.setWStock(rs.getInt(4));
                    warehouse.setWMaxAmount(rs.getInt(5));
                    warehouse.setWtName(rs.getString(6));
                    warehouse.setDoName(rs.getString(7));
                }
                return warehouse;
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public int checkWarehouseExist(String wUniqueNum){
        conn = DBUtil.getConnection();
        String sql = "{call checkWarehouseExist(?,?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setString(1,wUniqueNum);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            return cs.getInt(2); //있으면 1, 아니면 0
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return -1; //에러발생
    }

    public List<Warehouse> getAddressWarehouse(int doIdx){
        //wUniqueNum, wName, wRent, wStock, wAddr
        conn = DBUtil.getConnection();
        warehouseList = new ArrayList<>();

        String sql = "{call getAddressWarehouse(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, doIdx);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Warehouse warehouse = new Warehouse();
                    warehouse.setWUniqueNum(rs.getString(1));
                    warehouse.setWName(rs.getString(2));
                    warehouse.setWRent(rs.getInt(3));
                    warehouse.setWStock(rs.getInt(4));
                    warehouse.setWAddr(rs.getString(5));

                    warehouseList.add(warehouse);
                }
                return warehouseList;
            }
        }catch(SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }

    public List<Warehouse> getTypeWarehouse(int wtIdx){
        conn = DBUtil.getConnection();
        // w.wUniqueNum, w.wName, w.wRent, w.wStock, d.doName

        warehouseList = new ArrayList<>();
        String sql = "{call getTypeWarehouse(?)}";

        try(CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1,wtIdx);

            try(ResultSet rs = cs.executeQuery()){
                while(rs.next()){
                    Warehouse warehouse = new Warehouse();
                    warehouse.setWUniqueNum(rs.getString(1));
                    warehouse.setWName(rs.getString(2));
                    warehouse.setWRent(rs.getInt(3));
                    warehouse.setWStock(rs.getInt(4));
                    warehouse.setDoName(rs.getString(5));

                    warehouseList.add(warehouse);
                }
                return warehouseList;
            }
        }catch (SQLException e){
            try{conn.rollback();}catch(SQLException e1){e1.printStackTrace();}
        }
        return null;
    }
}
