package domain;

import lombok.Data;

@Data
public class Approval { // User, WarehouseAdmin 의 회원가입 승인요청
    private int aIdx; // 승인번호
    private int uIdx; // 승인요청한 User
    private int waIdx; // 승인요청한 WarehouseAdmin
    private int taIdx; // 승인할 TotalAdmin
    private EntityStatus status; //PENDING:승인 대기 / APPROVED:승인 완료 / REJECTED:승인 거절
}
