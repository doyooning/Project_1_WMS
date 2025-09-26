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
    public int approveRequest(int requestId) {
        // 프로시저
        String sql = "{call approveOutRequestStatus(?, ?)}";

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

    // Outbound 테이블에 정보 등록
    public int addOutboundData(int uId, int warehouseId, String dueDate) {
        // 프로시저
        String sql = "{call createOutRequest(?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, uId);
            call.setDate(2, Date.valueOf(dueDate));
            call.setInt(3, warehouseId);
            call.registerOutParameter(4, Types.INTEGER);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(4);
            return rtn;

        } catch (
                SQLException e) {
            return -1;
        }
    }

    // OutboundItem 테이블에 정보 등록
    public int addOutboundItemData(int uId, String productId, int productQuantity) {
        //프로시저
        String sql = "{call createOutRequestProduct(?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, uId);
            call.setString(2, productId);
            call.setInt(3, productQuantity);
            call.registerOutParameter(4, Types.INTEGER);

            // 실행
            call.execute();

            // 리턴
            int rtn = call.getInt(4);
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
        String sql = "{call cancelOutRequestStatus(?, ?)}";

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

    // 미승인 요청 불러오기
    @Override
    public List<List<String>> getPendingRequestList() {
        String sql = "{call getPendingOutRequest()}";
        List<List<String>> pRequestList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터

            // 실행
            boolean hasResult = call.execute();
            if (hasResult) {
                try (ResultSet rs = call.getResultSet()) {
                    while (rs.next()) {
                        List<String> pRequest = new ArrayList<>();
                        pRequest.add(rs.getString(1)); // outRequestIdx
                        pRequest.add(rs.getString(2)); // wIdx
                        pRequest.add(rs.getString(3)); // uName
                        pRequest.add(rs.getString(4)); // count(outItemIdx) as itemCount
                        pRequest.add(rs.getString(5)); // outDueDate
                        pRequest.add(rs.getString(6)); // outRequestDate
                        pRequestList.add(pRequest);
                    }
                }
            }

            // 리턴
            return pRequestList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<List<String>> getRequestById(int uId) {

        String sql = "{call getOutRequestByUId(?)}";
        List<List<String>> outRequestList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, uId);

            // 실행
            boolean hasResult = call.execute();
            if (hasResult) {
                try (ResultSet rs = call.getResultSet()) {
                    while (rs.next()) {
                        List<String> request = new ArrayList<>();
                        request.add(rs.getString(1)); // outRequestIdx
                        request.add(rs.getString(2)); // outDueDate
                        request.add(rs.getString(3)); // wIdx
                        request.add(rs.getString(4)); // outRequestDate
                        request.add(rs.getString(5)); // requestStatus
                        request.add(rs.getString(6)); // outboundDate (null -> '-')
                        outRequestList.add(request);
                    }
                }
            }

            // 리턴
            return outRequestList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 요청의 물품 정보를 ID값으로 찾기
    @Override
    public List<List<String>> getItemsById(int uId) {
        String sql = "{call getOutItemsByUId(?)}";
        List<List<String>> outRequestItemList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setInt(1, uId);

            // 실행
            boolean hasResult = call.execute();
            if (hasResult) {
                try (ResultSet rs = call.getResultSet()) {
                    while (rs.next()) {
                        List<String> item = new ArrayList<>();
                        item.add(rs.getString(1)); // outRequestIdx
                        item.add(rs.getString(2)); // outItemIdx
                        item.add(rs.getString(3)); // pIdx
                        item.add(rs.getString(4)); // pName
                        item.add(rs.getString(5)); // pQuantity
                        item.add(rs.getString(6)); // wIdx
                        outRequestItemList.add(item);
                    }
                }
            }

            // 리턴
            return outRequestItemList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
