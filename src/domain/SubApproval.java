package domain;

import lombok.Data;

import java.util.Date;

@Data
public class SubApproval {
    private int saIdx;
    private int uIdx;
    private int waIdx;
    private int smIdx;
    private Date saDate;
    private EntityStatus status;
    private Date saStartDate;
    private Date saEndDate;
    private String saPayment;
}
