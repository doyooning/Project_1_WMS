package service;

import java.util.List;

public interface InOutboundService {

    // 요청 등록(일반회원만)
    int addRequest(int id, int num, String str);

    // 요청 승인(관리자만)
    int approveRequest(int requestId);

    // 요청 수정
    int updateRequest(int Id, int num, String str);

    // 요청 취소
    int cancelRequest(int requestId);

    // 현황 조회
    List<List<String>> getBoundInfo(int uId);

}
