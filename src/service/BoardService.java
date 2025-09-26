package service;

import domain.Announcement;
import domain.Expense;
import domain.Inquiry;
import domain.Warehouse;

import java.util.List;

public interface BoardService {

    List<Announcement> getAnnouncementList();

    boolean addAnnouncement(Announcement announcement);

    Announcement getAnnouncement(int anIdx);
    boolean modifyAnnouncement(Announcement announcement);
    boolean removeAnnouncement(int anIdx);

    List<Inquiry> getInquiryList();
}
