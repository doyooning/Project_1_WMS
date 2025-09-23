package dao;

import Util.DBUtil;
import domain.Warehouse;
import java.util.*;
import java.sql.*;

public class WarehouseDao {
    private static WarehouseDao warehouseDao;
    private Connection conn;

    private WarehouseDao(){}
    public static WarehouseDao getInstance() {
        if (warehouseDao == null) {warehouseDao = new WarehouseDao();}
        return warehouseDao;
    }

    public List<Warehouse> getWarehouseList() {
        List<Warehouse> warehouseList = new ArrayList<>();
        conn = DBUtil.getConnection();
        //callable statement

        //기능 구현
        return warehouseList;
    }
}
