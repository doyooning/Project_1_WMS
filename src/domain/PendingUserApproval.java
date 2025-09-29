package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PendingUserApproval {
    private int aIdx;
    private int uIdx;
    private String uName;
    private String uPhone;
    private String uEmail;
    private Timestamp createdAt;
}


