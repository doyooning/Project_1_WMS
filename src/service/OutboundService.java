package service;

import dao.OutboundDAO;

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
    public int approveRequest() {
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

    @Override
    public int updateRequest(int num, String str) {
        return 0;
    }

    @Override
    public int cancelRequest() {
        return 0;
    }

    @Override
    public void getBoundInfo() {

    }
}
