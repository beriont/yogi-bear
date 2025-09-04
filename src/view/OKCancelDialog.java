/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * OK és Mégsem gombokkal rendelkező párbeszédablakok közös őse.
 *
 * @author bence
 */
public abstract class OKCancelDialog extends JDialog {

    public static final int OK = 1;
    public static final int CANCEL = 0;
    protected int btnCode;
    protected JPanel btnPanel;
    protected JButton btnOK;
    protected JButton btnCancel;

    /**
     * Egy párbeszédablak létrehozása.
     *
     * @param frame a keret, amihez a párbeszédablak tartozik
     * @param name a párbeszédablak címe
     */
    protected OKCancelDialog(JFrame frame, String name) {
        super(frame, name, true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        btnCode = CANCEL;
        btnOK = new JButton(actionOK);
        btnOK.setMnemonic('O');
        btnOK.setPreferredSize(new Dimension(90, 25));
        btnCancel = new JButton(actionCancel);
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        InputMap inputMap = btnCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = btnCancel.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", actionCancel);
        }
        btnCancel.setPreferredSize(new Dimension(90, 25));
        getRootPane().setDefaultButton(btnOK);
        btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);
    }

    /**
     * Az ablak bezárását okozó gomb lekérdezése.
     *
     * @return a gomb kódja
     */
    public int getButtonCode() {
        return btnCode;
    }

    /**
     * Az OK megnyomásakor elvégzendő ellenőrzések, műveletek.
     *
     * @return true, ha a gomb lenyomása elfogadott
     */
    protected abstract boolean processOK();

    /**
     * A Cancel megnyomásakor elvégzendő tevékenységek.
     */
    protected abstract void processCancel();

    /**
     * Az OK gomb eseménykezelője.
     */
    private AbstractAction actionOK = new AbstractAction("OK") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (processOK()) {
                btnCode = OK;
                OKCancelDialog.this.setVisible(false);
            }
        }
    };

    /**
     * A Mégsem gomb eseménykezelője.
     */
    private AbstractAction actionCancel = new AbstractAction("Mégsem") {
        @Override
        public void actionPerformed(ActionEvent e) {
            processCancel();
            btnCode = CANCEL;
            OKCancelDialog.this.setVisible(false);
        }
    };
}
