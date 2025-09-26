package controller;

import domain.Announcement;
import domain.Expense;

import java.util.List;

public interface BoardController {
    //로그인 유저 정보 연동
    void setLoggedInUser(Object user);

    // 게시판 메인 페이지, 진입점
    Boolean showBoardMenu();

    //공지사항 목록 조회
    List<Announcement> getAnnouncementList();

    Announcement getAnnouncement(int anIdx);

    Boolean addAnnouncement(Announcement announcement);
    Boolean modifyAnnouncement(Announcement announcement);
}
