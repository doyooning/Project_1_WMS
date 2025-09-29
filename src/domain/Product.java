package domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Product {
    private String pIdx;
    private String pName;
    private Date pExpireDate;
    private Timestamp updatedAt;
    private EntityStatus status;
    private int bIdx;
    private int pPrice;
    private int cPIdx;
}
