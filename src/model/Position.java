/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Pozíciót reprezentáló osztály.
 *
 * @author bence
 */
public class Position {

    public int x, y;

    /**
     * Pozíció konstruktora.
     *
     * @param x x koordináta
     * @param y y koordináta
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A pozíciót eltolja egy bizonyos mértékkel.
     *
     * @param d az irány, amelybe tolunk
     * @return az új pozíció
     */
    public Position translate(Direction d) {
        return new Position(x + d.x, y + d.y);
    }
}
