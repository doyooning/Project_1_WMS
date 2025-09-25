package dao;

import domain.*;
import util.DBUtil;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class FinanceDao implements Finance {

    // 싱글톤 패턴
    private static FinanceDao instance;
    private FinanceDao() {}
    public static FinanceDao getInstance() {
        if (instance == null) instance = new FinanceDao();
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
    public List<Expense> getMontlyExpenseList(int wIdx, String date) {
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getMontlyExpenseList(?, ?)}"; // 변경 3: 표준 JDBC 호출 구문으로 변경
            cstmt = conn.prepareCall(sql);                   // 변경 4: prepareCall() 사용
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            List<Expense> list = new ArrayList<>();
            rs = cstmt.executeQuery();                       // 변경 5: cstmt로 실행

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setEIdx(rs.getInt("eIdx"));
                expense.setWIdx(rs.getInt("wIdx"));
                expense.setEType(rs.getString("eType"));
                expense.setEAmount(rs.getLong("eAmount"));
                expense.setEDate(rs.getDate("eDate"));
                expense.setCreatedAt(rs.getTimestamp("createdAt"));
                expense.setUpdatedAt(rs.getTimestamp("updatedAt"));
                String statusStr = rs.getString("status");
                EntityStatus status = EntityStatus.valueOf(statusStr);
                expense.setStatus(status);
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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getMontlySalesList(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            List<Sales> list = new ArrayList<>();
            rs = cstmt.executeQuery();

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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getMontlySalesTotal(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            int result = 0;
            rs = cstmt.executeQuery();

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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getMontlyExpenseTotal(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            int result = 0;
            rs = cstmt.executeQuery();

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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getYearlySalesList(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            rs = cstmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month");
                long amount = rs.getLong("amount");
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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getYearlyExpenseList(?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, wIdx);
            cstmt.setString(2, date);

            rs = cstmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month");
                long amount = rs.getLong("amount");
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
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getWarehouseList()}";
            cstmt = conn.prepareCall(sql);

            rs = cstmt.executeQuery();

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
                String statusStr = rs.getString("wStatus");
                EntityStatus status = EntityStatus.valueOf(statusStr);
                warehouse.setWStatus(status);
                warehouse.setWStock(rs.getInt("wStock"));
                warehouse.setDoIdx(rs.getInt("doIdx"));
                warehouse.setDoName(rs.getString("doName"));
                warehouse.setWtIdx(rs.getInt("wtIdx"));
                // warehouse.setWUniqueNum(rs.getString("wtUniqueNum")); // 원본 코드에 주석처리 되어있어 유지
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

    @Override
    public int addExpense(Expense expense) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call addExpense(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, expense.getWIdx());
            cstmt.setString(2, expense.getEType());
            cstmt.setLong(3, expense.getEAmount());
            cstmt.setDate(4, (Date)expense.getEDate());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public int modifyExpense(Expense expense) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call modifyExpense(?, ?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, expense.getEIdx());
            cstmt.setInt(2, expense.getWIdx());
            cstmt.setString(3, expense.getEType());
            cstmt.setLong(4, expense.getEAmount());
            cstmt.setDate(5, (Date)expense.getEDate());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public int removeExpense(int eIdx, int wIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call removeExpense(?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, eIdx);
            cstmt.setInt(2, wIdx);

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public SubModel getUserSubInfo(int uIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getUserSubInfo(?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, uIdx);

            rs = cstmt.executeQuery();

            if (rs.next()) {
                SubModel subModel = new SubModel();
                subModel.setSmIdx(rs.getInt("smIdx"));
                subModel.setSmName(rs.getString("smName"));
                subModel.setSmPrice(rs.getLong("smPrice"));
                subModel.setSmAmount(rs.getInt("smAmount"));
                // warehouse.setWUniqueNum(rs.getString("wtUniqueNum")); // 원본 코드에 주석처리 되어있어 유지
                return subModel;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public List<SubModel> getSubModelList() {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getSubModelList()}";
            cstmt = conn.prepareCall(sql);

            rs = cstmt.executeQuery();

            List<SubModel> list = new ArrayList<>();
            while (rs.next()) {
                SubModel subModel = new SubModel();
                subModel.setSmIdx(rs.getInt("smIdx"));
                subModel.setSmName(rs.getString("smName"));
                subModel.setSmPrice(rs.getLong("smPrice"));
                subModel.setSmAmount(rs.getInt("smAmount"));
                // warehouse.setWUniqueNum(rs.getString("wtUniqueNum")); // 원본 코드에 주석처리 되어있어 유지
                list.add(subModel);
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
    public int addSubscription(SubApproval subApproval) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call addSubscription(?, ?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, subApproval.getUIdx());
            cstmt.setInt(2, subApproval.getSmIdx());
            cstmt.setInt(3, subApproval.getWaIdx());
            cstmt.setString(4, subApproval.getSaPayment());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public SubApproval getPrevSub(int uIdx) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getPrevSub(?)}";
            cstmt = conn.prepareCall(sql);


            cstmt.setInt(1, uIdx);

            rs = cstmt.executeQuery();

            if(rs.next()) {
                SubApproval subApproval = new SubApproval();

                subApproval.setSaIdx(rs.getInt("saIdx"));
                subApproval.setSmIdx(rs.getInt("smIdx"));
                subApproval.setUIdx(rs.getInt("uIdx"));
                subApproval.setWaIdx(rs.getInt("waIdx"));
                subApproval.setSaDate(rs.getDate("saDate"));
                subApproval.setSaStartDate(rs.getTimestamp("saStartDate"));
                subApproval.setSaEndDate(rs.getTimestamp("saEndDate"));
                subApproval.setSaPayment(rs.getString("saPayment"));

                return subApproval;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return null;
    }

    @Override
    public int updateSubscriptionStatus(int saIdx, EntityStatus status) {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call updateSubscriptionStatus(?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, saIdx);
            cstmt.setString(2, status.name());

            return cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return 0;
    }

    @Override
    public List<SubApproval> getPendingSubApprovalList() {
        // CallableStatement 사용으로 변경
        try {
            conn = DBUtil.getConnection();
            String sql = "{call getPendingSubApprovalList()}";
            cstmt = conn.prepareCall(sql);

            rs = cstmt.executeQuery();

            List<SubApproval> list = new ArrayList<>();
            while (rs.next()) {
                SubApproval subApproval = new SubApproval();
                subApproval.setSaIdx(rs.getInt("saIdx"));
                subApproval.setSmIdx(rs.getInt("smIdx"));
                subApproval.setUIdx(rs.getInt("uIdx"));
                subApproval.setWaIdx(rs.getInt("waIdx"));
                subApproval.setSaDate(rs.getDate("saDate"));
                subApproval.setSaStartDate(rs.getTimestamp("saStartDate"));
                subApproval.setSaEndDate(rs.getTimestamp("saEndDate"));
                subApproval.setSaPayment(rs.getString("saPayment"));
                list.add(subApproval);
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
