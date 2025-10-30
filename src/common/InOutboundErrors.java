package common;

public enum InOutboundErrors {

    DATA_INPUT_ERROR("""
            
            [오류] 데이터 입력 중 오류가 발생하였습니다.
                 (해당하는 정보가 없거나, 요청을 처리할 수 없습니다.)
            """),

    INVALID_INPUT_ERROR("""
                    
                    [오류] 유효하지 않은 입력입니다.
                    
                    """),

    DATE_INPUT_ERROR("""
                    
                    [오류] 날짜 입력에 오류가 있습니다.
                    
                    """),

    NUMBER_INPUT_ERROR("""
                    
                    [오류] 숫자만 입력하여 주십시오.
                    
                    """),

    UNEXPECTED_ERROR("""
                    
                    [오류] 예기치 못한 오류가 발생하였습니다.
                    
                    """),

    VO_LOAD_ERROR("""
                    
                    [오류] 고지서 정보를 불러오는 데 실패하였습니다.
                          (고지서는 승인된 요청만 출력 가능합니다.)
                    
                    """),

    CANNOT_APPROVE_ERROR("""
                    
                    [오류] 재고가 부족하여 승인할 수 없습니다.
                    
                    """),

    INACCESSIBLE_REQUEST_ERROR("""
                    
                    [요청 접근 불가] 회원님의 요청번호가 맞는지 확인하십시오.
                    ============================================================
                    """);

    private final String text;
    InOutboundErrors(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

}
