package dao;

import Util.DBUtil;
import domain.Warehouse;
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
        //callable statement

        //기능 구현
        return warehouseList;
    }

    public boolean addWarehouse(Warehouse warehouse) {
        //임대료는 처음에 null로 넣기
    }

    public int getDoIdx(String doName){
        conn = DBUtil.getConnection();

    }

    public int getWtIdx(String wtName){

    }
}
