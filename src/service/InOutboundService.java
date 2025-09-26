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

    // 요청 현황 조회
    List<List<String>> getBoundInfo(int uId);

    // 요청 상품 조회
    List<List<String>> getBoundItemInfo(int uId);

    // 고지서 출력 요청정보
    Object showReqBillData(int requestId);

    // 고지서 출력 물품정보
    List<List<String>> showItemBillData(int requestId);

    // 미승인 요청 조회
    List<List<String>> getPendingRequestList();

}
