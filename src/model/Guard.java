/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * A járőröket reprezentáló osztály.
 *
 * @author bence
 */
public class Guard {

    public Position p;
    public Direction d;

    /**
     * Járőr konstruktora.
     *
     * @param p járőr pozíciója
     * @param d járőr mozgási iránya
     */
    public Guard(Position p, Direction d) {
        this.p = p;
        this.d = d;
    }
}
