package com.psychology.glowMentally.StatsUI.ERTStatsUI;

public class ErtStatsModel {

    String date;
    int verySad, sad, normal, happy, veryHappy;

    public ErtStatsModel() {
    }

    public ErtStatsModel(String date, int verySad, int sad, int normal, int happy, int veryHappy) {
        this.date = date;
        this.verySad = verySad;
        this.sad = sad;
        this.normal = normal;
        this.happy = happy;
        this.veryHappy = veryHappy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVerySad() {
        return verySad;
    }

    public void setVerySad(int verySad) {
        this.verySad = verySad;
    }

    public int getSad() {
        return sad;
    }

    public void setSad(int sad) {
        this.sad = sad;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getVeryHappy() {
        return veryHappy;
    }

    public void setVeryHappy(int veryHappy) {
        this.veryHappy = veryHappy;
    }
}
