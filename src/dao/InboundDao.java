package dao;

public class InboundDao {

    // 싱글턴 패턴 세팅
    private static InboundDao inboundDao;
    private InboundDao() {}

    public static InboundDao getInboundDao() {
        if (inboundDao == null) {
            inboundDao = new InboundDao();
        }
        return inboundDao;
    }

}
