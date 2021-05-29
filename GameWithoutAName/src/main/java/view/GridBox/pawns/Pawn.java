package view.GridBox.pawns;

import Swings.IHotRescale;
import config.ChessPoint;
import config.Dictionary;
import controller.Controller;
import lombok.Getter;
import lombok.Setter;
import view.GridBox.GridButton;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A Pawn in the game field
 */
public abstract class Pawn extends GridButton implements IHotRescale {
    @Getter
    protected final int player;

    protected BufferedImage image;

    @Setter
    protected boolean clicked = false;

    @Getter
    protected PropertyChangeSupport support;

    protected Pawn(String text, int x, int y, int player) {
        super(text, x, y);
        this.player = player;

        support = new PropertyChangeSupport(this);
    }

    protected Pawn(ImageIcon icon, int x, int y, int player) {
        super(icon, x, y);
        this.player = player;

        support = new PropertyChangeSupport(this);
    }

    protected boolean canMove() {
        return Controller.getCurrentPlayer() == player;
    }

    protected void informFieldMovable(int x, int y) {
        if(canMove()) {
            support.firePropertyChange(Dictionary.FIELD_MOVABLE, null, new ChessPoint(x, y));
        }
    }

    protected void informClear() {
        if(canMove()) {
            support.firePropertyChange(Dictionary.FIELD_CLEAR, false, true);
        }
    }

    public void register(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
 }
