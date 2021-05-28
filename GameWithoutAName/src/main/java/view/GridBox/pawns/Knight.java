package view.GridBox.pawns;

import Swings.IHotRescale;
import config.ChessPoint;
import lombok.Getter;
import lombok.SneakyThrows;
import view.GridBox.GridButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Knight extends Pawn {
    private BufferedImage image;
    @Getter
    private PropertyChangeSupport support;

    private int x, y;

    @SneakyThrows
    public Knight(int color, int x, int y) {
        super("");
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("knight%d.png", color))));
        KnightMoves moves = new KnightMoves();
        moves.register(e -> {
            if(e.getPropertyName().equals("CLICKED")) {
                if ((boolean) e.getNewValue()) {
                    positions();
                } else {
                    clearing();
                }
            }
        });
        addActionListener(moves);

        this.x = x;
        this.y = y;
        support = new PropertyChangeSupport(this);
    }

    private void clearing() {
        support.firePropertyChange("CLEAR", false, true);
    }


    private void positions() {
        for(int i = -2; i < 3; i++) {
            support.firePropertyChange("MOVABLE", null, new ChessPoint(x + i, y));
            support.firePropertyChange("MOVABLE", null, new ChessPoint(x, y+i));
        }
        support.firePropertyChange("MOVABLE", null, new ChessPoint(x-1, y-1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(x-1, y+1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(x+1, y-1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(x+1, y+1));
    }

    public void register(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void swap(int x1, int y1, int x2, int y2) {

    }

    private class KnightMoves implements ActionListener {
        private boolean clicked = false;
        private PropertyChangeSupport support;

        public KnightMoves()  {
            support = new PropertyChangeSupport(this);
        }

        public void register(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            support.firePropertyChange("CLICKED", clicked, !clicked);
            clicked = !clicked;
        }
    }

    @Override
    public void onRescale(int width, int height) {
        setIcon(new ImageIcon(image.getScaledInstance((int) (width * .95), (int) (height * .95), Image.SCALE_FAST)));
    }
}
