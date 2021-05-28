package view.GridBox.pawns;

import Swings.IHotRescale;
import view.GridBox.GridButton;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * A Pawn in the game field
 */
public abstract class Pawn extends GridButton implements IHotRescale {
    protected BufferedImage image;

    public Pawn(String text, int x, int y) {
        super(text, x, y);
    }

    public Pawn(ImageIcon icon, int x, int y) {
        super(icon, x, y);
    }
}
