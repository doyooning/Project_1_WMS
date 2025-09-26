package dao;

import java.util.List;

public interface InOutboundDAO {

    // 미승인 요청 여러 개를 목록으로 조회
    List<List<String>> getPendingRequestList();

    // 요청을 키워드로 찾기
    List<List<String>> getRequestById(int num);

    // 요청한 상품 정보를 ID로 조회
    List<List<String>> getItemsById(int inRequestIdx);

}
