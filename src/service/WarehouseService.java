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

    public boolean addWarehouse(Warehouse temp) {
        List<Warehouse> list = warehouseDao.getWarehouseList();

        for (Warehouse warehouse : list) {
            if(warehouse.getWIdx().equals(temp.getWIdx())) {
                System.out.println("이미 존재하는 창고입니다. 창고 등록에 실패하였습니다.");
                return false;
            }
        }

        //임대료는 처음에 안넣음
        temp.setWStock(0);
        String[] spiltAddress = temp.getWAddr().split(" ");
        int doIdx = warehouseDao.getDoIdx(spiltAddress[0]);
        temp.setDoIdx(doIdx);
        temp.setWAddr(spiltAddress[0]);
        int wtIdx = warehouseDao.getWtIdx(temp.getWtName());
        temp.setWtIdx(wtIdx);
        boolean result = warehouseDao.addWarehouse(temp);

        if(result) return true;
        return false;
    }
}
