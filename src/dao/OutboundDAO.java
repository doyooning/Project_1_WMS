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


    // Outbound 테이블에 정보 등록
    public int addOutboundData(int warehouseId, String dueDate) {
        // 프로시저
        System.out.println(warehouseId + " " + dueDate);
        return 1;
    }

    // OutboundItem 테이블에 정보 등록
    public int addOutboundItemData(String productId, int productQuantity) {
        //프로시저
        System.out.println(productId + " " + productQuantity);
        return 1;
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
