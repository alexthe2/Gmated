package view.GridBox.pawns;

import Swings.IHotRescale;
import view.GridBox.GridButton;

import javax.swing.*;

public abstract class Pawn extends GridButton implements IHotRescale {
    public Pawn(String text) {
        super(text);
    }

    public Pawn(ImageIcon icon) {
        super(icon);
    }
}
