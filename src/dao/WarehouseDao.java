package dao;

import util.DBUtil;
import common.Errors;
import domain.Warehouse;
import exception.DaoException;

import java.util.*;
import java.sql.*;

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
                warehouse.setCreatedAt(rs.getTimestamp(5));

                warehouseList.add(warehouse);
            }

            return warehouseList;
        }catch(SQLException e){
            throw new DaoException("[DB] 전체 현황 리스트를 불러오는 중 오류가 발생했습니다.", e);
        }
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
            return true;
        }catch(SQLException e){
            throw new DaoException(Errors.DB_ADDWAREHOUSE_ERROR.getText(), e);
        }
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
            throw new DaoException(Errors.DB_DOIDX_ERROR.getText(), e);
        }
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
            return 0; //해당 wtName이 존재하지 않음
        }catch(SQLException e){
            throw new DaoException(Errors.DB_WAREHOUSETYPEIDX_ERROR.getText(), e);
        }
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
            throw new DaoException(Errors.DB_WAREHOUSESEARCH_WIDX_ERROR.getText(), e);
        }
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
            throw new DaoException(Errors.DB_WAREHOUSE_EXIST_ERROR.getText(), e);
        }
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
            throw new DaoException(Errors.DB_WAREHOUSESEARCH_ADDR_ERROR.getText(), e);
        }
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
            throw new DaoException(Errors.DB_WAREHOUSESEARCH_TYPE_ERROR.getText(), e);
        }
    }

    public List<Warehouse> getNotAssignedWarehouses(){
        conn = DBUtil.getConnection();

        warehouseList = new ArrayList<>();
        String sql = "{call getNotAssignedWarehouses()}";

        try(CallableStatement cs = conn.prepareCall(sql); ResultSet rs = cs.executeQuery()){
            while(rs.next()){
                Warehouse warehouse = new Warehouse();
                warehouse.setWIdx(rs.getInt(1));
                warehouse.setWUniqueNum(rs.getString(2));
                warehouse.setWName(rs.getString(3));

                warehouseList.add(warehouse);
            }
            return warehouseList;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
