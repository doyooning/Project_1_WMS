package domain;

import lombok.Data;

@Data
public class OutboundItem {
    private int outItemIdx;
    private int pQuantity;
    private int outRequestIdx;
    private String pIdx;
    private int wsIdx;
    private int wIdx;
}
