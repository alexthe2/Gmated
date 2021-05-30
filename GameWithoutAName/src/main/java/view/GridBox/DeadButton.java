package view.GridBox;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static config.Config.*;

public class DeadButton extends GridButton {
    @SneakyThrows
    public DeadButton(int x, int y) {
        super("", x, y);
        ImageIcon icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("lava.png")))
                .getScaledInstance((int) (WIDTH_CHUNK * .95), (int)(HEIGHT_CHUNK * .95), Image.SCALE_SMOOTH));

        setDisabledIcon(icon);
        setIcon(icon);
        setEnabled(false);
    }

    @Override
    public void makeAttackable() {

    }

    @Override
    public void makeClickable() {

    }

    @Override
    public void makeBoth() {

    }

    @Override
    public void makeUnClickable() {

    }
}
