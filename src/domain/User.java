package domain;


import lombok.*;

import java.sql.Timestamp;

@Data
public class User {
    private int uIdx;
    private String uName;
    private String uId;
    private String userPw;
    private String uPhone;
    private String uEmail;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EntityStatus status;
}
