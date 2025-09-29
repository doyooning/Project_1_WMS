package domain;

import lombok.Data;

@Data
public class InboundItem {
    private int inItemIdx;
    private int inRequestIdx;
    private int wIdx;
    private int wsIdx;
    private String pIdx;
    private int pQuantity;
}
