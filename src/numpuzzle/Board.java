package numpuzzle;

import utils.Pair;

public interface Board {
    /**
     * Get the value of a certain position on the board
     * @param r The row of the element
     * @param c The column of the element
     * @return The value of the element
     */
    int get(int r, int c);

    /**
     * Set the value of a certain position on the board
     * @param r The row of the element
     * @param c The column of the element
     * @param value The value being set
     */
    void set(int r, int c, int value);

    /**
     * Resets the board to default location
     * @return The board itself
     */
    Board reset();

    /**
     * Shuffles the board randomly into a VALID state
     * @return The board itself
     */
    Board shuffle();

    /**
     * Swaps the values of two positions on the board
     * @param r1 Row of first position
     * @param c1 Column of first position
     * @param r2 Row of second position
     * @param c2 Column of second position
     */
    void swap(int r1, int c1, int r2, int c2);

    /**
     * Checks if the position is valid
     * @param r The row
     * @param c The column
     * @return Whether the position is valid
     */
    boolean inBound(int r, int c);

    // Getters & setters
    int getHeight();
    int getWidth();

    /**
     * Get the position of the empty cell
     * @return a {@code Pair} containing the position of the empty cell
     */
    Pair getEmptyCell();
}
