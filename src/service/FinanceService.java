package service;

import domain.Warehouse;

import java.util.List;
import java.util.Map;

public interface FinanceService {
    Map<String, Object> getFinanceList(int wIdx, String type, String date);

    List<Warehouse> getWarehouseList();
}
