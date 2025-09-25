package dao;

import domain.Inbound;
import util.DBUtil;

import java.sql.*;
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

    // 요청 승인
    public void approveRequest() {
        System.out.println("InboundDao approveRequest");
    }

    // Inbound 테이블에 정보 등록
    public int addInboundData(int warehouseId, String dueDate) {
        // 프로시저
        String sql = "{call createInRequest(?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setDate(1, Date.valueOf(dueDate));
            call.setInt(2, warehouseId);
            call.registerOutParameter(3, Types.INTEGER);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(3);
            System.out.println("rtn값  " + rtn);
            return rtn;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    // InboundItem 테이블에 정보 등록
    public int addInboundItemData(String productId, int productQuantity) {
        //프로시저
        String sql = "{call createInRequestProduct(?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call = conn.prepareCall(sql)
        ) {
            // 데이터
            call.setString(1, productId);
            call.setInt(2, productQuantity);
            call.registerOutParameter(3, Types.INTEGER);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(3);
            return rtn;

        } catch (SQLException e) {
            return -1;
        }
    }

    // Inbound 테이블 정보 수정
    public int updateInboundData(int requestId, int warehouseId, String dueDate) {
        //프로시저

        String sql = "{call updateInRequest(?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);
            call.setDate(2, Date.valueOf(dueDate));
            call.setInt(3, warehouseId);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(4);
            return rtn;

        } catch (SQLException e) {
            return -1;
        }
    }

    // InboundItem 테이블 정보 수정
    public int updateInboundItemData(int requestId, int itemId, String productId, int productQuantity) {
        //프로시저
        String sql = "{call updateInRequestProduct(?, ?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);
            call.setInt(2, itemId);
            call.setString(3, productId);
            call.setInt(4, productQuantity);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(5);
            return rtn;

        } catch (SQLException e) {
            return -1;
        }
    }

    // Inbound 요청 상태 수정 : 취소됨
    public int cancelInboundData(int requestId) {
        //프로시저
        String sql = "{call updateInRequestStatus(?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(2);
            return rtn;

        } catch (SQLException e) {
            return -1;
        }
    }

    // Inbound 요청 + 물품 정보 전부
    public InboundBillVO readInReqBillData(int requestId) {
        String sql = "{call readInReqBillData(?, ?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);

            // 실행
            call.execute();
            InboundBillVO inboundBill = new InboundBillVO();

            inboundBill.setInRequestId(call.getInt(2));
            inboundBill.setInDate(call.getDate(3));
            inboundBill.setWId(call.getInt(4));
            inboundBill.setUName(call.getString(5));

            // 리턴
            return inboundBill;

        } catch (SQLException e) {
            return null;
        }
    }

    // Inbound 요청 + 물품 정보 전부
    public List<ArrayList> readInItemBillData(int requestId) {
        String sql = "{call readInItemBillData(?, ?, ?, ?, ?)}";
        ArrayList<String> itemBill;
        List<ArrayList> inboundList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);

            // 실행
            call.execute();
            ResultSet rs = call.getResultSet();

            while(rs.next()) {
                itemBill = new ArrayList<>();
                itemBill.add(rs.getString(2));
                itemBill.add(rs.getString(3));
                itemBill.add(rs.getString(4));
                itemBill.add(rs.getString(5));
                inboundList.add(itemBill);
            }

            // 리턴
            return inboundList;

        } catch (SQLException e) {
            return null;
        }
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
    public void getItemsById(int inRequestIdx) {

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