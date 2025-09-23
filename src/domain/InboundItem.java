package domain;

import lombok.Data;

@Data
public class InboundItem {
    private int inItemIdx;
    private int inRequestIdx;
    private int wIdx;
    private int wsIdx;
    private int pIdx;
    private int pQuantity;
}
