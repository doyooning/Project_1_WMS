package service;

import dao.BoardDao;
import domain.Announcement;

import java.util.List;

public class BoardServiceImpl implements BoardService {
    //dao 객체
    private BoardDao boardDao;

    // 싱글톤 패턴 적용
    private static BoardServiceImpl instance;
    private BoardServiceImpl() {
        this.boardDao = BoardDao.getInstance();
    }
    public static BoardServiceImpl getInstance() {
        if (instance == null) instance = new BoardServiceImpl();
        return instance;
    }

    @Override
    public List<Announcement> getAnnouncementList() {
        return boardDao.getAnnouncementList();
    }

}
