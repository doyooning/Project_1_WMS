package service;

import domain.Warehouse;
import java.util.List;

public interface WarehouseService {

    // 창고 목록 조회
    List<Warehouse> getWarehouseList();

    // 창고 추가
    boolean addWarehouse(Warehouse temp);

    // 창고 단건 조회
    Warehouse getWarehouse(String wUniqueNum);

    // 특정 도 지역(doIdx)에 속한 창고 조회
    List<Warehouse> getAddressWarehouse(int doIdx);

    // 특정 유형(wtIdx)에 속한 창고 조회
    List<Warehouse> getTypeWarehouse(int wtIdx);

    // 아직 배정되지 않은 창고 목록 조회
    List<Warehouse> getNotAssignedWarehouses();
}