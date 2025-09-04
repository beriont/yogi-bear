/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Game;
import model.Guard;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;

/**
 * A játéktábla kirajzolásáért felelős osztály.
 *
 * @author bence
 */
public class Board extends JPanel {

    private Game game;
    private final Image basket, guard, tree, player, mountain, empty;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;

    /**
     * A játéktábla-grafika konstruktora.
     *
     * @param g a játék példány referenciája
     * @throws IOException
     */
    public Board(Game g) throws IOException {
        game = g;
        scale = 1.0;
        scaled_size = (int) (scale * tile_size);
        basket = ResourceLoader.loadImage("res/basket.png");
        guard = ResourceLoader.loadImage("res/guard.png");
        tree = ResourceLoader.loadImage("res/tree.png");
        player = ResourceLoader.loadImage("res/bear.png");
        mountain = ResourceLoader.loadImage("res/mountain.png");
        empty = ResourceLoader.loadImage("res/empty.png");
    }

    /**
     * Beállítja a tábla nagyítását.
     *
     * @param scale a nagyítás mértéke
     * @return
     */
    public boolean setScale(double scale) {
        this.scale = scale;
        scaled_size = (int) (scale * tile_size);
        return refresh();
    }

    /**
     * Frissíti a játéktábla grafikát.
     *
     * @return
     */
    public boolean refresh() {
        if (!game.isLevelLoaded()) {
            return false;
        }
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }

    /**
     * A grafikát újrarajzoló metódus.
     *
     * @param g a grafika példánya
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelLoaded()) {
            return;
        }
        Graphics2D gr = (Graphics2D) g;
        int w = game.getLevelCols();
        int h = game.getLevelRows();
        Position p = game.getPlayerPos();
        ArrayList<Guard> gh = game.getGuardsH();
        ArrayList<Guard> gv = game.getGuardsV();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Image img = null;
                LevelItem li = game.getItem(y, x);
                switch (li) {
                    case BASKET:
                        img = basket;
                        break;
                    case TREE:
                        img = tree;
                        break;
                    case MOUNTAIN:
                        img = mountain;
                        break;
                    case EMPTY:
                        img = empty;
                        break;
                }
                for (Guard gu : gh) {
                    if (gu.p.x == x && gu.p.y == y) {
                        img = guard;
                    }
                }
                for (Guard gu : gv) {
                    if (gu.p.x == x && gu.p.y == y) {
                        img = guard;
                    }
                }
                if (p.x == x && p.y == y) {
                    img = player;
                }
                if (img == null) {
                    continue;
                }
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }

}
