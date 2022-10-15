package numpuzzle;

public interface Board {
    /**
     * Get the value of a certain position on the board
     * @param r The row of the element
     * @param c The column of the element
     * @return The value of the element
     */
    public int get(int r, int c);

    /**
     * Get the value of a certain position on the board (or default value)
     * @param r The row of the element
     * @param c The column of the element
     * @param defaultValue This will be returned if {@code r} or {@code c} is out of bounds
     * @return The value of the element
     */
    public int get(int r, int c, int defaultValue);

    /**
     * Resets the board to default location
     * @return The board itself
     */
    public Board reset();

    /**
     * Shuffles the board randomly into a VALID state
     * @return The board itself
     */
    public Board shuffle();

    /**
     * Moves the board towards a certain direction.
     * @param move Move toward the direction specified. E.g. if the value is {@code Move.UP} then the element
     *             UNDER the empty cell will move upward
     * @return The board itself
     */
    public Board moveDirection(Move move);

    /**
     * Populate the board with the given state
     * @param state A string made of {@code r * c} characters. Each character is translated into an integer
     *              by their utf-8 code. There are no checks on the validness of the state
     * @return The board itself
     */
    public Board loadState(String state);

    /**
     * Outputs the board's current state
     * @return A string made of {@code r * c} characters. Each character is translated into an integer
     *         by their utf-8 code
     */
    public String getState();

    // Getters & setters
    public int getWidth();
    public int getHeight();
}
