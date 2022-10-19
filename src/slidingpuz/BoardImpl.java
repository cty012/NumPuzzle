package slidingpuz;

import utils.Pair;

import java.util.Random;

public class BoardImpl implements Board {
    private final int height;
    private final int width;
    private final Pair emptyCell;
    private final int[][] board;

    public BoardImpl(int height, int width) {
        assert height >= 2 && width >= 2 : "Height and width need to be at least 2";
        this.height = height;
        this.width = width;
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
    public void set(int r, int c, int value) {
        assert inBound(r, c) : "(r, c) = (" + r + ", " + c + ") is out of bounds";
        board[r][c] = value;
        // Update empty cell
        if (value == 0) {
            emptyCell.x = r;
            emptyCell.y = c;
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
        Random rand = new Random();
        int maximum = height * width - 1;
        for (int i = 0; i < maximum; i++) {
            // Find a random index >= i
            int j = i + rand.nextInt(maximum - i + 1);
            // Switch the contents of two indexes
            int ir = i / width, ic = i % width;
            int jr = j / width, jc = j % width;
            swap(ir, ic, jr, jc);
            // Update the row and column of the empty cell
            if (board[ir][ic] == 0) {
                emptyCell.x = ir;
                emptyCell.y = ic;
            }
        }

        /*
         * To guarantee a valid state: The board can be treated as a permutation (The empty cell is h*w).
         * Then, the current state is valid <==> the parity of the permutation is the same as the parity
         *   of the displacement (diff_row + diff_col) of the empty cell from the bottom right corner.
         */
        boolean stateIsEven = (parity() == 1);
        int displacement = ((height-1) - emptyCell.x) + ((width-1) - emptyCell.y);
        boolean dispIsEven = displacement % 2 == 0;
        if (stateIsEven != dispIsEven) {
            // The parity of current state is incorrect
            // Thus we need to flip it back by swapping the last two elements in the last row
            //   that does not contain the empty cell
            if (emptyCell.x == height - 1) {
                swap(height-2, width-2, height-2, width-1);
            } else {
                swap(height-1, width-2, height-1, width-1);
            }
        }

        return this;

        /*
         * Proof of this approach:
         * Note that all valid states can be transformed to the default state within finite steps.
         * Each step is a transposition, thus flips the parity of the state. We also know that the
         * parity of the default state is even. Therefore, the parity of the current state is equal
         * to the parity of the steps required to transform to the default state.
         *
         * On the other hand, each step moves the empty cell to an adjacent location, thus changing
         * the parity of its displacement from the bottom right corner. Therefore, the parity of the
         * displacement is also equal to the steps taken to transform to the default state.
         *
         * Therefore, state is valid ==> parity of state's permutation == parity of empty cell's
         * displacement from bottom right corner.
         *
         * We still need to prove the other direction. Let's take width = 3 and height = 4 for example.
         * We define e and e' as the following states:
         *                     [1  2  3  4 ]                [1  2  3  4 ]
         *                 e = [5  6  7  8 ]    and    e' = [5  6  7  11]
         *                     [9  10 11 * ]                [9  10 8  * ]
         * Note that all states p can be transformed into either e or e' (but not both), so we can
         * categorize them into two groups E and E'. Define phi as a function that switches the 8
         * and 11 entries of p. Then phi is a bijection (phi is the inverse of itself). Also, p and
         * phi(p) cannot belong to the same group for obvious reasons. Therefore, |E| == |E'|. Since
         * all states in E are valid, then the number of valid states is half of all permutations.
         * This is sufficient to prove that: parity of state's permutation == parity of empty cell's
         * displacement from bottom right corner ==> state is valid.
         */
    }

    @Override
    public void swap(int r1, int c1, int r2, int c2) {
        int temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
        // Check if empty cell is swapped
        if (board[r1][c1] == 0) {
            emptyCell.set(r1, c1);
        } else if (board[r2][c2] == 0) {
            emptyCell.set(r2, c2);
        }
    }

    @Override
    public boolean inBound(int r, int c) {
        return r >= 0 && r < height && c >= 0 && c < width;
    }

    // Helpers
    /**
     * Finds the parity of the board's permutation (the empty cell = w*h)
     * @return -1 if odd permutation and 1 if even permutation
     */
    private int parity() {
        /*
         * Note that the board is a permutation of numbers 1 ~ width * height
         * All permutations can be decomposed into disjoint cycles
         * The parity of the permutation is the product of the parity of cycles
         * The parity of each cycle is calculated as (-1) ^ (length-1)
         * Therefore, parity of permutation = parity of (length - number of cycles)
         */

        // index 0 actually refers to the empty cell / bottom right position
        boolean[] permutation = new boolean[height * width];
        int numCycles = 0;
        for (int i = 0; i < height * width; i++) {
            // Skip if i is already used in previous cycles
            if (permutation[i]) continue;
            int next = i;
            while (!permutation[next]) {
                permutation[next] = true;
                // Shift backward by 1 to match the board's index
                next = (next + height * width - 1) % (height * width);
                int ni = next / width, nj = next % width;
                next = board[ni][nj];
            }
            numCycles++;
        }

        return (height * width - numCycles) % 2 == 0 ? 1 : -1;
    }

    // Getters & setters
    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Pair getEmptyCell() {
        return emptyCell.clone();
    }
}
