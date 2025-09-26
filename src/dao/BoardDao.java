package dao;

import domain.Announcement;
import domain.EntityStatus;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDao implements Board {

    // 싱글톤 패턴
    private static BoardDao instance;
    private BoardDao() {}
    public static BoardDao getInstance() {
        if (instance == null) instance = new BoardDao();
        return instance;
    }

    private Connection conn;
    private PreparedStatement pstmt;
    private Statement stmt;
    private ResultSet rs;
    private CallableStatement cstmt; // 변경 1: CallableStatement 멤버 변수 추가

    private void disConnect() {
        if (rs != null) try {rs.close();} catch (SQLException e) { e.printStackTrace(); }
        if (stmt != null) try {stmt.close();} catch (SQLException e) { e.printStackTrace(); }
        if (pstmt != null) try {pstmt.close();} catch (SQLException e) { e.printStackTrace(); }
        if (cstmt != null) try {cstmt.close();} catch (SQLException e) { e.printStackTrace(); } // 변경 2: cstmt 자원 해제 추가
        if (conn != null) try {conn.close();} catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Announcement> getAnnouncementList() {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getAnnouncementList()}";
            cstmt = conn.prepareCall(sql);

            rs = cstmt.executeQuery();

            List<Announcement> list = new ArrayList<>();
            while (rs.next()) {
                Announcement announcement = new Announcement();

                announcement.setAnIdx(rs.getInt("anIdx"));
                announcement.setTaIdx(rs.getInt("taIdx"));
                announcement.setAnTitle(rs.getString("anTitle"));
                announcement.setAnContent(rs.getString("anContent"));
                announcement.setCreatedAt(rs.getTimestamp("createdAt"));
                announcement.setUpdatedAt(rs.getTimestamp("updatedAt"));
                announcement.setStatus(EntityStatus.valueOf(rs.getString("status")));

                list.add(announcement);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }
}
