package dao;

import domain.Inbound;

import java.util.ArrayList;
import java.util.List;

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

    // statics
    // 입고요청을 담을 리스트
    private static List<Inbound> inboundList = new ArrayList<>();


    // 요청 승인
    public void approveRequest() {
        System.out.println("InboundDao approveRequest");
    }

    // Inbound 테이블에 정보 등록
    public int addInboundData(int warehouseId, String dueDate) {
        // 프로시저
        System.out.println(warehouseId + " " + dueDate);
        return 1;
    }

    // InboundItem 테이블에 정보 등록
    public int addInboundItemData(String productId, int productQuantity) {
        //프로시저
        System.out.println(productId + " " + productQuantity);
        return 1;
    }

    // 요청 불러오기
    @Override
    public void getRequestList() {

    }

    // 요청을 ID값으로 찾기
    @Override
    public void getRequestById(int inRequestIdx) {

    }

    // 요청의 물품 정보를 ID값으로 찾기
    @Override
    public void getRequestItemInfo(int inRequestIdx) {

    }

}

//private void dbLoad() {
//    String sql = "select * from Inbound";
//
//    try (Connection conn = DBUtil.getConnection();
//         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//         ResultSet rs = pstmt.executeQuery();
//    ) {
//        while (rs.next()) {
//            Inbound inbound = new Inbound();
//            inbound.setInRequestIdx(rs.getInt(1));
//            inbound.setInDueDate(rs.getTimestamp(2));
//            inbound.setRequestStatus(EntityStatus.valueOf(rs.getString(3)));
//            inbound.setInRequestDate(rs.getTimestamp(4));
//            inbound.setUIdx(rs.getInt(5));
//            inbound.setInboundDate(rs.getTimestamp(6));
//            inbound.setWIdx(rs.getInt(7));
//            inboundList.add(inbound);
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}