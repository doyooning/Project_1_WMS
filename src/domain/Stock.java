package domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Stock {
    private String pIdx;
    private int wIdx;
    private int wsIdx;
    private int sWhole;
    private int sNotAvail;
    private int sSafe;
    private Timestamp updatedAt;
}
