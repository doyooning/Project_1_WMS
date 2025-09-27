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

        List<Warehouse> warehouseList = warehouseDao.getWarehouseList();
        if(warehouseList == null) {System.out.println("전체 현황 리스트를 불러오는 중 오류가 발생했습니다."); }
        else if(warehouseList.size() == 0) {System.out.println("창고 리스트에 창고가 존재하지 않습니다."); }

        return warehouseList;
    }

    public boolean addWarehouse(Warehouse temp) {
        List<Warehouse> list = warehouseDao.getWarehouseList();

        for (Warehouse warehouse : list) {
            if(warehouse.getWUniqueNum().equals(temp.getWUniqueNum())) {
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

    public Warehouse getWarehouse(String wUniqueNum) {
        int check = warehouseDao.checkWarehouseExist(wUniqueNum); // count 이용해서 해당 창고 존재하는지 확인
        if(check == -1){ System.out.print("창고 존재 확인 중 에러가 발생했습니다. "); return null;}
        else if(check == 0){
            System.out.print("해당 창고는 존재하지 않습니다. "); return null;}

        Warehouse warehouse = warehouseDao.getWarehouse(wUniqueNum);
        return warehouse;
    }

    public List<Warehouse> getAddressWarehouse(int doIdx){
        List<Warehouse> addrWarehouseList = warehouseDao.getAddressWarehouse(doIdx);

        if(addrWarehouseList == null) return null;

        return addrWarehouseList;
    }

    public List<Warehouse> getTypeWarehouse(int wtIdx){
        List<Warehouse> typeWarehouseList = warehouseDao.getTypeWarehouse(wtIdx);

        if(typeWarehouseList == null) return null;

        return typeWarehouseList;
    }
}
