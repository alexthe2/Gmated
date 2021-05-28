package view.GridBox;

import Swings.IHotRescale;

import javax.swing.*;
import java.awt.*;

public class GridButton extends JButton {
    public GridButton(String text) {
        super(text);

        init();
    }

    public GridButton(ImageIcon icon) {
        super(icon);

        init();
    }

    private void init() {
        setBackground(Color.lightGray);

    }

    public void makeClickable() {
        setEnabled(true);
    }

    public void makeUnClickable() {
        setEnabled(false);
    }
}
