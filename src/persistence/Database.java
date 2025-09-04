/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Az adatbázis kezeléséért felelős osztály.
 *
 * @author bence
 */
public class Database {

    private final int maxScores = 10;
    private final String tableName = "scores";
    private final Connection conn;
    private final ArrayList<HighScore> highScores;
    private PreparedStatement insertStatement;
    private PreparedStatement deleteStatement;

    /**
     * Az adatbázis-kapcsolat konstruktora.
     */
    public Database() {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:derby://localhost:1527/scores;");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        this.conn = c;
        try {
            String insertQuery = "INSERT INTO SCORES (TIMESTAMP, NAME, SCORE) VALUES (?, ?, ?)";
            insertStatement = conn.prepareStatement(insertQuery);
            String deleteQuery = "DELETE FROM SCORES "
                    + "WHERE TIMESTAMP = ("
                    + "SELECT MIN(TIMESTAMP) "
                    + "FROM SCORES "
                    + "WHERE SCORE = ?"
                    + ")";
            deleteStatement = conn.prepareStatement(deleteQuery);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        highScores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * Eltárol az adatbázisban egy új teljesítményt.
     *
     * @param name név
     * @param newScore pontszám
     */
    public void storeHighScore(String name, int newScore) {
        storeToDatabase(name, newScore);
    }

    /**
     * Visszaadja az adatbázisban szereplő teljesítmények lsitáját.
     *
     * @return az adatbázisban szereplő teljesítmények lsitája
     */
    public ArrayList<HighScore> getHighScores() {
        ArrayList<HighScore> scores = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                scores.add(new HighScore(name, score));
            }
            sortHighScores(scores);
        } catch (Exception e) {
            System.out.println("loadHighScores error: " + e.getMessage());
        }
        return scores;
    }

    /**
     * (Újra) betölti a listába az adatbázisban szereplő értékeket.
     */
    private void loadHighScores() {
        highScores.clear();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                highScores.add(new HighScore(name, score));
            }
            sortHighScores(highScores);
        } catch (Exception e) {
            System.out.println("loadHighScores error: " + e.getMessage());
        }
    }

    /**
     * Csökkenő sorrendbe rendezi az eredményeket.
     *
     * @param highScores a teljesítmények listája
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore t, HighScore t1) {
                return t1.getScore() - t.getScore();
            }
        });
    }

    /**
     * Eltárol az adatbázisban egy új teljesítményt.
     *
     * @param name név
     * @param score pontszám
     */
    private void storeToDatabase(String name, int score) {
        if (highScores.size() < maxScores) {
            try {
                insertScore(name, score);
                loadHighScores();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getScore();
            if (leastScore < score) {
                try {
                    deleteScores(leastScore);
                    insertScore(name, score);
                    loadHighScores();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Beszúr egy új teljesítményt az adatbázisba.
     *
     * @param name név
     * @param score pontszám
     * @throws SQLException
     */
    private int insertScore(String name, int score) throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        insertStatement.setTimestamp(1, ts);
        insertStatement.setString(2, name);
        insertStatement.setInt(3, score);
        return insertStatement.executeUpdate();
    }

    /**
     * Törli a legkorábbi megadott pontszámmal rendelkező rekordot az
     * adatbázisból.
     *
     * @param score pontszám
     * @throws SQLException
     */
    private int deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        return deleteStatement.executeUpdate();
    }

}
