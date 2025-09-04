/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * A lépési irányokat reprezentáló felsorolás.
 *
 * @author bence
 */
public enum Direction {
    DOWN(0, 1), LEFT(-1, 0), UP(0, -1), RIGHT(1, 0);

    /**
     * Lépési irány konstruktora.
     *
     * @param x x koordináta
     * @param y y koordináta
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public final int x, y;
}
