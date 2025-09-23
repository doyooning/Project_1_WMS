package dao;

import domain.EntityStatus;
import domain.Inbound;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InboundDAO implements InOutboundDAO {

    // 싱글턴 패턴 세팅
    private static InboundDAO inboundDao;
    private InboundDAO() {

    }

    public static InboundDAO getInboundDao() {
        if (inboundDao == null) {
            inboundDao = new InboundDAO();
        }
        return inboundDao;
    }

    // statics
    // 입고요청을 담을 리스트
    private static List<Inbound> inboundList = new ArrayList<>();

    // DAO 인스턴스가 만들어지면 바로 해야할 일
    // = 리스트에 입고요청 집어넣기
    private void dbLoad() {
        String sql = "select * from Inbound";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                Inbound inbound = new Inbound();
                inbound.setInRequestIdx(rs.getInt(1));
                inbound.setInDueDate(rs.getTimestamp(2));
                inbound.setRequestStatus(EntityStatus.valueOf(rs.getString(3)));
                inbound.setInRequestDate(rs.getTimestamp(4));
                inbound.setUIdx(rs.getInt(5));
                inbound.setInboundDate(rs.getTimestamp(6));
                inbound.setWIdx(rs.getInt(7));
                inboundList.add(inbound);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 요청 승인
    public void approveRequest() {
        System.out.println("InboundDao approveRequest");
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
