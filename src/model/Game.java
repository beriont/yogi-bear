/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import persistence.Database;
import persistence.HighScore;
import res.ResourceLoader;

/**
 * A játékmodellt reprezentáló osztály.
 *
 * @author bence
 */
public class Game {

    private final HashMap<Integer, GameLevel> gameLevels;
    private GameLevel gameLevel = null;
    private final Database database;
    private static int levelCount = 0;
    private int lives = 3;
    private int points = 0;
    private static Random r = new Random();

    /**
     * A játékmodell konstruktora.
     */
    public Game() {
        gameLevels = new HashMap<>();
        database = new Database();
        readLevels();
    }

    /**
     * Egy véletlenszerű játékszint betöltése.
     */
    public void loadLevel() {
        gameLevel = new GameLevel(gameLevels.get(r.nextInt(1, gameLevels.size() + 1)));
    }

    /**
     * Egy adott azonosítójú játékszint betöltése.
     *
     * @param levelID a játékszint azonosítója
     */
    public void loadLevel(int levelID) {
        gameLevel = new GameLevel(gameLevels.get(levelID));
    }

    /**
     * Új játék indítása.
     */
    public void startNewGame() {
        loadLevel();
        lives = 3;
        points = 0;
    }

    /**
     * A játékos játéktáblán való mozgatásáért felelős függvény.
     *
     * @param d a mozgatási irány
     * @return sikeres-e a mozgatás
     */
    public boolean step(Direction d) {
        if (!isGameEnded()) {
            boolean stepped = gameLevel.movePlayer(d);
            boolean collectedBasket = false;
            if (!stepped) {
                collectedBasket = gameLevel.collectBasket(d);
                if (collectedBasket) {
                    points++;
                }
            }
            return stepped || collectedBasket;
        }
        return false;
    }

    /**
     * A járőrök játéktáblán való mozgatásáért felelős függvény.
     */
    public void stepGuards() {
        gameLevel.moveGuards();
    }

    /**
     * Megvizsgálja, hogy nincs-e járőr közelében Maci Laci a játéktáblán.
     *
     * @return járőr közelében van-e a karakter a játéktáblán
     */
    public boolean checkYogiNearGuards() {
        boolean caught = gameLevel.check();
        if (caught) {
            lives--;
        }
        return caught;
    }

    /**
     * Eltárolja a megadott értékeket új rekordként az adatbázisban.
     *
     * @param name név
     * @param score pontszám
     */
    public void storeInDatabase(String name, int score) {
        database.storeHighScore(name, score);
    }

    /**
     * Visszaadja, hogy van-e játékszint betöltve.
     *
     * @return van-e játékszint betöltve
     */
    public boolean isLevelLoaded() {
        return gameLevel != null;
    }

    /**
     * Visszaadja a játékszint sorainak számát.
     *
     * @return a játékszint sorainak száma
     */
    public int getLevelRows() {
        return gameLevel.rows;
    }

    /**
     * Visszaadja a játékszint oszlopainak számát.
     *
     * @return a játékszint oszlopainak száma
     */
    public int getLevelCols() {
        return gameLevel.cols;
    }

    /**
     * Visszaadja a játékszint egy adott sor- és oszlopindexű elemét.
     *
     * @param row a sorindex
     * @param col az oszlopindex
     * @return az elem
     */
    public LevelItem getItem(int row, int col) {
        return gameLevel.level[row][col];
    }

    /**
     * Visszaadja a játékos életeinek számát.
     *
     * @return a játékos életeinek száma
     */
    public int getLives() {
        return lives;
    }

    /**
     * Visszaadja a játékos pontszámát.
     *
     * @return a játékos pontszáma
     */
    public int getPoints() {
        return points;
    }

    /**
     * Visszaadja a játékszint azonosítóját (ha van betöltve szint).
     *
     * @return a játékszint azonosítója (ha van betöltve szint, különben null)
     */
    public int getLevelID() {
        return (gameLevel != null) ? gameLevel.levelID : null;
    }

