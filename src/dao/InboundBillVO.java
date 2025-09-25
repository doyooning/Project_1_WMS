package dao;

import lombok.*;

import java.sql.Date;

// 입고고지서 정보를 담을 VO

@Data
public class InboundBillVO {
    private int inRequestId;
    private Date inDate;
    private int wId;
    private String uName;
}
