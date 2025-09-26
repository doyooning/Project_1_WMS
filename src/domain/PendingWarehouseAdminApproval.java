package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PendingWarehouseAdminApproval {
    private int aIdx;
    private int waIdx;
    private String waName;
    private String waPhone;
    private String waEmail;
    private Timestamp createdAt;
}


