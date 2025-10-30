package service;

import dao.InboundDAO;

import java.text.SimpleDateFormat;
import java.util.List;

public class InboundService implements InOutboundService{

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 싱글턴 패턴 세팅
    private static InboundService inboundService;
    private InboundService() {}

    public static InboundService getInstance() {
        if (inboundService == null) {
            inboundService = new InboundService();
        }
        return inboundService;
    }

    private InboundDAO inboundDao = InboundDAO.getInboundDao();

    @Override
    public int approveRequest(int requestId) {
        int status = inboundDao.approveRequest(requestId);

        return status;
    }

    public int addRequest(int uId, int warehouseId, String dueDate) {
        int status = inboundDao.addInboundData(uId, warehouseId, dueDate);
        if (status < 0) {
            return -1;
        }
        return status;
    }

    public int addRequest(int uId, String productId, int productQuantity) {
        int status = inboundDao.addInboundItemData(uId, productId, productQuantity);
        if (status < 0) {
            return -1;
        }
        return status;
    }

    public int updateRequest(int requestId, int warehouseId, String dueDate) {
        int status = inboundDao.updateInboundData(requestId, warehouseId, dueDate);

        return status;
    }

    public int updateItem(int requestId, int itemId, String productId, int productQuantity) {
        int status = inboundDao.updateInboundItemData(requestId, itemId, productId, productQuantity);

        return status;
    }

    @Override
    public int cancelRequest(int requestId) {
        int status = inboundDao.cancelInboundData(requestId);

        return status;
    }

    @Override
    public List<List<String>> getBoundInfo(int uIdx) {
        List<List<String>> list = inboundDao.getRequestById(uIdx);

        return list;
    }

    @Override
    public List<List<String>> getBoundItemInfo(int uIdx) {
        List<List<String>> list = inboundDao.getItemsById(uIdx);

        return list;
    }

    @Override
    public List<String> showReqBillData(int requestId) {
        List<String> reqBillData = inboundDao.readInReqBillData(requestId);

        return reqBillData;
    }

    @Override
    public List<List<String>> showItemBillData(int requestId) {
        List<List<String>> itemBillData = inboundDao.readInItemBillData(requestId);

        return itemBillData;
    }

    @Override
    public List<List<String>> getPendingRequestList() {
        List<List<String>> list = inboundDao.getPendingRequestList();

        return list;
    }

    public List<List<String>> getRequestListByPeriod(String start, String end) {
        List<List<String>> list = inboundDao.getRequestListByPeriod(start, end);

        return list;
    }

    public int isAccessibleRequest(int requestId, int uIdx) {
        int status = inboundDao.isAccessibleRequest(requestId, uIdx);

        return status;
    }
}
