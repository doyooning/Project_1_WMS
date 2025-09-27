package service;

import domain.*;

import java.util.List;
import java.util.Map;

public interface BoardService {

    List<Announcement> getAnnouncementList();

    boolean addAnnouncement(Announcement announcement);

    Announcement getAnnouncement(int anIdx);
    boolean modifyAnnouncement(Announcement announcement);
    boolean removeAnnouncement(int anIdx);

    List<Inquiry> getInquiryList();

    Inquiry getInquiry(Object userInfo, int iqIdx);

    boolean addInquiry(Inquiry inquiry);
    boolean modifyInquiry(Inquiry inquiry);
    boolean removeInquiry(int iqIdx);

    boolean addResponse(Response response);
}
