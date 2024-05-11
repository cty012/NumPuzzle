package slidingpuz;

import game.Game;
import game.Move;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class SlidingPuzGame implements Game {
    private final Board board;
    private final StringBuilder pastMoves;

    public SlidingPuzGame(int height, int width) {
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
    public String getFormattedState() {
        int h = board.getHeight();
        int w = board.getWidth();
        StringBuilder result = new StringBuilder();

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                int val = board.get(r, c);
                result.append('\t');
                if (val == 0) {
                    result.append('*');
                } else {
                    result.append(val);
                }
            }
            result.append('\n');
        }

        // Add the moves at the end
        int steps = getSteps();
        result.append("Moves: ");
        result.append(steps == 0 ? "none" : getPastMoves());
        result.append('\n');

        return result.toString();
    }

    @Override
    public boolean move(Move direction) {
        Pair emptyCell = board.getEmptyCell();
        Pair target = emptyCell.clone();
        switch ((SlidingPuzMove) direction) {
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

        SlidingPuzMove lastMove = SlidingPuzMove.fromChar(pastMoves.charAt(n - 1));
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
            moves.add(SlidingPuzMove.UP);
        }
        if (emptyCell.x > 0) {
            moves.add(SlidingPuzMove.DOWN);
        }
        if (emptyCell.y < board.getWidth() - 1) {
            moves.add(SlidingPuzMove.LEFT);
        }
        if (emptyCell.y > 0) {
            moves.add(SlidingPuzMove.RIGHT);
        }

        // Remove the last executed move for optimization
        SlidingPuzMove lastMove = SlidingPuzMove.STAY;
        if (pastMoves.length() > 0) {
            lastMove = SlidingPuzMove.fromChar(pastMoves.charAt(pastMoves.length() - 1));
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
        return isFinalState(this.getState());
    }

    @Override
    public boolean isFinalState(String state) {
        int h = board.getHeight();
        int w = board.getWidth();
        String trimmedState = state.substring(state.length() - h * w);

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (isIncorrectAt(trimmedState, r, c)) {
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
        // A combination of several heuristic functions
        // Rely on experiments to determine the best combination
        // return manhattanDist(state) + countSquares(state);
        // return manhattanDist(state) + countCornerRect(state, 0b111);
        // return manhattanDist(state) / 2 + countCornerRect(state, 0b100);
        return manhattanDist(state) + countCornerRect(state, 0b111)
                + countCornerRect(state, 0b100);
    }

    // Heuristics
    private int manhattanDist(String state) {
        /*
         * For all numbers i, define the target position of i as the position of i in the final state.
         * Define the displacement of i as the manhattan distance between its current position and
         * target position. Note that each move can only change the displacement of one of the numbers.
         * Therefore, the minimum required move is the sum of all displacements of non-empty cells.
         */
        int h = board.getHeight();
        int w = board.getWidth();
        String trimmedState = state.substring(state.length() - h * w);
        int totalDisplacement = 0;

        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                // Find the value at (r, c)
                int value = trimmedState.charAt(r * w + c) - '0';

                // Skip the empty cell
                if (value == 0) {
                    continue;
                }

                // Find its target location
                int targetR = (value - 1) / w;
                int targetC = (value - 1) % w;

                // Find the manhattan distance
                int displacement = Math.abs(targetR - r) + Math.abs(targetC - c);
                totalDisplacement += displacement;
            }
        }

        return totalDisplacement;
    }

    public int countSquares(String state) {
        /*
         * Intuition: Placing a single number at the correct position is meaningless. We want to make
         * sure that its neighbors are also in the correct spot.
         *
         * The board is scanned with a 2x2 kernel. If at least one of the numbers in the kernel is not
         * correct, we add 1 to the total score.
         */
        int h = board.getHeight();
        int w = board.getWidth();
        String trimmedState = state.substring(state.length() - h * w);
        int score = 0;

        for (int r = 0; r < h - 1; r++) {
            for (int c = 0; c < w - 1; c++) {
                // Check if the 4 numbers int the 2x2 kernel are at the correct position
                if (isIncorrectAt(trimmedState, r, c)
                        || isIncorrectAt(trimmedState, r, c + 1)
                        || isIncorrectAt(trimmedState, r + 1, c)
                        || isIncorrectAt(trimmedState, r + 1, c + 1)) {
                    score++;
                }
            }
        }

        return score;
    }

    public int countCornerRect(String state, int cornerMask) {
        /*
         * Intuition: Consider the following state:
         *                                  [1  12 3  4  ]
         *                                  [5  6  7  8  ]
         *                                  [9  10 11 2  ]
         *                                  [13 14 15 *  ]
         * Both manhattan distance and count squares methods would consider this as a very good state.
         * However, we know that even though only the numbers 2 and 12 are misplaced, we need to move
         * the numbers between them in order to switch 2 and 12.
         *
         * The board is scanned three times starting from the top left corner, top right corner, and
         * bottom left corner. A grid is considered "good" if for at least one corner, the rectangle
         * containing it and the starting corner does not contain misplaced numbers.
         *
         * In some cases not all three corners are used. Therefore, we apply a corner mask taking values
         * from 0 to 7 inclusive. e.g.:
         *                            cornerMask = 5 = binary(101)
         *              ==> only the top left and bottom left corners are considered
         */
        int h = board.getHeight();
        int w = board.getWidth();
        String trimmedState = state.substring(state.length() - h * w);
        int[] arr = new int[h * w];

        // Top left corner (mask = 1)
        int prevEnd = w;  // The column value of the next row cannot exceed the previous row;
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < prevEnd; c++) {
                // End the line if encounter a misplaced element
                if (isIncorrectAt(trimmedState, r, c)) {
                    prevEnd = c;
                    break;
                }

                // Else assign the last bit to be 1
                arr[r * w + c] |= 1;
            }
        }

        // Top right corner (mask = 2)
        prevEnd = -1;
        for (int r = 0; r < h; r++) {
            for (int c = w - 1; c > prevEnd; c--) {
                if (isIncorrectAt(trimmedState, r, c)) {
                    prevEnd = c;
                    break;
                }
                arr[r * w + c] |= 2;
            }
        }

        // Bottom left corner (mask = 4)
        prevEnd = w;
        for (int r = h - 1; r > 0; r--) {
            for (int c = 0; c < prevEnd; c++) {
                if (isIncorrectAt(trimmedState, r, c)) {
                    prevEnd = c;
                    break;
                }
                arr[r * w + c] |= 4;
            }
        }

        // Count the zeros (after applying the corner mask)
        int count = 0;
        for (int i = 0; i < h * w; i++) {
            if ((arr[i] & cornerMask) == 0) {
                count++;
            }
        }

        return count;
    }

    // Helpers
    /**
     * Check if a given TRIMMED state is incorrect (does not match the final state) at a given position.
     * @param trimmedState Trimmed state (without the moves in the front)
     * @param r Row of the given position
     * @param c Column of the given position
     * @return A boolean value representing if the given position is incorrect compared to the final
     *         state
     */
    private boolean isIncorrectAt(String trimmedState, int r, int c) {
        if (!board.inBound(r, c)) return true;
        int h = board.getHeight();
        int w = board.getWidth();
        return trimmedState.charAt(r * w + c) - '0' != (r * w + c + 1) % (h * w);
    }

    // Getters & setters
    public int getHeight() {
        return board.getHeight();
    }

    public int getWidth() {
        return board.getWidth();
    }
}
