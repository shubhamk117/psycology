package com.psychology.glowMentally.StatsUI.PaStatsUI;

import java.util.Date;

public class PaStatsModel implements Comparable<PaStatsModel> {

    String affirmation, date;
    Date dateD;

    public PaStatsModel() {
    }

    public String getAffirmation() {
        return affirmation;
    }

    public void setAffirmation(String affirmation) {
        this.affirmation = affirmation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

    @Override
    public int compareTo(PaStatsModel o) {
        return dateD.compareTo(o.dateD);
    }
}
