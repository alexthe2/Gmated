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
    protected int x;
    protected int y;

    /**
     * Create a GridButton
     * @param text
     * @param x
     * @param y
     */
    public GridButton(String text, int x, int y) {
        super(text);

        this.x = x;
        this.y = y;
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

        this.x = x;
        this.y = y;
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
    }

    /**
     * Make it unclickable
     */
    public void makeUnClickable() {
        setEnabled(false);
    }
}
