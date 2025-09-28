package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Warehouse {
    private int wIdx;
    private String wName;
    private String wAddr;
    private int wRent;
    private int wMaxAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus wStatus;
    private int wStock;
    private int doIdx;
    private String doName;
    private int wtIdx;
    private String wtName;
    private String wUniqueNum;
}
