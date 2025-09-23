package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CheckLog {
    private int clIdx;
    private String pIdx;
    private int wIdx;
    private int wsIdx;
    private Timestamp createdAt;
    private EntityStatus clstatus;
    private EntityStatus clCorrect;
}
