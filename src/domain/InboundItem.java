package domain;

import lombok.Data;

@Data
public class InboundItem {
    private int inItemIdx;
    private int inRequestIdx;
    private String wIdx;
    private String swIdx;
    private int pIdx;
    private int pQuantity;
}
