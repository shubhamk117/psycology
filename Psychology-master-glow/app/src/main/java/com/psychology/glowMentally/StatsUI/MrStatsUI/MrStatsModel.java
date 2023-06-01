package com.psychology.glowMentally.StatsUI.MrStatsUI;

import java.util.Date;

public class MrStatsModel implements Comparable<MrStatsModel>{

    String date, time, event;
    Date dateD;

    public MrStatsModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

    @Override
    public int compareTo(MrStatsModel o) {
        return dateD.compareTo(o.getDateD());
    }
}
