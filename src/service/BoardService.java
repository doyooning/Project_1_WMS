package service;

import domain.Announcement;
import domain.Expense;
import domain.Warehouse;

import java.util.List;

public interface BoardService {

    List<Announcement> getAnnouncementList();

    boolean addAnnouncement(Announcement announcement);

    Announcement getAnnouncement(int anIdx);
    boolean modifyAnnouncement(Announcement announcement);
}
