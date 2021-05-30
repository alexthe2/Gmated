package view;

import Swings.AdvancedButtonLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.ChessPoint;
import config.Dictionary;
import controller.Controller;
import lombok.SneakyThrows;
import view.GridBox.DeadButton;
import view.GridBox.GridButton;
import view.GridBox.pawns.Archer;
import view.GridBox.pawns.King;
import view.GridBox.pawns.Knight;
import view.GridBox.pawns.Pawn;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static config.Config.*;

public class MainPanel extends JPanel {


    private HashMap<ChessPoint, GridButton> buttons;

    private ObjectMapper mapper;
    private Map config;

    private ChessPoint oldPoint;

    private ArrayList<ChessPoint> attackable;
    private ArrayList<ChessPoint> movable;

    private King kingOrange;
    private King kingPurple;

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

        updateFromYaml();
        constructNoise();
    }

    private GridButton createAtPos(int x, int y) {
        var button = new GridButton(String.format("x-%d : y-%d", x, y), x, y);
        button.loadAsStone();
        add(button);
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
            makeMove();
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

    @SneakyThrows
    private void makeMove() {
        final var x = new Random().nextInt() % 2;
        final var y = new Random().nextInt() % 2;

        ChessPoint point;
        ChessPoint knightPoint;

        if(Controller.getCurrentPlayer() == PURPLE) {
            point = new ChessPoint(kingPurple.getGridx() + x, kingPurple.getGridy() + y);
            knightPoint = new ChessPoint(kingPurple.getGridx(), kingPurple.getGridy());
        } else {
            point = new ChessPoint(kingOrange.getGridx() + x, kingOrange.getGridy() + y);
            knightPoint = new ChessPoint(kingOrange.getGridx(), kingOrange.getGridy());
        }

        final var toMoveTo = buttons.get(point);
        if(toMoveTo != null && !(toMoveTo instanceof DeadButton) && !point.equals(knightPoint)) {
            if(toMoveTo instanceof Pawn) {
                oldPoint = knightPoint;
                attackable.add(point);
                checkHit(point);
            } else {
                swap(point, knightPoint);
            }
        }

        updateUI();
        repaint();

        boolean playable = false;
        Controller.move();
        for(var entry : buttons.entrySet()) {
            if(entry.getValue() instanceof Pawn) {
                var pawn = (Pawn) entry.getValue();
                if(!(pawn instanceof King)) {
                    playable = true;
                    if(pawn.getPlayer() == Controller.getCurrentPlayer()) {
                        return;
                    }
                }
            }
        }

        if(!playable) {
            JOptionPane.showMessageDialog(null, "Draw");
            Thread.currentThread().interrupt();
        }

        TimeUnit.MILLISECONDS.sleep(100);
        makeMove();
    }

    private void replace(GridButton newButton, int x, int y) {
        var button = buttons.get(new ChessPoint(x, y));
        remove((Component) button);
        buttons.remove(button);

        buttons.put(new ChessPoint(x, y), newButton);
        add(newButton);

    }

    public void makeMovable(ChessPoint point) {
        movable.add(point);
        var but = buttons.get(point);
        if(but != null) {
            if(attackable.contains(point)) {
                but.makeBoth();
            } else {
                but.makeClickable();
            }
        }
    }

    public void makeAttackable(ChessPoint point) {
        attackable.add(point);
        var but = buttons.get(point);
        if(but != null) {
            if(movable.contains(point)) {
                but.makeBoth();
            } else {
                but.makeAttackable();
            }
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

        updateUI();
        repaint();
    }

    public void checkHit(ChessPoint toHit) {
        if(oldPoint != null) {
            if(((Pawn) buttons.get(oldPoint)).getPlayer() != ((Pawn) buttons.get(toHit)).getPlayer() && attackable.contains(toHit)) {
                var attacked = (Pawn) buttons.get(toHit);
                var attacker = (Pawn) buttons.get(oldPoint);

                if(!attacked.reduceHealth(attacker.getAttack())) {
                    makeMove();
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
                case "CURRENT" -> {
                    clearClickable();
                    oldPoint = new ChessPoint(((ChessPoint) e.getNewValue()).getX(), ((ChessPoint) e.getNewValue()).getY());
                }
                case Dictionary.FIELD_ATTACKED ->  checkHit((ChessPoint) e.getNewValue());
                default -> throw new IllegalStateException();
            }
        });
        replace(k, x, y);
    }

    @SneakyThrows
    private void updateFromYaml() {
        mapper = new ObjectMapper(new YAMLFactory());
        JFileChooser fileChooser = new JFileChooser("./");
        fileChooser.setDialogTitle("Choose the config.yaml");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getAbsolutePath().endsWith(".yaml") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        fileChooser.showOpenDialog(null);
        config = mapper.readValue(fileChooser.getSelectedFile(), Map.class);

        Map<String, Object> purples = (Map<String, Object>) config.get("purple");
        Map<String, Object> oranges = (Map<String, Object>) config.get("orange");

        for(var king : (ArrayList<Map<String, Object>>) (purples).get("king")) {
            final int x = (Integer) king.get("x");
            final int y = (Integer) king.get("y");

            King k = new King(PURPLE, x, y);
            kingPurple = k;

            registerPawn(k, x, y);
        }

        for(var knight : (ArrayList<Map<String, Object>>) (purples).get("knight")) {
            final int x = (Integer) knight.get("x");
            final int y = (Integer) knight.get("y");

            registerPawn(new Knight(PURPLE, x, y), x, y);
        }

        for(var archer : (ArrayList<Map<String, Object>>) (purples).get("archer")) {
            final int x = (Integer) archer.get("x");
            final int y = (Integer) archer.get("y");

            registerPawn(new Archer(PURPLE, x, y), x, y);
        }

        for(var king : (ArrayList<Map<String, Object>>) (oranges).get("king")) {
            final int x = (Integer) king.get("x");
            final int y = (Integer) king.get("y");

            King k = new King(ORANGE, x, y);
            kingOrange = k;

            registerPawn(k, x, y);
        }

        for(var knight : (ArrayList<Map<String, Object>>) (oranges).get("knight")) {
            final int x = (Integer) knight.get("x");
            final int y = (Integer) knight.get("y");

            registerPawn(new Knight(ORANGE, x, y), x, y);
        }

        for(var archer : (ArrayList<Map<String, Object>>) (oranges).get("archer")) {
            final int x = (Integer) archer.get("x");
            final int y = (Integer) archer.get("y");

            registerPawn(new Archer(ORANGE, x, y), x, y);
        }
    }

    private void constructNoise() {
        final var chunk = SIZE_Y / 3;
        for(int y = chunk; y < 2*chunk; y++) {
            for(int x = 0; x < SIZE_X; x++) {
                if(Math.random() < .2) {
                    replace(new DeadButton(x, y), x, y);
                }
            }
        }
    }
}
