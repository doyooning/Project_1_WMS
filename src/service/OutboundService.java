package service;

import dao.OutboundDAO;

import java.text.SimpleDateFormat;
import java.util.List;

public class OutboundService implements InOutboundService {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // 싱글턴 패턴 세팅
    private static OutboundService outboundService;
    private OutboundService() {}

    public static OutboundService getInstance() {
        if (outboundService == null) {
            outboundService = new OutboundService();
        }
        return outboundService;
    }

    private OutboundDAO outboundDao = OutboundDAO.getInstance();

    @Override
    public int approveRequest(int requestId) {
        int status = outboundDao.approveRequest(requestId);

        return status;
    }

    public int addRequest(int id, int warehouseId, String dueDate) {
        int status = outboundDao.addOutboundData(id, warehouseId, dueDate);
        if (status < 0) {
            return -1;
        }
        return status;
    }

    public int addRequest(int id, String productId, int productQuantity) {
        int status = outboundDao.addOutboundItemData(id, productId, productQuantity);
        if (status < 0) {
            return -1;
        }
        return status;
    }

    public int updateRequest(int requestId, int warehouseId, String dueDate) {
        int status = outboundDao.updateOutboundData(requestId, warehouseId, dueDate);

        return status;
    }

    public int updateItem(int requestId, int itemId, String productId, int productQuantity) {
        int status = outboundDao.updateOutboundItemData(requestId, itemId, productId, productQuantity);

        return status;
    }

    @Override
    public int cancelRequest(int requestId) {
        int status = outboundDao.cancelOutboundData(requestId);

        return status;
    }

    @Override
    public List<String> showReqBillData(int requestId) {
        List<String> reqBillData = outboundDao.readOutReqBillData(requestId);

        return reqBillData;
    }

    @Override
    public List<List<String>> showItemBillData(int requestId) {
        List<List<String>> list = outboundDao.readOutItemBillData(requestId);

        return list;
    }


    @Override
    public List<List<String>> getBoundInfo(int uId) {
        List<List<String>> list = outboundDao.getRequestById(uId);

        return list;
    }

    @Override
    public List<List<String>> getBoundItemInfo(int uId) {
        List<List<String>> list = outboundDao.getItemsById(uId);

        return list;
    }

    @Override
    public List<List<String>> getPendingRequestList() {
        List<List<String>> list = outboundDao.getPendingRequestList();

        return list;
    }

    public List<List<String>> getRequestListByPeriod(String start, String end) {
        List<List<String>> list = outboundDao.getRequestListByPeriod(start, end);

        return list;
    }

    public int isAccessibleRequest(int requestId, int uIdx) {
        int status = outboundDao.isAccessibleRequest(requestId, uIdx);

        return status;
    }
}
