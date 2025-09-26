package dao;

import lombok.Data;

import java.sql.Timestamp;

// 입고고지서 정보를 담을 VO

@Data
public class OutboundBillVO {
    private int outRequestId;
    private Timestamp outDate;
    private int wId;
    private String uName;
}
