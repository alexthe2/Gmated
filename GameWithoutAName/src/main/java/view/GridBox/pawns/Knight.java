package view.GridBox.pawns;

import config.ChessPoint;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * A Knight in the field
 */
public class Knight extends Pawn {

    private static final int KNIGHT_ATTACK = 20;
    private static final int KNIGHT_HEALTH = 50;

    @SneakyThrows
    public Knight(int color, int x, int y) {
        super("",  x, y, color, KNIGHT_ATTACK, KNIGHT_HEALTH);
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("knight%d.png", color))));

        this.gridx = x;
        this.gridy = y;
        support = new PropertyChangeSupport(this);

        register();
    }

    private void register() {
        addActionListener(e -> {
            if(!canMove()) {
                informClicked();
                return;
            }


            clicked = !clicked;
            if (!clicked) {
                super.informClear();
            } else {
                positions();
            }
        });
    }

    private void positions() {
        support.firePropertyChange("CURRENT", null, new ChessPoint(gridx, gridy));

        for(int i = -2; i < 3; i++) {
            if(i == 0) continue;
            if(Math.abs(i)  == 1) {
                informFieldBoth(gridx + i, gridy);
                informFieldBoth(gridx, gridy + i);
            } else {
                informFieldMovable(gridx + i, gridy);
                informFieldMovable(gridx, gridy + i);
            }
        }


        informFieldBoth(gridx - 1, gridy - 1);
        informFieldBoth(gridx - 1, gridy + 1);
        informFieldBoth(gridx + 1, gridy - 1);
        informFieldBoth(gridx + 1, gridy + 1);
    }


    @Override
    public void onRescale(int width, int height) {
        setIcon(new ImageIcon(image.getScaledInstance((int) (width * .95), (int) (height * .95), Image.SCALE_FAST)));
    }
}
