package domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Outbound {
    private int outRequestIdx;
    private Date outDueDate;
    private EntityStatus requestStatus;
    private Timestamp outRequestDate;
    private int uIdx;
    private Timestamp outboundDate;
    private int wIdx;
}
