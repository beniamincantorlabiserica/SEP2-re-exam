package model;

public interface ServerDashboardModel {
    WorkingHours getWorkingHours();
    void setOpeningHours(String openingTime);
    void setClosingHours(String closingTime);
}