package com.sql;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
public class Main {
    public static final int ID = 0;
    public static final int SEASON = 1;
    public static final int TEAM1 = 4;
    public static final int TEAM2 = 5;
    public static final int TOSS_WINNER = 6;
    public static final int WINNER = 10;
    public static void main(String[] args) {
        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerYear(matches);
        findNumberOfMatchesWonPerTeamInallSeasons(matches);
        findExtraRunsConcededPerTeamIn2016(matches, deliveries);
        findMostEconomicalBowlerIn2015(matches, deliveries);
        findNumberOfTossesWonByEachTeam(matches);
    }
    private static void findMostEconomicalBowlerIn2015(List<Match> matches, List<Delivery> deliveries) {
        List<Integer> matchIds = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            int season = matches.get(i).getSeason();
            if (season==2015 ){
                matchIds.add(Integer.valueOf(String.valueOf(matches.get(i).getId())));
            }
        }
        HashMap<String, Integer> bowlerRuns = new HashMap();
        HashMap<String, Integer> bowlerBalls = new HashMap();

        for (int j = 0; j < deliveries.size(); j++) {
            int deliveryId1 = deliveries.get(j).getMatchId();
            if (matchIds.contains(deliveryId1) && deliveries.get(j).getIsSuperOver()==0) {
                if (Integer.valueOf(deliveries.get(j).getWideRuns()) == 0 && Integer.valueOf(deliveries.get(j).getNoBallRuns()) == 0) {
                    String bowler = deliveries.get(j).getBowler();
                    if (bowlerBalls.containsKey(bowler)) {
                        bowlerBalls.put(bowler, bowlerBalls.get(bowler) + 1);
                    } else {
                        bowlerBalls.put(bowler, 1);
                    }
                }
                int totalRuns = Integer.valueOf(deliveries.get(j).getTotalRuns());
                int byeRuns = Integer.valueOf(deliveries.get(j).getByeRuns());
                int legByeRuns = Integer.valueOf(deliveries.get(j).getLegByeRuns());
                String bowler1 = deliveries.get(j).getBowler();

                int runs = Integer.valueOf(totalRuns - byeRuns - legByeRuns);
                if (bowlerRuns.containsKey(bowler1)) {
                    bowlerRuns.put(bowler1, bowlerRuns.get(bowler1) + runs);
                } else {
                    bowlerRuns.put(bowler1, runs);
                }
            }
        }
        HashMap<String, Float> bowlerEconomies = new HashMap();
        List<String> bowlers = new ArrayList<>(bowlerBalls.keySet());

