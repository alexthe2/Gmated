package view;

import Swings.AdvancedButtonLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.ChessPoint;
import config.Dictionary;
import controller.Controller;
import lombok.SneakyThrows;
import riven.PerlinNoise;
import view.GridBox.GridButton;
import view.GridBox.pawns.Archer;
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

    private ArrayList<ChessPoint> attackable;
    private ArrayList<ChessPoint> movable;

    public MainPanel() {
        super();

        buttons = new HashMap<>(SIZE_X * SIZE_Y);
        attackable = new ArrayList<>();
        movable = new ArrayList<>();
        setBackground(Color.darkGray);

        construct();
    }

    private void construct() {
        setLayout(new AdvancedButtonLayout(SIZE_Y, SIZE_X, WIDTH_CHUNK, buttons));

        for(var y = 0; y < SIZE_Y; y++) {
            for(var x = 0; x < SIZE_X; x++) {
                createAtPos(x, y);
            }
        }

        updateFromYaml("C:\\Users\\blesi\\Documents\\school\\Gmated\\GameWithoutAName\\src\\main\\resources\\config.yaml");
    }

    private GridButton createAtPos(int x, int y) {
        var button = new GridButton(String.format("x-%d : y-%d", x, y), x, y);
        add(button, y * SIZE_X + x);
        buttons.put(new ChessPoint(x,y), button);
        button.addActionListener(e -> {
            checkSwap(button.getGridx(), button.getGridy());
        });
        button.makeUnClickable();

        return button;
    }
    private void checkSwap(int x, int y) {
        if (oldPoint != null && movable.contains(new ChessPoint(x, y))) {
            swapAndClear(new ChessPoint(x, y), oldPoint);
            Controller.move();
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

        if(!(button1 instanceof Pawn)) {
            button1.setText(String.format("x-%d : y-%d", p2.getX(), p2.getY()));
        }

        if(!(button2 instanceof Pawn)) {
            button2.setText(String.format("x-%d : y-%d", p1.getX(), p1.getY()));
        }

    }

    private void clear() {
        clearClickable();

        updateUI();
        repaint();
    }

    private void swapAndClear(ChessPoint p1, ChessPoint p2) {
        swap(p1, p2);
        clear();
    }

    private void replace(GridButton newButton, int x, int y) {
        var button = buttons.get(new ChessPoint(x, y));
        remove((Component) button);
        buttons.remove(button);

        buttons.put(new ChessPoint(x, y), newButton);
        add(newButton, y*SIZE_X + x);

    }

    public void makeMovable(ChessPoint point) {
        movable.add(point);
        var but = buttons.get(point);
        if(but != null) {
            but.makeClickable();
        }
    }

    public void makeAttackable(ChessPoint point) {
        attackable.add(point);
        var but = buttons.get(point);
        if(but != null) {
            but.makeAttackable();
        }
    }

    public void clearClickable() {
        oldPoint = null;
        attackable.clear();
        movable.clear();
        buttons.forEach((chessPoint, gridButton) -> {
            if(!(gridButton instanceof Pawn))
                gridButton.makeUnClickable();
            else
                ((Pawn)gridButton).setClicked(false);
        });
    }

    public void checkHit(ChessPoint toHit) {
        if(oldPoint != null) {
            if(((Pawn) buttons.get(oldPoint)).getPlayer() != ((Pawn) buttons.get(toHit)).getPlayer() && attackable.contains(toHit)) {
                var attacked = (Pawn) buttons.get(toHit);
                var attacker = (Pawn) buttons.get(oldPoint);

                if(!attacked.reduceHealth(attacker.getAttack())) {
                    Controller.move();
                    clearClickable();
                    return;
                }


                var toRemove = buttons.get(toHit);
                buttons.remove(toHit);
                remove(toRemove);
                createAtPos(toHit.getX(), toHit.getY());

                clear();
            }
        }
    }

    private void registerPawn(Pawn k, int x, int y) {

        k.register(e -> {
            switch (e.getPropertyName()) {
                case Dictionary.FIELD_CLEAR -> clearClickable();
                case Dictionary.FIELD_MOVABLE -> makeMovable((ChessPoint) e.getNewValue());
                case Dictionary.FIELD_ATTACK ->  makeAttackable((ChessPoint) e.getNewValue());
                case "CURRENT" -> oldPoint = new ChessPoint(((ChessPoint) e.getNewValue()).getX(), ((ChessPoint) e.getNewValue()).getY());
                case Dictionary.FIELD_ATTACKED ->  checkHit((ChessPoint) e.getNewValue());
                default -> throw new IllegalStateException();
            }
        });
        replace(k, x, y);
    }

    @SneakyThrows
    private void updateFromYaml(String yaml) {
        mapper = new ObjectMapper(new YAMLFactory());
        config = mapper.readValue(new File(yaml), Map.class);

        Map<String, Object> purples = (Map<String, Object>) config.get("purple");
        Map<String, Object> oranges = (Map<String, Object>) config.get("orange");

        for(var king : (ArrayList<Map<String, Object>>) (purples).get("king")) {
            final int x = (Integer) king.get("x");
            final int y = (Integer) king.get("y");

            registerPawn(new King(0, x, y), x, y);
        }

        for(var knight : (ArrayList<Map<String, Object>>) (purples).get("knight")) {
            final int x = (Integer) knight.get("x");
            final int y = (Integer) knight.get("y");

            registerPawn(new Knight(0, x, y), x, y);
        }

        for(var archer : (ArrayList<Map<String, Object>>) (purples).get("archer")) {
            final int x = (Integer) archer.get("x");
            final int y = (Integer) archer.get("y");

            registerPawn(new Archer(0, x, y), x, y);
        }

        for(var king : (ArrayList<Map<String, Object>>) (oranges).get("king")) {
            final int x = (Integer) king.get("x");
            final int y = (Integer) king.get("y");

            registerPawn(new King(1, x, y), x, y);
        }

        for(var knight : (ArrayList<Map<String, Object>>) (oranges).get("knight")) {
            final int x = (Integer) knight.get("x");
            final int y = (Integer) knight.get("y");

            registerPawn(new Knight(1, x, y), x, y);
        }

        for(var archer : (ArrayList<Map<String, Object>>) (oranges).get("archer")) {
            final int x = (Integer) archer.get("x");
            final int y = (Integer) archer.get("y");

            registerPawn(new Archer(1, x, y), x, y);
        }
    }

    private void constructNoise() {
        PerlinNoise noise =  new PerlinNoise((int) (System.currentTimeMillis() % Integer.MAX_VALUE));

        final var chunk = SIZE_Y / 3;
        for(int y = chunk; y < 2*chunk; y++) {
            for(int x = 0; x < chunk; x++) {

            }
        }
    }
}
