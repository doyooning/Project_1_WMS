package view;

import controller.InOutboundController;
import controller.InboundControllerImpl;
import controller.OutboundControllerImpl;

// 기능 작동 확인용 Main
public class Main {

    public static void main(String[] args) {
//        InboundControllerImpl incontrol = InboundControllerImpl.getInstance();
//        OutboundControllerImpl outcontrol = OutboundControllerImpl.getInstance();

        InOutboundController inoutcontrol = InboundControllerImpl.getInstance();
        inoutcontrol.showMenu();

    }
}