        for (int i = 0; i < bowlers.size(); i++) {
            String bowler = bowlers.get(i);
            float overs = bowlerBalls.get(bowler) / 6;
            float economy = bowlerRuns.get(bowler) / overs;
            bowlerEconomies.put(bowler, economy);
        }
        HashMap<String, Float> sortedBowlerEconomies = sortByValue(bowlerEconomies);
        Map.Entry<String, Float> entry = sortedBowlerEconomies.entrySet().iterator().next();
        String key = entry.getKey();
        Float value = entry.getValue();
        System.out.println("For the year 2015 the top economical bowler ---- Name : " + key + "  -- Economy : " + value);
    }
    private static HashMap<String, Float> sortByValue(HashMap<String, Float> sortedBowlers) {
        List<Map.Entry<String, Float>> sortedList = new LinkedList<Map.Entry<String, Float>>(sortedBowlers.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> object1,
                               Map.Entry<String, Float> object2) {
                return (object1.getValue()).compareTo(object2.getValue());
            }
        });
        HashMap<String, Float> sortedEconomy = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float>  listObject: sortedList) {
            sortedEconomy.put(listObject.getKey(), listObject.getValue());
        }
        return sortedEconomy;
    }
    private static void findNumberOfTossesWonByEachTeam(List<Match> matches) {
        HashMap<String, Integer> numberOfTossesWonPerTeam = new HashMap<>();
        for (int i = 0; i < matches.size(); i++) {
            String team = matches.get(i).getTossWinner();
            if (numberOfTossesWonPerTeam.containsKey(team)) {
                int count =  numberOfTossesWonPerTeam.get(team);
                numberOfTossesWonPerTeam.put(team, count += 1);
            } else {
                numberOfTossesWonPerTeam.put(team, 1);
            }
        }
        System.out.println(("numberOfTossesWonPerTeam"));
        System.out.println(numberOfTossesWonPerTeam);
    }
    private static void findExtraRunsConcededPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        HashMap<String, Integer> extraRunsConcededPerTeam = new HashMap<>();
        List<Integer> matchIds = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            int season = matches.get(i).getSeason();

            if (season==2016) {
                matchIds.add(Integer.valueOf(String.valueOf(matches.get(i).getId())));
            }
        }
        for (int j = 0; j < deliveries.size(); j++) {

            int deliviriesId = deliveries.get(j).getMatchId();
            if (matchIds.contains(deliviriesId)) {
                String team = deliveries.get(j).getBowlingTeam();
                if (extraRunsConcededPerTeam.containsKey(team)) {
                    int count = extraRunsConcededPerTeam.get(team);
                    extraRunsConcededPerTeam.put(team, count + Integer.valueOf(deliveries.get(j).getExtraRuns()));
                } else {
                    extraRunsConcededPerTeam.put(team, Integer.valueOf(deliveries.get(j).getExtraRuns()));
                }
            }
        }
        System.out.println("extraRunsConcededPerTeam");
        System.out.println(extraRunsConcededPerTeam);
    }
    private static void findNumberOfMatchesWonPerTeamInallSeasons(List<Match> matches) {
        HashMap<String, Integer> numberOfMatchesWonPerTeam = new HashMap<>();
        for (int i = 0; i < matches.size(); i++) {
            String team = matches.get(i).getWinner();
            if (numberOfMatchesWonPerTeam.containsKey(team)) {
                int count = (int) numberOfMatchesWonPerTeam.get(team);
                numberOfMatchesWonPerTeam.put(team, count += 1);

            } else {
                numberOfMatchesWonPerTeam.put(team, 1);
            }
        }
        System.out.println(("numberOfMatchesWonPerTeam"));
        System.out.println(numberOfMatchesWonPerTeam);
    }
    private static void findNumberOfMatchesPlayedPerYear(List<Match> matches) {
        HashMap<Integer, Integer> totalMatchesPlayedPerYear = new HashMap<>();
        for (int i = 0; i < matches.size(); i++) {
            int year = matches.get(i).getSeason();
            if (totalMatchesPlayedPerYear.containsKey(year)) {
                int count = (int) totalMatchesPlayedPerYear.get(year);
                totalMatchesPlayedPerYear.put(Integer.valueOf(String.valueOf(year)), count += 1);
            } else {
                totalMatchesPlayedPerYear.put(Integer.valueOf(String.valueOf(year)), 1);
            }
        }
        System.out.println("NumberOfMatchesPlayedPerYear");
        System.out.println(totalMatchesPlayedPerYear);
    }
    private static List<Delivery> getDeliveriesData() {
        List<Delivery> deliveries = null;
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            Connection deliveryConnect= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/iplproject","root","Sudeer@7904");
            Statement deliveriesStatement=deliveryConnect.createStatement();
            ResultSet deliveriesResultSet=deliveriesStatement.executeQuery("select * from deliveries");

            deliveries= new ArrayList<Delivery>();
            while(deliveriesResultSet.next()) {
                Delivery delivery = new Delivery();
                delivery.setExtraRuns((deliveriesResultSet.getInt("extra_runs")));
                delivery.setMatchId(deliveriesResultSet.getInt("match_id"));
                delivery.setBowler(deliveriesResultSet.getString("bowler"));
                delivery.setBowlingTeam(deliveriesResultSet.getString("bowling_team"));
                delivery.setTotalRuns((deliveriesResultSet.getInt("total_runs")));
                delivery.setOver(deliveriesResultSet.getInt("over"));
                delivery.setBall(deliveriesResultSet.getInt("ball"));
                delivery.setIsSuperOver(deliveriesResultSet.getInt("is_super_over"));
                delivery.setWideRuns(deliveriesResultSet.getInt("wide_runs"));
                delivery.setByeRuns(deliveriesResultSet.getInt("bye_runs"));
                delivery.setLegByeRuns(deliveriesResultSet.getInt("legbye_runs"));
                delivery.setNoBallRuns(deliveriesResultSet.getInt("noball_runs"));

                deliveries.add(delivery);
            }
            deliveryConnect.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return deliveries;
    }

    private static List<Match> getMatchesData() {
        List<Match> matches = new ArrayList<>();

        try{
            //Class.forName("com.mysql.jdbc.Driver");
            Connection connectToDataBase= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/iplproject","root","Sudeer@7904");
            Statement matchesStatement=connectToDataBase.createStatement();
            ResultSet matchesResultSet=matchesStatement.executeQuery("select * from matches");

            matches = new ArrayList<Match>();
            while(matchesResultSet.next()) {
                Match match = new Match();
                match.setSeason((matchesResultSet.getInt("season")));
                match.setTossWinner((matchesResultSet.getString("toss_winner")));
                match.setId((matchesResultSet.getInt("id")));
                match.setWinner(matchesResultSet.getString("winner"));
                match.setTeam1(matchesResultSet.getString("team1"));
                match.setTeam1(matchesResultSet.getString("team2"));

                matches.add(match);
            }
            connectToDataBase.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return matches;
    }
}
