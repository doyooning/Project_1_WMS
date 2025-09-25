package dao;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

// 입고고지서 정보를 담을 VO

@Data
public class InboundBillVO {
    private int inRequestId;
    private Timestamp inDate;
    private int wId;
    private String uName;
}
