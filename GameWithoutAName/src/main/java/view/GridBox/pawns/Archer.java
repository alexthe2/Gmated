package view.GridBox.pawns;

import config.ChessPoint;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Archer extends Pawn {

    private static final int ARCHER_ATTACK = 30;
    private static final int ARCHER_HEALTH = 40;

    @SneakyThrows
    public Archer(int color, int x, int y) {
        super("",  x, y, color, ARCHER_ATTACK, ARCHER_HEALTH);
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("archer%d.png", color))));

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

        for(int i = -3; i < 4; i++) {
            if(i == 0) continue;
            if(Math.abs(i) <= 2) {
                informFieldBoth(gridx + i, gridy);
                informFieldBoth(gridx, gridy + i);
            } else {
                informFieldAttack(gridx + i, gridy);
                informFieldAttack(gridx, gridy + i);
            }
        }


        informFieldMovable(gridx - 1, gridy - 1);
        informFieldMovable(gridx - 1, gridy + 1);
        informFieldMovable(gridx + 1, gridy - 1);
        informFieldMovable(gridx + 1, gridy + 1);
    }


    @Override
    public void onRescale(int width, int height) {
        setIcon(new ImageIcon(image.getScaledInstance((int) (width * .95), (int) (height * .95), Image.SCALE_FAST)));
    }
}
