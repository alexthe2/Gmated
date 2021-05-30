package view.GridBox;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static config.Config.HEIGHT_CHUNK;
import static config.Config.WIDTH_CHUNK;

/**
 * A Custom Button for GMated
 */
@Setter
@Getter
public class GridButton extends JButton {
    protected int gridx;
    protected int gridy;

    private ImageIcon disabledIcon;
    private ImageIcon selectableIcon;
    private ImageIcon attackableIcon;
    private ImageIcon bothIcon;

    /**
     * Create a GridButton
     * @param text
     * @param x
     * @param y
     */
    public GridButton(String text, int x, int y) {
        super(text);

        this.gridx = x;
        this.gridy = y;
        init();
    }

    /**
     * Create a GridButton
     * @param icon
     * @param x
     * @param y
     */
    public GridButton(ImageIcon icon, int x, int y) {
        super(icon);

        this.gridx = x;
        this.gridy = y;
        init();
    }

    @SneakyThrows
    public void loadAsStone() {
        setText("");

        disabledIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("stone.png")))
                .getScaledInstance((int) (WIDTH_CHUNK * .95), (int)(HEIGHT_CHUNK * .95), Image.SCALE_SMOOTH));

        selectableIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("stoneHighLight.png")))
                .getScaledInstance((int) (WIDTH_CHUNK * .95), (int)(HEIGHT_CHUNK * .95), Image.SCALE_SMOOTH));

        attackableIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("stoneHighRed.png")))
                .getScaledInstance((int) (WIDTH_CHUNK * .95), (int)(HEIGHT_CHUNK * .95), Image.SCALE_SMOOTH));

        bothIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("stoneHighBoth.png")))
                .getScaledInstance((int) (WIDTH_CHUNK * .95), (int)(HEIGHT_CHUNK * .95), Image.SCALE_SMOOTH));
    }

    /**
     * Init the button
     */
    private void init() {
        setBackground(Color.lightGray);
    }

    /**
     * Make it clickable
     */
    public void makeClickable() {
        setEnabled(true);

        setIcon(selectableIcon);
    }

    public void makeAttackable() {
        setEnabled(true);

        setIcon(attackableIcon);
    }

    public void makeBoth() {
        setEnabled(true);

        setIcon(bothIcon);
    }


    /**
     * Make it unclickable
     */
    public void makeUnClickable() {
        setIcon(disabledIcon);
        setEnabled(false);
    }
}
