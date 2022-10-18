package game;

import org.jetbrains.annotations.NotNull;

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
     * Execute a certain move that changes the game state
     * @param m The type of the move
     * @return Whether the move is successful
     */
    boolean move(@NotNull Move m);

    /**
     * Get a list of all moves that will end with a successful result starting from the current state
     * @return The list of valid moves
     */
    List<Move> getValidMoves();

    /**
     * Evaluate how "good" the current state is. Usually, a state with fewer steps to reach and closer
     * to the target state will be a better state
     * @return A positive integer score of the current state. A lower score represents a better state
     */
    int evaluate();
}
