/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * A tábla elemeit reprezentáló felsorolás.
 *
 * @author bence
 */
public enum LevelItem {
    TREE('T'), MOUNTAIN('M'), BASKET('B'), EMPTY('E');

    /**
     * Táblaelem konstruktora.
     *
     * @param rep
     */
    LevelItem(char rep) {
        representation = rep;
    }
    public final char representation;
}
