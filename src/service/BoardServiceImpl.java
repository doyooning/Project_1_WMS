package service;

import dao.BoardDao;
import domain.*;

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

    @Override
    public boolean addAnnouncement(Announcement announcement) {
        int result = boardDao.addAnnouncement(announcement);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public Announcement getAnnouncement(int anIdx) {
        return boardDao.getAnnouncement(anIdx);
    }

    @Override
    public boolean modifyAnnouncement(Announcement announcement) {
        int result = boardDao.modifyAnnouncement(announcement);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean removeAnnouncement(int anIdx) {
        int result = boardDao.removeAnnouncement(anIdx);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public List<Inquiry> getInquiryList() {
        return boardDao.getInquiryList();
    }

    @Override
    public Inquiry getInquiry(Object userInfo, int iqIdx) {
        Inquiry inquiry = boardDao.getInquiry(iqIdx);
        if (inquiry != null) {
            inquiry.setResponse(boardDao.getResponse(iqIdx));

            if (userInfo instanceof WarehouseAdmin) {
                if (inquiry.getIqType() == '0') {
                    inquiry.setIqIdx(0);
                }
            } else if (userInfo instanceof User) {
                if (inquiry.getIqType() == '0' && inquiry.getUIdx() != ((User) userInfo).getUIdx()) {
                    inquiry.setIqIdx(0);
                }
            }
        }
        return inquiry;
    }

    @Override
    public boolean addInquiry(Inquiry inquiry) {
        int result = boardDao.addInquiry(inquiry);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean modifyInquiry(Inquiry inquiry) {
        int result = boardDao.modifyInquiry(inquiry);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean removeInquiry(int iqIdx) {
        int result = boardDao.removeInquiry(iqIdx);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean addResponse(Response response) {
        int result = boardDao.addResponse(response);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean modifyResponse(Response response) {
        int result = boardDao.modifyResponse(response);
        if(result > 0) return true;
        else return false;
    }

    @Override
    public boolean removeResponse(int iqIdx) {
        int result = boardDao.removeResponse(iqIdx);
        if(result > 0) return true;
        else return false;
    }

}
