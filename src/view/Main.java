package view;

import controller.InOutboundController;
import controller.InboundControllerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 기능 작동 확인용 Main
public class Main {

    public static void main(String[] args) {
//        InboundControllerImpl incontrol = InboundControllerImpl.getInstance();
//        OutboundControllerImpl outcontrol = OutboundControllerImpl.getInstance();

        InOutboundController inoutcontrol = InboundControllerImpl.getInstance();
        // 관리자 : 1, 일반회원 : 2
        // 회원번호 필요
        int[] userData = new int[2];
        // 임시 유저 데이터 (회원번호, 권한구분)
        userData[0] = 3;
        userData[1] = 2;

        inoutcontrol.showMenu(userData);

    }
}
