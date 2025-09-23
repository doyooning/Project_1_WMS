package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TotalAdmin {
    private int taIdx;
    private String taName;
    private String taId;
    private String taPw;
    private String taPhone;
    private String taEmail;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
}
