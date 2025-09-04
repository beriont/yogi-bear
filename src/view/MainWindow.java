/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import model.Direction;
import model.Game;

/**
 * A főablakot reprezentáló osztály.
 *
 * @author bence
 */
public class MainWindow extends JFrame {

    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;
    private long startTime;
    private Timer timer;
    private Timer guardMoveTimer;
    private Timer guardCheckTimer;
    private NameDlg nameDlg;

    /**
     * A főablak konstruktora.
     *
     * @throws IOException
     */
    public MainWindow() throws IOException {
        game = new Game();
        nameDlg = new NameDlg(this, "Gratulálok!", game);

        setTitle("Maci Laci");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/bear.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Játék");
        JMenuItem menuNewGame = new JMenuItem(new AbstractAction("Új játék") {
            /**
             * Az új játék lehetőség kiválasztásakor újraindul minden időzítő,
             * és a modellben is új pályát indítunk.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startNewGame();
                startTime = System.currentTimeMillis();
                timer.start();
                guardMoveTimer.start();
                guardCheckTimer.start();
                board.refresh();
                pack();
            }
        });

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Dicsőségtábla") {
            /**
             * A dicsőségtábla lehetőség kiválasztásakor megjelenik a
             * dicsőségtáblát tartalmazó ablak.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), MainWindow.this);
            }
        });

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Kilépés") {
            /**
             * A kilépés lehetőség kiválasztásakor a játékból kilépünk.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuGame.add(menuNewGame);
        menuGame.add(menuHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("label");

        add(gameStatLabel, BorderLayout.NORTH);
        try {
            add(board = new Board(game), BorderLayout.CENTER);
        } catch (IOException ex) {
        }

        addKeyListener(new KeyAdapter() {
            /**
             * A lenyomott billentyűnek megfelelően mozog a karakterünk, illetve
             * itt történik az új szint betöltése is, ha minden kosarat
             * összegyűjtöttünk.
             */
            @Override
            public void keyPressed(KeyEvent ke) {
                if (!game.isGameEnded()) {
                    super.keyPressed(ke);
                    if (!game.isLevelLoaded()) {
                        return;
                    }
                    int kk = ke.getKeyCode();
                    Direction d = null;
                    switch (kk) {
                        case KeyEvent.VK_A:
                            d = Direction.LEFT;
                            break;
                        case KeyEvent.VK_D:
                            d = Direction.RIGHT;
                            break;
                        case KeyEvent.VK_W:
                            d = Direction.UP;
                            break;
                        case KeyEvent.VK_S:
                            d = Direction.DOWN;
                            break;
                    }
                    board.repaint();
                    if (d != null && game.step(d)) {
                        if (game.isLevelEnded()) {
                            refreshGameStatLabel();
                            game.loadLevel();
                        }
                    }
                }
            }
        });

        timer = new Timer(10, new ActionListener() {
            /**
             * 10 ms-onként frissíti a játékadatokat.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshGameStatLabel();
            }
        });
        guardMoveTimer = new Timer(1000, new ActionListener() {
            /**
             * Másodpercenként lépteti az őröket.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                game.stepGuards();
                board.repaint();
            }
        });
        guardCheckTimer = new Timer(10, new ActionListener() {
            /**
             * 10 ms-onként ellenőrzi, hogy nincs-e valamelyik őr közelében Maci
             * Laci, és ha igen, azt is ellenőrzi, hogy van-e még életpontja, ha
             * nincs, leállítja a játékot, és megadhatjuk a nevünket a
             * dicsőségtáblába.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.checkYogiNearGuards()) {
                    if (game.isGameEnded()) {
                        refreshGameStatLabel();
                        timer.stop();
                        guardMoveTimer.stop();
                        guardCheckTimer.stop();
                        nameDlg.setVisible(true);
                    }
                }
            }
        });
        startTime = System.currentTimeMillis();
        timer.start();

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadLevel(10);
        board.setScale(1.5);
        pack();
        guardMoveTimer.start();
        guardCheckTimer.start();
        setVisible(true);
    }

    /**
     * Frissíti a játék adatait tartalmazó címkét.
     */
    private void refreshGameStatLabel() {
        String s = "Életek száma: "
                + game.getLives()
                + ", pontszám: "
                + game.getPoints()
                + ", kosarak: "
                + game.getLevelNumCollectedBaskets()
                + "/" + game.getLevelNumBaskets()
                + ", eltelt idő: "
                + elapsedTime() + " ms";
        gameStatLabel.setText(s);
    }

    /**
     * A játék kezdetétől eltelt időt visszaadó függvény.
     *
     * @return A játék kezdetétől eltelt idő
     */
    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * A fő metódus, amely megnyitja a főablakot.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {
        }
    }
}
