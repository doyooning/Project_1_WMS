package domain;


import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Expense {
    private int eIdx;
    private int wIdx;
    private String eType;
    private long eAmount;
    private Date eDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
}
