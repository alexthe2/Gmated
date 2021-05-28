package view;

import javax.swing.*;
import java.awt.*;

import static config.Config.*;

public class Application extends JFrame {


    public Application(String name) {
        super(name);

        setLayout(new GridLayout(1,1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH_CHUNK * SIZE_X, HEIGHT_CHUNK * SIZE_Y));

        add(new MainPanel());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
