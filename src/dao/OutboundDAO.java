package dao;

public class OutboundDAO implements InOutboundDAO {

    // 싱글턴 패턴 세팅
    private static OutboundDAO outboundDao;
    private OutboundDAO() {}

    public static OutboundDAO getInstance() {
        if (outboundDao == null) {
            outboundDao = new OutboundDAO();
        }
        return outboundDao;
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
