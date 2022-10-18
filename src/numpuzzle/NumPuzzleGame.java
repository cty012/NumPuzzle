package numpuzzle;

import game.Game;
import game.Move;
import org.jetbrains.annotations.NotNull;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class NumPuzzleGame implements Game {
    private final Board board;
    private final StringBuilder pastMoves;

    public NumPuzzleGame(int height, int width) {
        board = new BoardImpl(height, width);
        pastMoves = new StringBuilder();
    }

    @Override
    public void restart() {
        board.reset().shuffle();
        pastMoves.setLength(0);
    }

    @Override
    public void loadState(String state) {
        int h = board.getHeight();
        int w = board.getWidth();
        assert state.length() >= h * w : "The state's length is too short";

        pastMoves.setLength(0);
        pastMoves.append(state.substring(0, state.length() - h * w));

        for (int i = 0; i < h * w; i++) {
            int r = i / w, c = i % w;
            board.set(r, c, state.charAt(i + pastMoves.length()) - '0');
        }
    }

    @Override
    public String getState() {
        int h = board.getHeight();
        int w = board.getWidth();

        StringBuilder state = new StringBuilder("");
        state.append(pastMoves);

        for (int i = 0; i < h * w; i++) {
            int r = i / w, c = i % w;
            state.append((char)(board.get(r, c) + '0'));  // Add '0' for better visualization
        }
        return state.toString();
    }

    @Override
    public boolean move(@NotNull Move direction) {
        Pair emptyCell = board.getEmptyCell();
        Pair target = emptyCell.clone();
        switch ((NumPuzzleMove) direction) {
            case UP:
                target.x++;
                break;
            case DOWN:
                target.x--;
                break;
            case LEFT:
                target.y++;
                break;
            case RIGHT:
                target.y--;
                break;
            default:
                return false;
        }

        // Valid moves will result in valid row and column of target
        if (board.inBound(target.x, target.y)) {
            // Swap target and empty cell
            board.swap(target.x, target.y, emptyCell.x, emptyCell.y);
            pastMoves.append(direction.toChar());
            return true;
        }

        return false;
    }

    @Override
    public boolean undo(){
        int n = pastMoves.length();
        if (n == 0) {
            return false;
        }

        NumPuzzleMove lastMove = NumPuzzleMove.fromChar(pastMoves.charAt(n - 1));
        move(lastMove.reverse());
        pastMoves.setLength(n - 1);

        return true;
    }

    @Override
    public List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>();
        Pair emptyCell = board.getEmptyCell();

        // Find valid moves according to the position of the empty cell
        if (emptyCell.x < board.getHeight() - 1) {
            moves.add(NumPuzzleMove.UP);
        }
        if (emptyCell.x > 0) {
            moves.add(NumPuzzleMove.DOWN);
        }
        if (emptyCell.y < board.getWidth() - 1) {
            moves.add(NumPuzzleMove.LEFT);
        }
        if (emptyCell.y > 0) {
            moves.add(NumPuzzleMove.RIGHT);
        }

        // Remove the last executed move for optimization
        NumPuzzleMove lastMove = NumPuzzleMove.STAY;
        if (pastMoves.length() > 0) {
            lastMove = NumPuzzleMove.fromChar(pastMoves.charAt(pastMoves.length() - 1));
        }
        moves.remove(lastMove.reverse());

        return moves;
    }

    @Override
    public String getPastMoves() {
        return pastMoves.toString();
    }

    @Override
    public int getSteps() {
        return pastMoves.length();
    }

    @Override
    public boolean isFinalState() {
        int h = board.getHeight();
        int w = board.getWidth();
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (board.get(r, c) != (r * w + c + 1) % (h * w)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int evaluate() {
        return evaluate(getState());
    }

    @Override
    public int evaluate(String state) {
        /*
         * For all numbers i, define the target position of i as the position of i in the final state.
         * Define the displacement of i as the sum of row difference and column difference of its
         * current position and target position. Note that each move can only change the displacement
         * of one of the numbers from 1 to h * w - 1. Therefore, the minimum required move is the sum
         * of all displacements of numbers 1 to h * w - 1.
         *
         * Therefore, adding the past number of moves with the minimum required moves gives the total
         * minimum moves. This can be used as a metric for minimizing the total number of steps.
         */
        // UPDATE: The total number of steps is now removed from this method
        return getDisplacement(state);
    }

    // Helper
    public int getDisplacement(String state) {
        int h = board.getHeight();
        int w = board.getWidth();
        state = state.substring(state.length() - h * w);
        int totalDisplacement = h * w + 2 * (h + w);  // Base score

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                int value = state.charAt(r * w + c) - '0';
                if (value == 0) {
                    totalDisplacement -= 3;
                    continue;
                }

                int targetR = (value - 1) / w;
                int targetC = (value - 1) % w;
                int displacement = Math.abs(targetR - r) + Math.abs(targetC - c);
                totalDisplacement += displacement;

                // subtract 1 if the number is in the correct place
                if (displacement == 0) {
                    totalDisplacement -= 1;
                    if ((r == 0 || r == h - 1) && (c == 0 || c == w - 1)) {
                        // subtract another 2 if the number is on the corner
                        totalDisplacement -= 2;
                    } else if (r == 0 || r == h - 1 || c == 0 || c == w - 1) {
                        // subtract another 1 if the number is on the edge
                        totalDisplacement -= 1;
                    }
                }
            }
        }

        return totalDisplacement;
    }

    // Getters & setters
    public int getHeight() {
        return board.getHeight();
    }

    public int getWidth() {
        return board.getWidth();
    }
}
