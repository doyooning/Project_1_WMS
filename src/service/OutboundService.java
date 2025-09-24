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
        return 0;
    }

    public int addRequest(String productId, int productQuantity) {
        return 0;
    }

    @Override
    public int updateRequest() {
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
