package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Stock {
    private String pIdx;
    private String pName;
    private int wIdx;
    private String wUniqueNum;
    private int wsIdx;
    private int sAvail;
    private int sNotAvail;
    private int sSafe;
    private String pCategory;
    private String sCategory;
    private String tCategory;
    private Timestamp updatedAt;
}