    /**
     * Visszaadja a játékszint összes kosarának számát (ha van betöltve szint).
     *
     * @return a játékszint összes kosarának száma (ha van betöltve szint,
     * különben 0)
     */
    public int getLevelNumBaskets() {
        return (gameLevel != null) ? gameLevel.getNumBaskets() : 0;
    }

    /**
     * Visszaadja a játékszinten összegyűjtött kosarak számát (ha van betöltve
     * szint).
     *
     * @return a játékszinten összegyűjtött kosarak száma (ha van betöltve
     * szint, különben 0)
     */
    public int getLevelNumCollectedBaskets() {
        return (gameLevel != null) ? gameLevel.getNumCollectedBaskets() : 0;
    }

    /**
     * Visszaadja, hogy a játékszint befejeződött-e.
     *
     * @return a játékszint befejeződött-e
     */
    public boolean isLevelEnded() {
        return (gameLevel != null && gameLevel.isLevelEnded());
    }

    /**
     * Visszaadja, hogy a játék befejeződött-e.
     *
     * @return a játék befejeződött-e
     */
    public boolean isGameEnded() {
        return lives <= 0;
    }

    /**
     * Visszaadja a játékos pozícióját a játékszinten.
     *
     * @return a játékos pozíciója a játékszinten
     */
    public Position getPlayerPos() {
        return new Position(gameLevel.player.x, gameLevel.player.y);
    }

    /**
     * Visszaadja a játékszinten található, vízszintesen mozgó járőrök listáját.
     *
     * @return a játékszinten található, vízszintesen mozgó járőrök listája
     */
    public ArrayList<Guard> getGuardsH() {
        if (gameLevel != null) {
            ArrayList<Guard> l = new ArrayList<>();
            for (Guard g : gameLevel.guardsH) {
                Position p = new Position(g.p.x, g.p.y);
                l.add(new Guard(p, g.d));
            }
            return l;
        }
        return null;
    }

    /**
     * Visszaadja a játékszinten található, függőlegesen mozgó járőrök listáját.
     *
     * @return a játékszinten található, függőlegesen mozgó járőrök listája
     */
    public ArrayList<Guard> getGuardsV() {
        if (gameLevel != null) {
            ArrayList<Guard> l = new ArrayList<>();
            for (Guard g : gameLevel.guardsV) {
                Position p = new Position(g.p.x, g.p.y);
                l.add(new Guard(p, g.d));
            }
            return l;
        }
        return null;
    }

    /**
     * Visszaadja az adatbázisban szereplő eredmények listáját.
     *
     * @return az adatbázisban szereplő eredmények listája
     */
    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }

    /**
     * Beolvassa a forrásfájlból a pályákat, és eltárolja őket egy HashMap-ben.
     */
    private void readLevels() {
        InputStream is;
        is = ResourceLoader.loadResource("res/levels.txt");

        try (Scanner sc = new Scanner(is)) {
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();

            while (!line.isEmpty()) {
                Integer id = readLevelID(line);
                if (id == null) {
                    return;
                }

                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';') {
                    gameLevelRows.add(line);
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel(gameLevelRows, id));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Hozzáadja az adott játékszintet a HashMap-hez.
     *
     * @param gameLevel a játékszint
     */
    private void addNewGameLevel(GameLevel gameLevel) {
        gameLevels.put(++levelCount, gameLevel);
    }

    /**
     * Visszaadja a fájlban a következő beolvasott sort.
     *
     * @param sc szkenner objektum
     * @return a következő beolvasott sor szövege
     */
    private String readNextLine(Scanner sc) {
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }

    /**
     * Beolvassa a játékszint azonosítóját.
     *
     * @param line az azonosítót tartalmazó sor szövege
     * @return az azonosító
     */
    private Integer readLevelID(String line) {
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') {
            return null;
        }
        Scanner s = new Scanner(line);
        s.next();
        if (!s.hasNextInt()) {
            return null;
        }
        int id = s.nextInt();
        return id;
    }
}
