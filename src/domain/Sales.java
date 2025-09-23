package domain;

import lombok.Data;

import java.util.Date;

@Data
public class Sales {
    private int sIdx;
    private int saIdx;
    private long sPrice;
    private Date sDate;
}
