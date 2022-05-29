package com.sql;

public class Match {

    private int id;

    private int season;

    private String team1;

    private String team2;


    private String tossWinner;

    private String winner;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getSeason() {
        return season;
    }


    public void setSeason(int season) {
        this.season = season;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }
}