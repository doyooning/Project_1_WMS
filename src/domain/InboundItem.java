package domain;

import lombok.Data;

@Data
public class InboundItem {
    private int inItemIdx;
    private int inRequestIdx;
    private String wIdx;
    private String swIdx;
    private String pIdx;
    private int pQuantity;
}
