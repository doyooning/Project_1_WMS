package domain;

public enum EntityStatus {
    PENDING, //승인 대기
    APPROVED,  // 승인 완료
    REJECTED,  // 승인 거절
    CANCELED, //취소

    EXIST, // 존재
    DELETED, // 삭제

    CORRECT, // 일치
    INCORRECT, //불일치

    총관리자,
    탈퇴
}
