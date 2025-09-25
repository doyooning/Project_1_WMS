package controller;

public enum Errors {

    DATA_INPUT_ERROR("""
            ============================================================
            [오류] 데이터 입력 중 오류가 발생하였습니다.
            ============================================================
            """),

    INVALID_INPUT_ERROR("""
                    ============================================================
                    [오류] 유효하지 않은 입력입니다. 다시 입력해 주십시오.
                    """),

    UNEXPECTED_ERROR("""
                    ============================================================
                    [오류] 예기치 못한 오류가 발생하였습니다. 다시 입력해 주십시오.
                    """);

    private final String text;
    Errors(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

}
