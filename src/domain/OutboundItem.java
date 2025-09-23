package domain;

import lombok.Data;

@Data
public class OutboundItem {
    private int outItemIdx;
    private int outRequestIdx;
    private String wIdx;
    private String wsIdx;
    private int pIdx;
    private int pQuantity;
}
