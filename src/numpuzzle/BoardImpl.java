package numpuzzle;


import org.jetbrains.annotations.NotNull;
import utils.Pair;

import java.util.Random;

public class BoardImpl implements Board {
    private final int width;
    private final int height;
    private final Pair emptyCell;
    private final int[][] board;

    public BoardImpl(int width, int height) {
        this.width = width;
        this.height = height;
        board = new int[height][width];
        emptyCell = new Pair();
        reset();
    }

    @Override
    public int get(int r, int c) {
        assert inBound(r, c) : "(r, c) = (" + r + ", " + c + ") is out of bounds";
        return board[r][c];
    }

    @Override
    public int get(int r, int c, int defaultValue) {
        if (!inBound(r, c)) {
            return defaultValue;
        } else {
            return board[r][c];
        }
    }

    @Override
    public Board reset() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                board[r][c] = (r * width + c + 1) % (height * width);
            }
        }
        emptyCell.x = height - 1;
        emptyCell.y = width - 1;
        return this;
    }

    @Override
    public Board shuffle() {
        // TODO: Current implementation does not guarantee the result is a legal state
        /*
         * Hint: count the inversions (excluding empty cell)
         * If w ODD, then the inversions must be EVEN
         * If w EVEN, then the inversions must be:
         *  (1) empty cell on ODD row: EVEN
         *  (2) empty cell on EVEN row: ODD
         * Note: the parity of empty cell row is determined by its index (starts from 0)
         */
        Random rand = new Random();
        int maximum = height * width - 1;
        for (int i = 0; i < maximum; i++) {
            // Find a random index >= i
            int j = i + rand.nextInt(maximum - (i + 1) + 1);
            // Switch the contents of two indexes
            int ir = i / width, ic = i % width;
            int jr = j / width, jc = j % width;
            swap(ir, ic, jr, jc);
        }
        return this;
    }

    @Override
    public Board moveDirection(@NotNull Move move) {
        Pair target = emptyCell.clone();
        switch (move) {
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
                return this;
        }

        if (inBound(target.x, target.y)) {
            swap(target.x, target.y, emptyCell.x, emptyCell.y);
            emptyCell.x = target.x;
            emptyCell.y = target.y;
        }

        return this;
    }

    @Override
    public Board loadState(String state) {
        // TODO
        return this;
    }

    @Override
    public String getState() {
        // TODO
        return "";
    }

    // Helpers
    private boolean inBound(int r, int c) {
        return r >= 0 && r < height && c >= 0 && c < width;
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
    }

    // Getters & setters
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
