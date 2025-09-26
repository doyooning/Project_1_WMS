package dao;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // statics

    // 요청 승인
    public void approveRequest() {
        System.out.println("OutboundDao approveRequest");
    }

    // Outbound 테이블에 정보 등록
    public int addOutboundData(int warehouseId, String dueDate) {
        // 프로시저
        String sql = "{call createOutRequest(?, ?, ?)}";

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
            return rtn;

        } catch (
                SQLException e) {
            return -1;
        }
    }

    // OutboundItem 테이블에 정보 등록
    public int addOutboundItemData(String productId, int productQuantity) {
        //프로시저
        String sql = "{call createOutRequestProduct(?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
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

    // Outbound 테이블 정보 수정
    public int updateOutboundData(int requestId, int warehouseId, String dueDate) {
        //프로시저
        String sql = "{call updateOutRequest(?, ?, ?, ?)}";

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

    // OutboundItem 테이블 정보 수정
    public int updateOutboundItemData(int requestId, int itemId, String productId, int productQuantity) {
        //프로시저
        String sql = "{call updateOutRequestProduct(?, ?, ?, ?, ?)}";

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

    // Outbound 요청 상태 수정 : 취소됨
    public int cancelOutboundData(int requestId) {
        //프로시저
        String sql = "{call updateOutRequestStatus(?, ?)}";

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

    // Outbound 요청 정보 불러오기
    public OutboundBillVO readOutReqBillData(int requestId) {
        String sql = "{call readOutReqBillData(?, ?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);
            call.registerOutParameter(2, Types.INTEGER);
            call.registerOutParameter(3, Types.TIMESTAMP);
            call.registerOutParameter(4, Types.INTEGER);
            call.registerOutParameter(5, Types.VARCHAR);

            // 실행
            call.execute();
            OutboundBillVO outboundBill = new OutboundBillVO();

            outboundBill.setOutRequestId(call.getInt(2));
            outboundBill.setOutDate(call.getTimestamp(3));
            outboundBill.setWId(call.getInt(4));
            outboundBill.setUName(call.getString(5));

            // 리턴
            return outboundBill;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Outbound 물품 정보 불러오기
    public List<List<String>> readOutItemBillData(int requestId) {
        String sql = "{call readOutItemBillData(?)}";
        List<List<String>> outboundList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, requestId);

            // 실행
            boolean hasResult = call.execute();
            if (hasResult) {
                try (ResultSet rs = call.getResultSet()) {
                    while (rs.next()) {
                        List<String> itemBill = new ArrayList<>();
                        itemBill.add(rs.getString(1)); // outRequestIdx
                        itemBill.add(rs.getString(2)); // outboundDate
                        itemBill.add(rs.getString(3)); // wIdx
                        itemBill.add(rs.getString(4)); // uName
                        outboundList.add(itemBill);
                    }
                }
            }

            // 리턴
            return outboundList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void getRequestList() {

    }

    @Override
    public void getRequestById(int inRequestIdx) {

    }

    @Override
    public void getItemsById(int inRequestIdx) {

    }
}
