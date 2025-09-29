package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WarehouseAdmin {
    private int waIdx;
    private String waName;
    private String waId;
    private String waPw;
    private String waPhone;
    private String waEmail;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
    private int taIdx;
    private int wIdx;
}

