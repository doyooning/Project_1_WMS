package dao;

import domain.Announcement;
import domain.Expense;
import domain.Inquiry;
import domain.Warehouse;

import java.util.List;

public interface Board {
    List<Announcement> getAnnouncementList();
    int addAnnouncement(Announcement announcement);
    Announcement getAnnouncement(int anIdx);
    int modifyAnnouncement(Announcement announcement);
    int removeAnnouncement(int anIdx);

    List<Inquiry> getInquiryList();
}
