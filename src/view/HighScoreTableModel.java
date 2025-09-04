/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import persistence.HighScore;

/**
 * A dicsőségtábla modelljét reprezentáló osztály.
 *
 * @author bence
 */
public class HighScoreTableModel extends AbstractTableModel {

    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{"Név", "Pontszám"};

    /**
     * A dicsőségtábla-modell konstruktora.
     *
     * @param highScores a legjobb eredmények listája
     */
    public HighScoreTableModel(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    /**
     * Sorok száma.
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return highScores.size();
    }

    /**
     * Oszlopok száma.
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * Visszaadja az adott sor- és oszlopindex alatt található elemet.
     *
     * @param r sorindex
     * @param c oszlopindex
     * @return az elem
     */
    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if (c == 0) {
            return h.getName();
        } else if (c == 1) {
            return h.getScore();
        }
        return null;
    }

    /**
     * Az adott indexű oszlop neve.
     *
     * @param i az index
     * @return név
     */
    @Override
    public String getColumnName(int i) {
        return colName[i];
    }

}
