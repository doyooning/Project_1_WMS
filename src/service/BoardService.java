package service;

import domain.Announcement;
import domain.Expense;
import domain.Inquiry;
import domain.Warehouse;

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
}
