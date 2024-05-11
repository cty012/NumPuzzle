package game;

import java.util.List;

public interface Game {
    /**
     * Restart the game to its initial position
     */
    void restart();

    /**
     * Populate the board with the given state
     * @param state A string made of {@code r * c} characters. Each character is translated into an integer
     *              by their utf-8 code. There are no checks on the validness of the state
     */
    void loadState(String state);

    /**
     * Outputs the board's current state
     * @return A string made of {@code r * c} characters. Each character is translated into an integer
     *         by their utf-8 code
     */
    String getState();

    /**
     * Outputs the formatted state for better visualization when printed to the console
     * @return The formatted string
     */
    String getFormattedState();

    /**
     * Execute a certain move that changes the game state
     * @param m The type of the move
     * @return Whether the move is successful
     */
    boolean move(Move m);

    /**
     * Undo the last move. Note that for some games this will always fail
     * @return Whether the undo is successful
     */
    boolean undo();

    /**
     * Get a list of all moves that will end with a successful result starting from the current state
     * @return The list of valid moves
     */
    List<Move> getValidMoves();

    /**
     * Get a string of chars representing the order of the moves executed
     */
    String getPastMoves();

    /**
     * Get the number of steps already taken
     * @return The number of steps
     */
    int getSteps();

    /**
     * Checks if the CURRENT state is the final state
     * @return A boolean value representing whether the CURRENT state is the final state
     */
    boolean isFinalState();

    /**
     * Checks if the GIVEN state is the final state
     * @return A boolean value representing whether the GIVEN state is the final state
     */
    boolean isFinalState(String state);

    /**
     * Evaluate how "good" the CURRENT state is. Usually, a state with fewer steps to reach AND closer
     * to the target state will be a better state
     * @return An integer score of the CURRENT state. A lower score represents a better state
     */
    int evaluate();

    /**
     * Evaluate how "good" the GIVEN state is. Usually, a state with fewer steps to reach AND closer
     * to the target state will be a better state
     * @param state The state to be evaluated
     * @return An integer score of the GIVEN state. A lower score represents a better state
     */
    int evaluate(String state);
}
