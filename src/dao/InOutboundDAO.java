package dao;

public interface InOutboundDAO {

    // 요청 여러 개를 목록으로 조회
    void getRequestList();

    // 요청 하나만 찾기
    void getRequestById(int inRequestIdx);

    // 요청한 상품 정보를 ID로 조회
    void getItemsById(int inRequestIdx);

}
