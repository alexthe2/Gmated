package view.GridBox.pawns;

import config.ChessPoint;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * A Knight in the field
 */
public class Knight extends Pawn {
    @Getter
    private PropertyChangeSupport support;

    private KnightMoves moves;

    @SneakyThrows
    public Knight(int color, int x, int y) {
        super("",  x, y);
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("knight%d.png", color))));
        moves = new KnightMoves();

        this.gridx = x;
        this.gridy = y;
        support = new PropertyChangeSupport(this);

        register();
    }

    private void register() {
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
    }

    private void clearing() {
        support.firePropertyChange("CLEAR", false, true);
    }


    private void positions() {
        support.firePropertyChange("CURRENT", null, new ChessPoint(gridx, gridy));

        for(int i = -2; i < 3; i++) {
            support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx + i, gridy));
            support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx, gridy +i));
        }
        support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx -1, gridy -1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx -1, gridy +1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx +1, gridy -1));
        support.firePropertyChange("MOVABLE", null, new ChessPoint(gridx +1, gridy +1));
    }

    public void register(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }


    private static class KnightMoves implements ActionListener {
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
