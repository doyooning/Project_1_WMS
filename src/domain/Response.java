package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Response {
    private int rIdx;
    private int iqIdx;
    private String rContent;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
    private int taIdx;
}
