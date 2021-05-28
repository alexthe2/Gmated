package Swings;

/**
 * Interface which specifies that Components can rescale themself, when the Layout is rearranged
 */
public interface IHotRescale {

    /**
     * Inform a component that it has been rescaled
     * @param width The new width which it has become
     * @param height The new height which it has become
     */
    void onRescale(int width, int height);
}
