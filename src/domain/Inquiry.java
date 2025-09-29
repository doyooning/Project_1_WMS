package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Inquiry {
    private int iqIdx;
    private char iqType; // 0 - 1:1문의글  1 - 일반 문의글
    private String iqTitle;
    private String iqContent;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
    private int uIdx;
    private String iqPassword;
    private String iqWriter;

    private Response response;
}
