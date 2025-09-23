package domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Inbound {
    private int inRequestIdx;
    private Date inDueDate;
    private EntityStatus requestStatus;
    private Timestamp inRequestDate;
    private int uIdx;
    private Timestamp inboundDate;
    private String wIdx;
}
