package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CheckLog {
    private int clIdx;
    private String pIdx;
    private String wUniqueNum;
    private String wName;
    private int wsIdx;
    private String wsName;
    private Timestamp createdAt;
    private EntityStatus clstatus;
    private EntityStatus clCorrect;
}
