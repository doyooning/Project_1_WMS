package service;

public interface InOutboundService {

    // 요청 등록(일반회원만)
    int addRequest(int num, String str);

    // 요청 승인(관리자만)
    int approveRequest();

    // 요청 수정
    int updateRequest(int Id, int num, String str);

    // 요청 취소
    int cancelRequest(int requestId);

    // 현황 조회
    void getBoundInfo();

}
