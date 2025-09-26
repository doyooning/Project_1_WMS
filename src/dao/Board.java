package dao;

import domain.Announcement;
import domain.Warehouse;

import java.util.List;

public interface Board {
    List<Announcement> getAnnouncementList();
}
