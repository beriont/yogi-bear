/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import model.Game;

/**
 * A névmegadáshoz szükséges dialógusablakot reprezentáló osztály.
 *
 * @author bence
 */
public class NameDlg extends OKCancelDialog {

    private JTextField text;
    private final Game game;

    /**
     * A dialógusablak konstruktora.
     *
     * @param frame a főablak keretének referenciája
     * @param title a dialógusablak címe
     * @param g a játék példány referenciája
     */
    public NameDlg(JFrame frame, String title, Game g) {
        super(frame, title);
        JTextArea textArea = new JTextArea(
                "Add meg a neved, hogy felkerülhess a dicsőségtáblára, "
                + "és nyomd meg az OK gombot, vagy a Mégse gombra kattintva "
                + "elvetheted az eredményt."
        );
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setOpaque(false);
        text = new JTextField();
        game = g;
        setLayout(new BorderLayout());
        add("North", textArea);
        add("Center", text);
        add("South", btnPanel);
        pack();
        setSize(300, 150);
        setResizable(false);
    }

    /**
     * Visszaadja a szövegdoboz tartalmát.
     *
     * @return a szövegdoboz tartalma
     */
    public String getValue() {
        return text.getText();
    }

    /**
     * Az OK gomb kiválasztása esetén tárolja az eredményt az adatbázisban.
     *
     * @return true
     */
    @Override
    protected boolean processOK() {
        game.storeInDatabase(getValue(), game.getPoints());
        return true;
    }

    /**
     * A mégsem gomb lenyomása esetén végrehajtandó teendők.
     */
    @Override
    protected void processCancel() {
    }
}
