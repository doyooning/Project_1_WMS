package dao;

public class OutboundDao {

    // 싱글턴 패턴 세팅
    private static OutboundDao outboundDao;
    private OutboundDao() {}

    public static OutboundDao getInstance() {
        if (outboundDao == null) {
            outboundDao = new OutboundDao();
        }
        return outboundDao;
    }


}
