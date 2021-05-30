package view.GridBox.pawns;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static config.Config.ORANGE;
import static config.Config.PURPLE;

/**
 * A King
 */
public class King extends Pawn {
    private static final int KING_ATTACK = 10;
    private static final int KING_HEALTH = 80;


    /**
     * Create a King
     * @param color
     * @param x
     * @param y
     */
    @SneakyThrows
    public King(int color, int x, int y) {
        super("", x, y, color, KING_ATTACK, KING_HEALTH);
        image = ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(String.format("king%d.png", color))));

        register();
    }

    private void register() {
        addActionListener(e -> informClicked());
    }

    /**
     * When rescaled, also rescale the king
     * @param width The new width which it has become
     * @param height The new height which it has become
     */
    @Override
    public void onRescale(int width, int height) {
        setIcon(new ImageIcon(image.getScaledInstance((int) (width * .95), (int) (height * .95), Image.SCALE_FAST)));
    }

    @Override
    public boolean reduceHealth(int amount) {
        boolean end = super.reduceHealth(amount);
        if(end) {
            JOptionPane.showMessageDialog(null, String.format("%s won", player == ORANGE ? "Purple" : "Orange"));
            Thread.currentThread().interrupt();
        }

        return end;
    }
}
