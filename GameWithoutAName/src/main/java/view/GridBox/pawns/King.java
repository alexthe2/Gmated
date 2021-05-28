package view.GridBox.pawns;

import Swings.IHotRescale;
import lombok.SneakyThrows;
import view.GridBox.GridButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class King extends Pawn {
    private BufferedImage image;

    @SneakyThrows
    public King(int color) {
        super("");
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("king%d.png", color))));
    }

    @Override
    public void onRescale(int width, int height) {
        setIcon(new ImageIcon(image.getScaledInstance((int) (width * .95), (int) (height * .95), Image.SCALE_FAST)));
    }
}
