package dao;

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
    public int approveRequest(int requestId) {
        // 프로시저
        String sql = "{call approveInRequestStatus(?, ?)}";

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

    // Inbound 테이블에 정보 등록
    public int addInboundData(int uId, int warehouseId, String dueDate) {
        // 프로시저
        String sql = "{call createInRequest(?, ?, ?, ?)}";

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

        } catch (SQLException e) {
            return -1;
        }

    }

    // InboundItem 테이블에 정보 등록
    public int addInboundItemData(int uId, String productId, int productQuantity) {
        //프로시저
        String sql = "{call createInRequestProduct(?, ?, ?, ?)}";

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call = conn.prepareCall(sql)
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
        String sql = "{call cancelInRequestStatus(?, ?)}";

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

    // Inbound 요청 정보 불러오기
    public InboundBillVO readInReqBillData(int requestId) {
        String sql = "{call readInReqBillData(?, ?, ?, ?, ?)}";

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
            InboundBillVO inboundBill = new InboundBillVO();

            inboundBill.setInRequestId(call.getInt(2));
            inboundBill.setInDate(call.getTimestamp(3));
            inboundBill.setWId(call.getInt(4));
            inboundBill.setUName(call.getString(5));

            // 리턴
            return inboundBill;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inbound 물품 정보 불러오기
    public List<List<String>> readInItemBillData(int requestId) {
        String sql = "{call readInItemBillData(?)}";
        List<List<String>> inboundList = new ArrayList<>();

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
                        itemBill.add(rs.getString(1)); // inRequestIdx
                        itemBill.add(rs.getString(2)); // inboundDate
                        itemBill.add(rs.getString(3)); // wIdx
                        itemBill.add(rs.getString(4)); // uName
                        inboundList.add(itemBill);
                    }
                }
            }

            // 리턴
            return inboundList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 미승인 요청 불러오기
    @Override
    public List<List<String>> getPendingRequestList() {
        String sql = "{call getPendingInRequest()}";
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
                        pRequest.add(rs.getString(1)); // inRequestIdx
                        pRequest.add(rs.getString(2)); // wIdx
                        pRequest.add(rs.getString(3)); // uName
                        pRequest.add(rs.getString(4)); // count(inItemIdx) as itemCount
                        pRequest.add(rs.getString(5)); // inDueDate
                        pRequest.add(rs.getString(6)); // inRequestDate
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

    // 요청을 유저ID로 찾기
    @Override
    public List<List<String>> getRequestById(int uId) {

        String sql = "{call getInRequestByUId(?)}";
        List<List<String>> inRequestList = new ArrayList<>();

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
                        request.add(rs.getString(1)); // inRequestIdx
                        request.add(rs.getString(2)); // inDueDate
                        request.add(rs.getString(3)); // wIdx
                        request.add(rs.getString(4)); // inRequestDate
                        request.add(rs.getString(5)); // requestStatus
                        request.add(rs.getString(6)); // inboundDate (null -> '-')
                        inRequestList.add(request);
                    }
                }
            }

            // 리턴
            return inRequestList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 요청의 물품 정보를 ID값으로 찾기
    @Override
    public List<List<String>> getItemsById(int uId) {
        String sql = "{call getInItemsByUId(?)}";
        List<List<String>> inRequestItemList = new ArrayList<>();

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
                        item.add(rs.getString(1)); // inRequestIdx
                        item.add(rs.getString(2)); // inItemIdx
                        item.add(rs.getString(3)); // pIdx
                        item.add(rs.getString(4)); // pName
                        item.add(rs.getString(5)); // pQuantity
                        item.add(rs.getString(6)); // wIdx
                        inRequestItemList.add(item);
                    }
                }
            }

            // 리턴
            return inRequestItemList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<List<String>> getRequestListByPeriod(Timestamp startDate, Timestamp endDate) {
        String sql = "{call getInReqListByPeriod(?, ?)}";
        List<List<String>> requestList = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            CallableStatement call =  conn.prepareCall(sql)
        ) {
            // 데이터
            call.setTimestamp(1, startDate);
            call.setTimestamp(2, endDate);

            // 실행
            boolean hasResult = call.execute();
            if (hasResult) {
                try (ResultSet rs = call.getResultSet()) {
                    while (rs.next()) {
                        List<String> periodReqList = new ArrayList<>();
                        periodReqList.add(String.valueOf(rs.getInt(1))); // inRequestIdx
                        periodReqList.add(String.valueOf(rs.getInt(2))); // wIdx
                        periodReqList.add(rs.getString(3)); // itemInfo(...등 n건)
                        periodReqList.add(String.valueOf(rs.getTimestamp(4))); // inboundDate
                        requestList.add(periodReqList);
                    }
                }
            }

            // 리턴
            return requestList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

