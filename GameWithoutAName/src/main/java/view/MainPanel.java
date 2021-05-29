package view;

import Swings.AdvancedGridLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.ChessPoint;
import lombok.SneakyThrows;
import view.GridBox.GridButton;
import view.GridBox.pawns.King;
import view.GridBox.pawns.Knight;
import view.GridBox.pawns.Pawn;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static config.Config.*;

public class MainPanel extends JPanel {
    private HashMap<ChessPoint, GridButton> buttons;

    private ObjectMapper mapper;
    private Map config;

    private ChessPoint oldPoint;

    public MainPanel() {
        super();

        buttons = new HashMap<>(SIZE_X * SIZE_Y);
        setBackground(Color.darkGray);

        construct();
    }

    private void construct() {
        setLayout(new AdvancedGridLayout(SIZE_Y, SIZE_X));

        for(var y = 0; y < SIZE_Y; y++) {
            for(var x = 0; x < SIZE_X; x++) {
                var button = new GridButton(String.format("x-%d : y-%d", x, y), x, y);
                add(button, y * SIZE_X + x);
                buttons.put(new ChessPoint(x,y), button);
                button.addActionListener(e -> {
                    checkSwap(button.getGridx(), button.getGridy());
                });
                button.makeUnClickable();
            }
        }

        updateFromYaml("E:\\Gmated\\GameWithoutAName\\src\\main\\resources\\config.yaml");
    }

    private void checkSwap(int x, int y) {
        if (oldPoint != null) {
            swap(new ChessPoint(x, y), oldPoint);
        }
    }

    private void swap(ChessPoint p1, ChessPoint p2) {
        var button1 = buttons.get(p1);
        var button2 =  buttons.get(p2);

        button1.setGridx(p2.getX());
        button1.setGridy(p2.getY());
        button2.setGridx(p1.getX());
        button2.setGridy(p1.getY());

        buttons.replace(p1, button1, button2);
        buttons.replace(p2, button2, button1);

        remove(button1);
        remove(button2);

        add(button1, p2.getY() * SIZE_X + p2.getX());
        add(button2, p1.getY() * SIZE_X + p1.getX());

        clearClickable();

        updateUI();
        repaint();
    }

    private void replace(GridButton newButton, int x, int y) {
        var button = buttons.get(new ChessPoint(x, y));
        remove((Component) button);
        buttons.remove(button);

        buttons.put(new ChessPoint(x, y), newButton);
        add(newButton, y*SIZE_X + x);

    }

    public void makeClickable(ChessPoint point) {
        var but = buttons.get(point);
        if(but != null) {
            but.makeClickable();
        }
    }

    public void clearClickable() {
        oldPoint = null;
        buttons.forEach((chessPoint, gridButton) -> {
            if(!(gridButton instanceof Pawn))
                gridButton.makeUnClickable();
        });
    }

    @SneakyThrows
    private void updateFromYaml(String yaml) {
        mapper = new ObjectMapper(new YAMLFactory());
        config = mapper.readValue(new File(yaml), Map.class);

        Map<String, Object> purples = (Map<String, Object>) config.get("purple");
        for(var king : (ArrayList<Map<String, Object>>) (purples).get("king")) {
            final int x = (Integer) king.get("x");
            final int y = (Integer) king.get("y");

            replace(new King(1, x, y), x, y);
        }

        for(var knight : (ArrayList<Map<String, Object>>) (purples).get("knight")) {
            final int x = (Integer) knight.get("x");
            final int y = (Integer) knight.get("y");

            var k = new Knight(1, x, y);
            k.register(e -> {
                switch (e.getPropertyName()) {
                    case "CLEAR" -> clearClickable();
                    case "MOVABLE" -> makeClickable((ChessPoint) e.getNewValue());
                    case "CURRENT" -> oldPoint = new ChessPoint(((ChessPoint) e.getNewValue()).getX(), ((ChessPoint) e.getNewValue()).getY());
                    default -> throw new IllegalStateException();
                }
            });
            replace(k, x, y);
        }
    }
}
