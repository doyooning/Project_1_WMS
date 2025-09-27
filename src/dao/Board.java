package dao;

import domain.*;

import java.util.List;

public interface Board {
    List<Announcement> getAnnouncementList();
    int addAnnouncement(Announcement announcement);
    Announcement getAnnouncement(int anIdx);
    int modifyAnnouncement(Announcement announcement);
    int removeAnnouncement(int anIdx);

    List<Inquiry> getInquiryList();

    Inquiry getInquiry(int iqIdx);
    Response getResponse(int iqIdx);
    int addInquiry(Inquiry inquiry);
    int modifyInquiry(Inquiry inquiry);
}
