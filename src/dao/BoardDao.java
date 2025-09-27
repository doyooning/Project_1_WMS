package dao;

import domain.*;
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

    @Override
    public int addAnnouncement(Announcement announcement) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call addAnnouncement(?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, announcement.getTaIdx());
            cstmt.setString(2, announcement.getAnTitle());
            cstmt.setString(3, announcement.getAnContent());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public Announcement getAnnouncement(int anIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getAnnouncement(?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, anIdx);

            rs = cstmt.executeQuery();

            if (rs.next()) {
                Announcement announcement = new Announcement();

                announcement.setAnIdx(rs.getInt("anIdx"));
                announcement.setTaIdx(rs.getInt("taIdx"));
                announcement.setAnTitle(rs.getString("anTitle"));
                announcement.setAnContent(rs.getString("anContent"));
                announcement.setCreatedAt(rs.getTimestamp("createdAt"));
                announcement.setUpdatedAt(rs.getTimestamp("updatedAt"));
                announcement.setStatus(EntityStatus.valueOf(rs.getString("status")));

                return announcement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public int modifyAnnouncement(Announcement announcement) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call modifyAnnoucement(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, announcement.getAnIdx());
            cstmt.setInt(2, announcement.getTaIdx());
            cstmt.setString(3, announcement.getAnTitle());
            cstmt.setString(4, announcement.getAnContent());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public int removeAnnouncement(int anIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call removeAnnouncement(?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, anIdx);

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public List<Inquiry> getInquiryList() {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getInquiryList()}";
            cstmt = conn.prepareCall(sql);

            rs = cstmt.executeQuery();

            List<Inquiry> list = new ArrayList<>();
            while (rs.next()) {
                Inquiry inquiry = new Inquiry();

                inquiry.setIqIdx(rs.getInt("iqIdx"));
                String iqTypeStr = rs.getString("iqType"); // CHAR(1) → String
                char iqType = iqTypeStr.charAt(0);
                inquiry.setIqType(iqType);
                inquiry.setIqTitle(rs.getString("iqTitle"));
                inquiry.setIqContent(rs.getString("iqContent"));
                inquiry.setCreatedAt(rs.getTimestamp("createdAt"));
                inquiry.setUpdatedAt(rs.getTimestamp("updatedAt"));
                inquiry.setStatus(EntityStatus.valueOf(rs.getString("status")));
                inquiry.setUIdx(rs.getInt("uIdx"));
                inquiry.setIqPassword(rs.getString("iqPassword"));

                list.add(inquiry);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public Inquiry getInquiry(int iqIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getInquiry(?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, iqIdx);

            rs = cstmt.executeQuery();

            if (rs.next()) {
                Inquiry inquiry = new Inquiry();

                inquiry.setIqIdx(rs.getInt("iqIdx"));
                String iqTypeStr = rs.getString("iqType"); // CHAR(1) → String
                char iqType = iqTypeStr.charAt(0);
                inquiry.setIqType(iqType);
                inquiry.setIqTitle(rs.getString("iqTitle"));
                inquiry.setIqContent(rs.getString("iqContent"));
                inquiry.setCreatedAt(rs.getTimestamp("createdAt"));
                inquiry.setUpdatedAt(rs.getTimestamp("updatedAt"));
                inquiry.setStatus(EntityStatus.valueOf(rs.getString("status")));
                inquiry.setUIdx(rs.getInt("uIdx"));
                inquiry.setIqPassword(rs.getString("iqPassword"));

                return inquiry;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public Response getResponse(int iqIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getResponse(?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, iqIdx);

            rs = cstmt.executeQuery();

            if (rs.next()) {
                Response response = new Response();

                response.setRIdx(rs.getInt("rIdx"));
                response.setIqIdx(rs.getInt("iqIdx"));
                response.setTaIdx(rs.getInt("taIdx"));
                response.setRContent(rs.getString("rContent"));
                response.setCreatedAt(rs.getTimestamp("createdAt"));
                response.setUpdatedAt(rs.getTimestamp("updatedAt"));
                response.setStatus(EntityStatus.valueOf(rs.getString("status")));

                return response;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public int addInquiry(Inquiry inquiry) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call addInquiry(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, inquiry.getUIdx());
            cstmt.setString(2, Character.toString(inquiry.getIqType()));
            cstmt.setString(3, inquiry.getIqTitle());
            cstmt.setString(4, inquiry.getIqContent());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }
}
