package view.GridBox;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * A Custom Button for GMated
 */
@Setter
@Getter
public class GridButton extends JButton {
    protected int gridx;
    protected int gridy;

    /**
     * Create a GridButton
     * @param text
     * @param x
     * @param y
     */
    public GridButton(String text, int x, int y) {
        super(text);

        this.gridx = x;
        this.gridy = y;
        init();
    }

    /**
     * Create a GridButton
     * @param icon
     * @param x
     * @param y
     */
    public GridButton(ImageIcon icon, int x, int y) {
        super(icon);

        this.gridx = x;
        this.gridy = y;
        init();
    }

    /**
     * Init the button
     */
    private void init() {
        setBackground(Color.lightGray);
    }

    /**
     * Make it clickable
     */
    public void makeClickable() {
        setEnabled(true);
        setBackground(Color.LIGHT_GRAY);
    }

    public void makeAttackable() {
        makeClickable();
        setBackground(Color.RED);
    }

    /**
     * Make it unclickable
     */
    public void makeUnClickable() {
        setEnabled(false);
    }
}
