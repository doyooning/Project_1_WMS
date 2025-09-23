package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Announcement {
    private int anIdx;
    private int taIdx;
    private String anTitle;
    private String anContent;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
}
