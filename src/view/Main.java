package view;

import controller.InOutboundController;
import controller.InboundControllerImpl;

// 기능 작동 확인용 Main
public class Main {

    public static void main(String[] args) {
//        InboundControllerImpl incontrol = InboundControllerImpl.getInstance();
//        OutboundControllerImpl outcontrol = OutboundControllerImpl.getInstance();

        InOutboundController inoutcontrol = InboundControllerImpl.getInstance();
        // 관리자 : 1, 일반회원 : 2
        inoutcontrol.showMenu(1);

    }
}
