package view.GridBox;

public class DeadButton extends GridButton {
    public DeadButton(String text, int x, int y) {
        super(text, x, y);
        setEnabled(false);
    }

    @Override
    public void makeAttackable() {

    }

    @Override
    public void makeClickable() {

    }
}
