/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy játékszintet reprezentáló osztály.
 *
 * @author bence
 */
public class GameLevel {

    public final int levelID;
    public final int rows, cols;
    public final LevelItem[][] level;
    private final Position startPos;
    public Position player = new Position(0, 0);
    public ArrayList<Guard> guardsH = new ArrayList<>();
    public ArrayList<Guard> guardsV = new ArrayList<>();
    private int numBaskets, numCollectedBaskets;

    /**
     * A játékszint konstruktora.
     *
     * @param gameLevelRows a szinthez tartozó, fájlból beolvasott sorok
     * @param levelID a szint azonosítója
     */
    public GameLevel(ArrayList<String> gameLevelRows, int levelID) {
        this.levelID = levelID;
        int c = 0;
        for (String s : gameLevelRows) {
            if (s.length() > c) {
                c = s.length();
            }
        }
        rows = gameLevelRows.size() + 2;
        cols = c + 2;
        level = new LevelItem[rows][cols];
        numBaskets = 0;
        numCollectedBaskets = 0;

        for (int i = 0; i < rows; i++) {
            String s = null;
            if (!(i == 0 || i == rows - 1)) {
                s = gameLevelRows.get(i - 1);
            }
            for (int j = 0; j < cols; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    level[i][j] = LevelItem.MOUNTAIN;
                    continue;
                }
                switch (s.charAt(j - 1)) {
                    case 'Y':
                        player = new Position(j, i);
                        level[i][j] = LevelItem.EMPTY;
                        break;
                    case 'T':
                        level[i][j] = LevelItem.TREE;
                        break;
                    case 'M':
                        level[i][j] = LevelItem.MOUNTAIN;
                        break;
                    case 'B':
                        level[i][j] = LevelItem.BASKET;
                        numBaskets++;
                        break;
                    case 'H':
                        level[i][j] = LevelItem.EMPTY;
                        Position ph = new Position(j, i);
                        guardsH.add(new Guard(ph, Direction.RIGHT));
                        break;
                    case 'V':
                        level[i][j] = LevelItem.EMPTY;
                        Position pv = new Position(j, i);
                        guardsV.add(new Guard(pv, Direction.UP));
                        break;
                    default:
                        level[i][j] = LevelItem.EMPTY;
                        break;
                }
            }
        }
        startPos = player;
    }

    /**
     * A játékszint konstruktora.
     *
     * @param gl egy adott játékszint
     */
    public GameLevel(GameLevel gl) {
        levelID = gl.levelID;
        rows = gl.rows;
        cols = gl.cols;
        numBaskets = gl.numBaskets;
        numCollectedBaskets = gl.numCollectedBaskets;
        level = new LevelItem[rows][cols];
        player = new Position(gl.player.x, gl.player.y);
        startPos = gl.startPos;
        for (int i = 0; i < rows; i++) {
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
        this.guardsH = new ArrayList<>();
        for (Guard g : gl.guardsH) {
            this.guardsH.add(new Guard(new Position(g.p.x, g.p.y), g.d));
        }
        this.guardsV = new ArrayList<>();
        for (Guard g : gl.guardsV) {
            this.guardsV.add(new Guard(new Position(g.p.x, g.p.y), g.d));
        }
    }

    /**
     * Visszaadja, hogy véget ért-e a szint, azaz összegyűjtöttük-e az összes
     * kosarat.
     *
     * @return véget ért-e a szint
     */
    public boolean isLevelEnded() {
        return numBaskets <= numCollectedBaskets;
    }

    /**
     * Visszaadja, hogy a pályán belüli-e a megadott pozíció.
     *
     * @param p a vizsgálandó pozíció
     * @return a pályán belül található-e a pozíció
     */
    public boolean isValidPosition(Position p) {
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }

    /**
     * Visszaadja, hogy szabad-e az adott pozíció a táblán.
     *
     * @param p a vizsgálandó pozíció
     * @return szabad-e a pozíció
     */
    public boolean isFree(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.EMPTY);
    }

    /**
     * Visszaadja, hogy akadály-e az adott pozíció a táblán.
     *
     * @param p a vizsgálandó pozíció
     * @return akadály-e a pozíció
     */
    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.TREE
                || li == LevelItem.MOUNTAIN
                || li == LevelItem.BASKET);
    }

    /**
     * Visszaadja, hogy kosár-e az adott pozíció a táblán.
     *
     * @param p a vizsgálandó pozíció
     * @return kosár-e a pozíció
     */
    boolean isBasket(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.BASKET);
    }

    /**
     * A játékos mozgatásáért felelős függvény.
     *
     * @param d a mozgatás iránya
     * @return sikeres-e a mozgatás
     */
    public boolean movePlayer(Direction d) {
        if (isLevelEnded()) {
            return false;
        }
        Position curr = player;
        Position next = curr.translate(d);
        if (isFree(next)) {
            player = next;
            return true;
        }
        return false;
    }

    /**
     * Egy kosár felvételéért felelős függvény.
     *
     * @param d a játékos mozgásának iránya
     * @return sikeres-e a kosárfelvétel
     */
    public boolean collectBasket(Direction d) {
        if (isLevelEnded()) {
            return false;
        }
        Position curr = player;
        Position next = curr.translate(d);
        if (isBasket(next)) {
            player = next;
            level[next.y][next.x] = LevelItem.EMPTY;
            numCollectedBaskets++;
            return true;
        }
        return false;
    }

    /**
     * A járőrök mozgatásáért felelős metódus.
     */
    public void moveGuards() {
        moveGuardsH();
        moveGuardsV();
    }

    /**
     * A vízszintesen mozgó járőrök mozgatásáért felelős metódus.
     */
    private void moveGuardsH() {
        if (isLevelEnded()) {
            return;
        }
        for (Guard g : guardsH) {
            Position curr = g.p;
            Position next = curr.translate(g.d);
            if (isFree(next)) {
                g.p = next;
            } else if (isObstacle(next)) {
                g.d = (g.d == Direction.RIGHT
                        ? Direction.LEFT : Direction.RIGHT);
                next = curr.translate(g.d);
                g.p = next;
            }
        }
    }

    /**
     * A függőlegesen mozgó járőrök mozgatásáért felelős metódus.
     */
    private void moveGuardsV() {
        if (isLevelEnded()) {
            return;
        }
        for (Guard g : guardsV) {
            Position curr = g.p;
            Position next = curr.translate(g.d);
            if (isFree(next)) {
                g.p = next;
            } else if (isObstacle(next)) {
                g.d = (g.d == Direction.UP
                        ? Direction.DOWN : Direction.UP);
                next = curr.translate(g.d);
                g.p = next;
            }
        }
    }

    /**
     * Megvizsgálja, hogy nincs-e egy őr közelében Maci Laci.
     *
     * @return őr közelében van-e a karakter
     */
    public boolean check() {
        if (isLevelEnded()) {
            return false;
        }
        for (Guard g : guardsH) {
            Position curr = g.p;
            Position up = new Position(curr.x, curr.y - 1);
            Position down = new Position(curr.x, curr.y + 1);
            Position left = new Position(curr.x - 1, curr.y);
            Position right = new Position(curr.x + 1, curr.y);
            ArrayList<Position> pos = new ArrayList<>(List.of(up, down, left, right));
            for (Position p : pos) {
                if (p.x == player.x && p.y == player.y) {
                    player = startPos;
                    return true;
                }
            }
        }
        for (Guard g : guardsV) {
            Position curr = g.p;
            Position up = new Position(curr.x, curr.y - 1);
            Position down = new Position(curr.x, curr.y + 1);
            Position left = new Position(curr.x - 1, curr.y);
            Position right = new Position(curr.x + 1, curr.y);
            ArrayList<Position> pos = new ArrayList<>(List.of(up, down, left, right));
            for (Position p : pos) {
                if (p.x == player.x && p.y == player.y) {
                    player = startPos;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Visszaadja a pályán lévő összes kosár számát.
     *
     * @return a pályán lévő összes kosár száma
     */
    public int getNumBaskets() {
        return numBaskets;
    }

    /**
     * Visszaadja az eddig összegyűjtött kosarak számát.
     *
     * @return az eddig összegyűjtött kosarak száma
     */
    public int getNumCollectedBaskets() {
        return numCollectedBaskets;
    }
}
