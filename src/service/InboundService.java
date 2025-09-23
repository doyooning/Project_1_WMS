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

    @Override
    public int addRequest() {
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

    public void showRequestInfo() {

    }
}
