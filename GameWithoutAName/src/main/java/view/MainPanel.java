package view;

import config.ChessPoint;
import view.GridBox.GridButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import static config.Config.*;

public class MainPanel extends JPanel {
    private HashMap<ChessPoint, GridButton> buttons;

    public MainPanel() {
        super();

        buttons = new HashMap<>(SIZE_X * SIZE_Y);

        construct();
    }

    private void construct() {
        setLayout(new GridLayout(SIZE_Y, SIZE_X));

        for(var y = 0; y < SIZE_Y; y++) {
            for(var x = 0; x < SIZE_X; x++) {
                var button = new GridButton(String.format("x-%d : y-%d", x, y));
                add(button);
                buttons.put(new ChessPoint(x,y), button);
            }
        }

    }
}
