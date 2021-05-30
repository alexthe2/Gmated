package view.GridBox.pawns;

import Swings.IHotRescale;
import config.ChessPoint;
import config.Dictionary;
import controller.Controller;
import lombok.Getter;
import lombok.Setter;
import view.GridBox.GridButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A Pawn in the game field
 */
public abstract class Pawn extends GridButton implements IHotRescale {
    @Getter
    protected int player;

    protected BufferedImage image;

    @Setter
    protected boolean clicked = false;

    @Getter
    protected PropertyChangeSupport support;

    @Getter
    protected int attack;

    @Getter
    protected int health;

    protected Pawn(String text, int x, int y, int player, int attack, int health) {
        super(text, x, y);

        construct(player, attack, health);
    }

    protected Pawn(ImageIcon icon, int x, int y, int player, int attack, int health) {
        super(icon, x, y);
        construct(player, attack, health);
    }

    private void updateSubText() {
        setText(String.format("%d|%d", attack, health));
    }

    protected void informFieldBoth(int x, int y) {
        informFieldMovable(x, y);
        informFieldAttack(x, y);
    }

    private void construct(int player, int attack, int health) {
        this.player = player;
        this.attack = attack;
        this.health = health;

        setFont(new Font("Arial", Font.BOLD, 15));
        setForeground(new Color(0, 0, 0));


        support = new PropertyChangeSupport(this);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        updateSubText();
    }

    protected boolean canMove() {
        return Controller.getCurrentPlayer() == player;
    }

    protected void informFieldMovable(int x, int y) {
        if(canMove()) {
            support.firePropertyChange(Dictionary.FIELD_MOVABLE, null, new ChessPoint(x, y));
        }
    }

    protected void informFieldAttack(int x, int y) {
        if(canMove()) {
            support.firePropertyChange(Dictionary.FIELD_ATTACK, null, new ChessPoint(x, y));
        }
    }

    protected void informClear() {
        if(canMove()) {
            support.firePropertyChange(Dictionary.FIELD_CLEAR, false, true);
        }
    }

    protected void informClicked() {
        if(!canMove()) {
            support.firePropertyChange(Dictionary.FIELD_ATTACKED, null,  new ChessPoint(gridx, gridy));
        }
    }

    public void register(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public boolean reduceHealth(int amount) {
        health  -= amount;
        updateSubText();

        return health <= 0;
    }
 }
