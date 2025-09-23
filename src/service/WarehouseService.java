package service;

import dao.WarehouseDao;
import domain.Warehouse;

import java.util.*;
import java.io.*;

public class WarehouseService {
    private WarehouseDao warehouseDao;

    private static WarehouseService warehouseService;

    private WarehouseService() {}

    public static WarehouseService getInstance() {
        if(warehouseService == null) {warehouseService = new WarehouseService();}
        return warehouseService;
    }

    public List<Warehouse> getWarehouseList() {
        warehouseDao = WarehouseDao.getInstance();
        return warehouseDao.getWarehouseList();
    }
}
