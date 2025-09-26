package service;

import dao.OutboundBillVO;
import dao.OutboundDAO;

import java.util.List;

public class OutboundService implements InOutboundService {

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
        return 0;
    }

    public int addRequest(int warehouseId, String dueDate) {
        int status = outboundDao.addOutboundData(warehouseId, dueDate);
        if (status <= 0) {
            return -1;
        }
        return status;
    }

    public int addRequest(String productId, int productQuantity) {
        int status = outboundDao.addOutboundItemData(productId, productQuantity);
        if (status <= 0) {
            return -1;
        }
        return status;
    }

    public int updateRequest(int requestId, int warehouseId, String dueDate) {
        int status = outboundDao.updateOutboundData(requestId, warehouseId, dueDate);
        if (status <= 0) {
            return -1;
        }
        return 0;
    }

    public int updateItem(int requestId, int itemId, String productId, int productQuantity) {
        int status = outboundDao.updateOutboundItemData(requestId, itemId, productId, productQuantity);
        if (status <= 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public int cancelRequest(int requestId) {
        int status = outboundDao.cancelOutboundData(requestId);
        if (status <= 0) {
            return -1;
        }
        return 0;
    }

    public OutboundBillVO showReqBillData(int requestId) {
        OutboundBillVO vo = outboundDao.readOutReqBillData(requestId);

        return vo;
    }

    public List<List<String>> showItemBillData(int requestId) {
        List<List<String>> list = outboundDao.readOutItemBillData(requestId);

        return list;
    }


    @Override
    public void getBoundInfo() {

    }
}
