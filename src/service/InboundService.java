package service;

import dao.InboundDAO;

public class InboundService implements InOutboundService{

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
    public int approveRequest() {
        inboundDao.approveRequest();
        return 0;
    }

    public int addRequest(int warehouseId, String dueDate) {
        int status = inboundDao.addInboundData(warehouseId, dueDate);
        if (status <= 0) {
            return -1;
        }
        return status;
    }

    public int addRequest(String productId, int productQuantity) {
        int status = inboundDao.addInboundItemData(productId, productQuantity);
        if (status <= 0) {
            return -1;
        }
        return status;
    }

    public int updateRequest(int requestId, int warehouseId, String dueDate) {
        int status = inboundDao.updateInboundData(requestId, warehouseId, dueDate);
        if (status <= 0) {
            return -1;
        }
        return 0;
    }

    public int updateItem(int requestId, int itemId, String productId, int productQuantity) {
        int status = inboundDao.updateInboundItemData(requestId, itemId, productId, productQuantity);
        if (status <= 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public int cancelRequest() {
        return 0;
    }

    @Override
    public void getBoundInfo() {

    }

    public void showRequestInfo() {

    }
}
