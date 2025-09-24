package domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Inbound {
    private int inRequestIdx;           // 요청번호
    private Date inDueDate;             // 입고기한
    private EntityStatus requestStatus; // 요청상태
    private Timestamp inRequestDate;    // 요청일자
    private int uIdx;                   // 유저번호
    private Timestamp inboundDate;      // 입고일자
    private int wIdx;                   // 창고번호
}
