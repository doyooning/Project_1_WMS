package service;

import common.Errors;
import dao.WarehouseDao;
import domain.Warehouse;
import exception.DaoException;
import exception.ExceptionManager;

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
        try {
            warehouseDao = WarehouseDao.getInstance();

            List<Warehouse> warehouseList = warehouseDao.getWarehouseList();

            if(warehouseList.size() == 0) {
                throw new ExceptionManager(Errors.NO_WAREHOUSE_IN_LIST.getText());
            }

            return warehouseList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean addWarehouse(Warehouse temp) {
        try {
            warehouseDao = WarehouseDao.getInstance();
            List<Warehouse> list = warehouseDao.getWarehouseList();

            for (Warehouse warehouse : list) {
                if(warehouse.getWUniqueNum().equals(temp.getWUniqueNum())) {
                    throw new ExceptionManager(Errors.WAREHOUSE_ALREADY_EXIST.getText());
                }
            }

            //임대료는 처음에 안넣음
            temp.setWStock(0);
            String[] spiltAddress = temp.getWAddr().split(" ");
            int doIdx = warehouseDao.getDoIdx(spiltAddress[0]);
            if(doIdx == 0){throw new ExceptionManager(Errors.NO_DONAME_IN_DOADDRESS.getText());}

            temp.setDoIdx(doIdx);
            temp.setDoName(spiltAddress[0]);
            int wtIdx = warehouseDao.getWtIdx(temp.getWtName());
            if(wtIdx == 0){throw new ExceptionManager(Errors.NO_WAREHOUSETYPE_IN_WAREHOUSETYPE.getText());}

            temp.setWtIdx(wtIdx);
            boolean result = warehouseDao.addWarehouse(temp);

            return result;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Warehouse getWarehouse(String wUniqueNum) {
        try {
            warehouseDao = WarehouseDao.getInstance();
            int check = warehouseDao.checkWarehouseExist(wUniqueNum); // count 이용해서 해당 창고 존재하는지 확인

            if(check == 0) throw new ExceptionManager(Errors.NO_WAREHOUSE.getText());

            Warehouse warehouse = warehouseDao.getWarehouse(wUniqueNum);
            return warehouse;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Warehouse> getAddressWarehouse(int doIdx){
        try {
            warehouseDao  = WarehouseDao.getInstance();
            List<Warehouse> addrWarehouseList = warehouseDao.getAddressWarehouse(doIdx);

            return addrWarehouseList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Warehouse> getTypeWarehouse(int wtIdx){
        try {
            warehouseDao  = WarehouseDao.getInstance();
            List<Warehouse> typeWarehouseList = warehouseDao.getTypeWarehouse(wtIdx);

            return typeWarehouseList;
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
