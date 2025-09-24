package dao;

import domain.EntityStatus;
import domain.Expense;
import domain.Sales;
import domain.Warehouse;
import util.DBUtil;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class FinanceDao implements Finance {

    // 싱글톤 패턴
    private static FinanceDao instance;
    private FinanceDao() {}
    public static FinanceDao getInstance() {
        if (instance == null) instance = new FinanceDao();
        return instance;
    }

    private Connection conn;   /** 데이터베이스 연결 객체 */
    private PreparedStatement pstmt;  /** SQL 문 실행을 위한 PreparedStatement 객체 */
    private Statement stmt; /** SQL 문 실행을 위한 Statement 객체 */
    private ResultSet rs; /** SQL 쿼리 결과를 저장하는 ResultSet 객체 */

    private void disConnect() {
        if (rs != null) try {rs.close();} catch (SQLException e) {}
        if (stmt != null) try {stmt.close();} catch (SQLException e) {}
        if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
        if (conn != null) try {conn.close();} catch (SQLException e) {}
    }
    @Override
    public List<Expense> getMontlyExpenseList(int wIdx, String date) {
        try {
            conn = DBUtil.getConnection();
            String sql = "call getMontlyExpenseList(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            List<Expense> list = new ArrayList<>();
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setEIdx(rs.getInt("eIdx"));
                expense.setWIdx(rs.getInt("wIdx"));
                expense.setEType(rs.getString("eType"));
                expense.setEAmount(rs.getLong("eAmount"));
                expense.setEDate(rs.getDate("eDate"));
                expense.setCreatedAt(rs.getTimestamp("createdAt"));
                expense.setUpdatedAt(rs.getTimestamp("updatedAt"));
                String statusStr = rs.getString("status"); // DB에서 ENUM 문자열 가져오기
                EntityStatus status = EntityStatus.valueOf(statusStr); // 문자열 → Enum
                expense.setStatus(status); // 객체에 설정
                list.add(expense);
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
    public List<Sales> getMontlySalesList(int wIdx, String date) {
        try {
            conn = DBUtil.getConnection();
            String sql = "call getMontlySalesList(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            List<Sales> list = new ArrayList<>();
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Sales sales = new Sales();
                sales.setSIdx(rs.getInt("sIdx"));
                sales.setSaIdx(rs.getInt("saIdx"));
                sales.setSPrice(rs.getLong(("sprice")));
                sales.setSDate(rs.getDate("sDate"));
                list.add(sales);
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
    public Integer getMontlySalesTotal(int wIdx, String date) {
        try {
            conn = DBUtil.getConnection();
            String sql = "call getMontlySalesTotal(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            int result = 0;
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }
    @Override
    public Integer getMontlyExpenseTotal(int wIdx, String date) {
        try {
            conn = DBUtil.getConnection();
            String sql = "call getMontlyExpenseTotal(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            int result = 0;
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }
    @Override
    public Map<String, Long> getYearlySalesList(int wIdx, String date) {
        Map<String, Long> list = new LinkedHashMap<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "CALL getYearlySalesList(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month"); // 예: "2025-01"
                long amount = rs.getLong("amount");   // 해당 월의 매출 합계
                list.put(month, amount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }

        return list;
    }
    @Override
    public Map<String, Long> getYearlyExpenseList(int wIdx, String date) {
        Map<String, Long> list = new LinkedHashMap<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "CALL getYearlyExpenseList(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wIdx);
            pstmt.setString(2, date);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month"); // 예: "2025-01"
                long amount = rs.getLong("amount");   // 해당 월의 지출 합계
                list.put(month, amount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }

        return list;
    }

    @Override
    public List<Warehouse> getWarehouseList() {
        try {
            conn = DBUtil.getConnection();
            String sql = "CALL getWarehouseList()";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Warehouse> list = new ArrayList<>();
            while (rs.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWIdx(rs.getInt("wIdx"));
                warehouse.setWName(rs.getString("wName"));
                warehouse.setWAddr(rs.getString("wAddr"));
                warehouse.setWRent(rs.getInt("wRent"));
                warehouse.setWMaxAmount(rs.getInt("wMaxAmount"));
                warehouse.setCreatedAt(rs.getTimestamp("createdAt"));
                warehouse.setUpdatedAt(rs.getTimestamp("updatedAt"));
                String statusStr = rs.getString("wStatus"); // DB에서 ENUM 문자열 가져오기
                EntityStatus status = EntityStatus.valueOf(statusStr); // 문자열 → Enum
                warehouse.setWStatus(status);
                warehouse.setWStock(rs.getInt("wStock"));
                warehouse.setDoIdx(rs.getInt("doIdx"));
                warehouse.setDoName(rs.getString("doName"));
                warehouse.setWtIdx(rs.getInt("wtIdx"));
                warehouse.setWUniqueNum(rs.getString("wtUniqueNum"));

                // 객체에 설정
                list.add(warehouse);
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
