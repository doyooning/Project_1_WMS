package service;

import java.util.Map;

public interface FinanceService {
    Map<String, Object> getFinanceList(int wIdx, String type, String date);
}
