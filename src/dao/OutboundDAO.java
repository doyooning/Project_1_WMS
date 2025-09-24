package dao;

import util.DBUtil;

import java.sql.*;

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
