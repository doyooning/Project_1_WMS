package dao;

public class InboundDAO implements InOutboundDAO {

    // 싱글턴 패턴 세팅
    private static InboundDAO inboundDao;
    private InboundDAO() {}

    public static InboundDAO getInboundDao() {
        if (inboundDao == null) {
            inboundDao = new InboundDAO();
        }
        return inboundDao;
    }

    public void approveRequest() {
        System.out.println("InboundDao approveRequest");
    }

    @Override
    public void getRequestList() {

    }

    @Override
    public void getRequestById(int inRequestIdx) {

    }

    @Override
    public void getRequestItemInfo(int inRequestIdx) {

    }
}
