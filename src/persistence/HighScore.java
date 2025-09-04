/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

import java.util.Objects;

/**
 * A név-pontszám párost reprezentáló osztály.
 *
 * @author bence
 */
public class HighScore {

    private final String name;
    private final int score;

    /**
     * A teljesítmény konstruktora.
     *
     * @param name név
     * @param score pontszám
     */
    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Visszaadja a két objektum összehasonlításához szükséges hash code-ot.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + this.score;
        return hash;
    }

    /**
     * Összehasonlít két objektumot.
     *
     * @param obj az összehasonlítandó objektum
     * @return egyenlő-e a két objektum
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HighScore other = (HighScore) obj;
        if (this.score != other.score) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Visszaadja a nevet.
     *
     * @return név
     */
    public String getName() {
        return name;
    }

    /**
     * Visszaadja a pontszámot.
     *
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Szöveges reprezentációja az eredménynek.
     *
     * @return szöveges reprezentációja az eredménynek
     */
    @Override
    public String toString() {
        return name + ": " + score;
    }

}
